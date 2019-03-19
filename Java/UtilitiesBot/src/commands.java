import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class commands implements MessageCreateListener {
    //Vars
    private String prefix;
    private Random rng = new Random();
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    //Constructor
    public commands(String prefix) {
        this.prefix = prefix;
    }

    //The big boi method. Gets called every time a message is sent
    //We need to read the message and see if it's a command, and act occordingly

    @Override 
    public void onMessageCreate(MessageCreateEvent event) {
        //Help text for help commands
        String helpText = "The prefix for commands is currently '" + prefix
                + "', you can use this to issue commands to me\n"
                + "The 'help' command shows this text. You can also mention me, like such '@UtilitiesBot help'\n"
                + "The 'ping' command makes me say 'Pong!', this is mainly used to make sure I am working fine\n"
                + "The 'flipacoin' command is used to flip a coin, I will flip a coin and reply 'heads' or 'tails'\n"
                + "The 'pickrandom' command is used to pick a random value from a list of values, usage is as follows: "
                + "\n\tpickrandom|[val1]|[val2]|[val3]...\n "
                + "The 'domaths' command is used to perform some basic maths operations, you can use it like this\n\tdomaths|[Expression]"
                + "The 'changeprefix' command is used to change the prefix. It can only be used by an administrator or moderator, usage is as follows:"
                + "\n\tchangeprefix|[prefix]\n"
                + "The 'makemod' command is used to make a user a moderator. It can only be used by an administrator, this is how you use it:"
                + "\n\tmakemod|[user]" + "";

        //Split the message up. Commands and args are pipe ('|') delimited
        String[] messageStrings = event.getMessageContent().split("\\|");
        messageStrings[0] = messageStrings[0].toLowerCase();

        //Create some useful variables
        User me = event.getApi().getYourself();
        User author = event.getMessageAuthor().asUser().get();
        Server server = event.getServer().get();

        // Ping command
        if (messageStrings[0].equals(prefix + "ping")) {
            log("Running ping command");
            event.getChannel().sendMessage("Pong!");
        }

        // Help Command (Using mention)
        else if (messageStrings[0].equals(me.getMentionTag() + " help")) {
            log("Running help command with mention");
            event.getChannel().sendMessage(helpText);
        }

        // Help Command (Using prefix)
        else if (messageStrings[0].equals(prefix + "help")) {
            log("Running help command with prefix");
            event.getChannel().sendMessage(helpText);
        }

        // Flip a coin
        else if (messageStrings[0].equals(prefix + "flipacoin")) {
            log("Running flipacoin command");
            if (rng.nextInt(2) == 0) { //Gets a 1 or 0, 0 is heads, 1 is tails
                event.getChannel().sendMessage("Heads");
            } else {
                event.getChannel().sendMessage("Tails");
            }
        }

        // Meaning of life command (Fun command)
        else if (messageStrings[0].equals(prefix + "whatisthemeaningoflife")) {
            log("Running whatisthemeaningoflife command");
            event.getChannel().sendMessage("The meaning of life is...");
            event.getChannel().sendMessage("42");
            //Totally not a douglas adams reference
        }

        // PickRandom
        else if (messageStrings[0].equals(prefix + "pickrandom")) {
            log("Running pickrandom command");
            //Get the number of arguments, select a random one, send that back
            int selection = rng.nextInt(messageStrings.length - 1);
            selection++;
            event.getChannel().sendMessage("Selected: " + messageStrings[selection]);
        }

        // Maths command
        else if (messageStrings[0].equals(prefix + "domaths")) {
            log("Running domaths command");
            log("Expression = " + messageStrings[1]);
            try {
                // Command to execute python script
                String cmd = "python3 /home/rhodso/Documents/GitHub/SLAD_Project/Java/UtilitiesBot/maths.py "
                        + messageStrings[1];
                Process p = Runtime.getRuntime().exec(cmd);

                // Grab the return string
                String str = "";
                str = new String(p.getInputStream().readAllBytes());

                // Catch if there was no output
                if (str.equals(null) || str.equals("")) {
                    str = "Unable to parse expression. Did you type it correctly?";
                }

                // Send answer or error message
                log("Answer = " + str);
                event.getChannel().sendMessage(str);
            } catch (IOException ex) {
                event.getChannel().sendMessage("An IOException occured");
                System.out.println(ex.getCause());
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace().toString());
            }
        }

        else if (messageStrings[0].equals(prefix + "changeprefix")) {
            log("Running changeprefix command");

            //Some useful vars
            String newPrefix = "";
            List<Role> adminRoleArray = null;
            List<Role> modRoleArray = null;
            List<Role> authorRoles = null;

            Boolean isAdminOrMod = false;

            try {
                //Get the admin, mod, and the roles the message author has
                adminRoleArray = server.getRolesByName("Admin");
                modRoleArray = server.getRolesByName("Mod");
                authorRoles = author.getRoles(server);

                //Get the new prefix
                newPrefix = messageStrings[1];

                //Go through all the roles called mod, see if the author has it
                for (Role r : modRoleArray) {
                    if (authorRoles.contains(r)) {
                        isAdminOrMod = true;
                    }
                }

                //Go through all the roles called admin, see if the author has it
                for (Role r : adminRoleArray) {
                    if (authorRoles.contains(r)) {
                        isAdminOrMod = true;
                    }
                }

                if (isAdminOrMod) { //If they're an admin or mod, change thee prefix, and update 'prefix.txt'
                    setPrefix(newPrefix);
                    prefix = getPrefix();
                    event.getChannel().sendMessage("Prefix changed to '" + prefix + "'");
                    log("Prefix changed to " + prefix);
                } else { //Tell the user that thay don't have the permissions
                    event.getChannel().sendMessage("You need to be an Admin or a Mod to access this command");
                }
            } catch (Exception ex) { //In case it goes wrong
                event.getChannel().sendMessage("An exception occured! Prefix may not have been changed");
            }
        }

        //Change the bot's avatar
        else if (messageStrings[0].equals(prefix + "changeAvatar")) { 
            if (!event.getMessage().getAuthor().isBotOwner()) { //Tell the user they need to be the bot's owner
                event.getChannel().sendMessage("You must be the bot's owner to use this command");
                
            }
            else{ //Copy the owner's avatar
                event.getApi().updateAvatar(event.getMessage().getAuthor().getAvatar());
                event.getChannel().sendMessage("Updated avatar");
            }
        }

        //Makes user a mod
        else if (messageStrings[0].equals(prefix + "makemod")) {
            log("Running makemod command");
            boolean isAdmin = false;
            List<Role> adminRoleArray = null;
            List<Role> authorRoles = null;
            List<Role> modRoleArray = null;

            try { //Get the admin role, author roles, and the mod role
                adminRoleArray = server.getRolesByName("Admin");
                authorRoles = author.getRoles(server);
                modRoleArray = server.getRolesByName("Mod");

                //See if the user is an admin
                for (Role r : adminRoleArray) {
                    if (authorRoles.contains(r)) {
                        isAdmin = true;
                    }
                }

                //Make sure the author hasn't menioned everyone
                if (!event.getMessage().mentionsEveryone()) {
                    if (isAdmin) {
                        List<User> users = event.getMessage().getMentionedUsers();
                        for (User u : users) {
                            for (Role r : modRoleArray) {
                                u.addRole(r);
                            }
                        }
                        event.getChannel().sendMessage("Gave 'Mod' role to all users mentioned in message");
                    } else {
                        event.getChannel().sendMessage("You need to be an Admin to access this command");
                    }
                } else {
                    event.getChannel().sendMessage("Cannot make everyone a moderator!");
                }
            } catch (Exception ex) {

            }

        }

        else {
            //TODO: Spam detection
        }
    }

    String getPrefix() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("prefix.txt")));
            String t = br.readLine();
            br.close();
            return t;
        } catch (Exception ex) {

            return null;
        }
    }

    boolean setPrefix(String newPrefix) {
        BufferedWriter bw = null;
        try {
            File f = new File("prefix.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            try {
                FileWriter fw = new FileWriter(f);

                for (int i = 0; i < newPrefix.length(); i++) {
                    fw.write((int) newPrefix.charAt(i));
                }

                fw.close();
                return true;
            } catch (IOException e) {
                System.out.println(e.toString());
                return false;
            }

        } catch (Exception ex) {

            return false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void log(String msg) {
        String strDate = sdfDate.format(new Date());
        System.out.println(strDate + "\t" + msg);
    }
}