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
    // Vars
    private static String prefix;
    private Random rng = new Random();
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private boolean silenced = false;

    User me;
    User author;
    Server server;
    MessageCreateEvent event;
    String[] messageStrings;

    // Help text for help commands
    static String helpText = "The prefix for commands is currently '" + getPrefix()
            + "', you can use this to issue commands to me\n"
            + "\nThe 'help' command shows this text. You can also mention me, like thiss '@UtilitiesBot help'"
            + "\nThe 'ping' command makes me say 'Pong!', this is mainly used to make sure I am working fine"
            + "\nThe 'flipacoin' command is used to flip a coin, I will flip a coin and reply 'heads' or 'tails'"
            + "\nThe 'pickrandom' command is used to pick a random value from a list of values, usage is as follows: "
            + "\n\tpickrandom|[val1]|[val2]|[val3]..."
            + "\nThe 'domaths' command is used to perform some basic maths operations, you can use it like this\n\tdomaths|[Expression]"
            + "\nThe 'changeprefix' command is used to change the prefix. It can only be used by an administrator or moderator, usage is as follows:"
            + "\n\tchangeprefix|[prefix]"
            + "\nThe 'makemod' command is used to make a user a moderator. It can only be used by an administrator, this is how you use it:"
            + "\n\tmakemod|[user]"
            + "\nThe 'silence' command is used to make me silenced. This will stop me responding to all commands, except silence and unsilence. It can only be used by an admin"
            + "\nThe 'unsilence' command is used to make me unsilenced. This will allow me to respond to all commands, except silence and unsilence. It can only be used by an admin"
            + "";

    // Constructor
    public commands(String newPrefix) {
        prefix = newPrefix;
    }

    // The big boi method. Gets called every time a message is sent
    // We need to read the message and see if it's a command, and act occordingly

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        // Get the prefix and instantiate the event
        prefix = getPrefix();
        this.event = event;

        // Split the message up. Commands and args are pipe ('|') delimited
        messageStrings = event.getMessageContent().split("\\|");
        messageStrings[0] = messageStrings[0].toLowerCase();
        messageStrings[0] = messageStrings[0].replace(" ", "");

        // Instatntiate some useful variables
        me = event.getApi().getYourself();
        author = event.getMessageAuthor().asUser().get();
        server = event.getServer().get();

        /*
         * Command structure: if(messageStrings[0].equals(prefix + "theCommandName"){
         * log("Running theCommandName command") theCommandNameCommand(); }
         * 
         * Where the command name is the name of the command, such as ping
         * theCommandNameCommand(); should be a private void method, that performs the
         * command
         */

        // Silence the bot command
        if (messageStrings[0].equals(prefix + "silence")) {
            log("Bot silenced");
            silenceCommand();
        }

        // Unsilence the bot command
        else if (messageStrings[0].equals(prefix + "unsilence")) {
            log("Bot unsilenced");
            unsilenceCommand();
        }

        // Help Command (Using mention)
        else if (messageStrings[0].equals(me.getMentionTag() + " help")) {
            log("Running help command with mention");
            helpCommand();
        }

        // Help Command (Using prefix)
        else if (messageStrings[0].equals(prefix + "help")) {
            log("Running help command with prefix");
            helpCommand();
        }

        // If the bot is silenced, don't do anything, if it isn't, process commands
        if (!silenced) {

            // Ping command
            if (messageStrings[0].equals(prefix + "ping")) {
                log("Running ping command");
                pingCommand();
            }

            // Flip a coin
            else if (messageStrings[0].equals(prefix + "flipacoin")) {
                log("Running flipacoin command");
                flipACoinCommand();
            }

            // government screw up command
            else if (messageStrings[0].equals(prefix + "dayssincelastgovernmentscrewup")) {
                log("Running government screwup command");
                govtScrewUpCommand();
            }

            // Google command
            else if (messageStrings[0].equals(prefix + "google")) {
                log("Running google command");
                googleCommand();
            }

            // Meaning of life command (Fun command)
            else if (messageStrings[0].equals(prefix + "whatisthemeaningoflife")) {
                log("Running whatisthemeaningoflife command");
                meaningOfLifeCommand();
            }

            // PickRandom
            else if (messageStrings[0].equals(prefix + "pickrandom")) {
                log("Running pickrandom command");
                pickRandomCommand();
            }

            // Maths command
            else if (messageStrings[0].equals(prefix + "domaths")) {
                log("Running domaths command");
                log("Expression = " + messageStrings[1]);
                doMathsCommand();
            }

            // Change prefix command
            else if (messageStrings[0].equals(prefix + "changeprefix")) {
                log("Running changeprefix command");
                changePrefixCommand();
            }

            // Makes user a mod
            else if (messageStrings[0].equals(prefix + "makemod")) {
                log("Running makemod command");
                makeModCommand();

            } else {

            }
        }
    }

    // Action for help command
    private void helpCommand() {
        event.getChannel().sendMessage(helpText);
    }

    // Action for silence command
    private void silenceCommand() {
        boolean isAdmin = false;
        List<Role> adminRoleArray = null;
        List<Role> authorRoles = null;

        try {
            adminRoleArray = server.getRolesByName("Admin");
            authorRoles = author.getRoles(server);

            // See if the user is an admin
            for (Role r : adminRoleArray) {
                if (authorRoles.contains(r)) {
                    isAdmin = true;
                }
            }

            if (isAdmin) {
                silenced = true;
                event.getChannel().sendMessage("Bot silenced");
            } else {
                event.getChannel().sendMessage("You must be an admin to use this command");
            }

        } catch (Exception ex) {

        }
    }

    // Action for unsilence command
    private void unsilenceCommand() {
        boolean isAdmin = false;
        List<Role> adminRoleArray = null;
        List<Role> authorRoles = null;

        try {
            adminRoleArray = server.getRolesByName("Admin");
            authorRoles = author.getRoles(server);

            // See if the user is an admin
            for (Role r : adminRoleArray) {
                if (authorRoles.contains(r)) {
                    isAdmin = true;
                }
            }

            if (isAdmin) {
                silenced = false;
                event.getChannel().sendMessage("Bot unsilenced");
            } else {
                event.getChannel().sendMessage("You must be an admin to use this command");
            }

        } catch (Exception ex) {

        }
    }

    // Action for ping command
    private void pingCommand() {
        event.getChannel().sendMessage("Pong!");
    }

    // Action for flipACoin command
    private void flipACoinCommand() {
        log("Running flipacoin command");
        int flip = rng.nextInt(101);
        if (flip < 50) { // 0-49 = Heads
            event.getChannel().sendMessage("Heads");
        } else if (flip > 50) { // 51-100 = Tails
            event.getChannel().sendMessage("Tails");
        } else { // 50 = Edge
            event.getChannel().sendMessage("You're not going to believe this...\nIt's landed on it's edge");
        }
    }

    // Action for govtScrewUp command
    private void govtScrewUpCommand() {
        event.getChannel().sendMessage("Probably 0");
    }

    // Action for meaningOfLife command
    private void meaningOfLifeCommand() {
        event.getChannel().sendMessage("The meaning of life is...");
        event.getChannel().sendMessage("42");
        // Totally not a douglas adams reference
    }

    // Action for google command
    private void googleCommand() {
        // Replace spaces with '+' because that's how google does spaces
        messageStrings[1] = messageStrings[1].replace(" ", "+");
        log("Query = " + messageStrings[1]);
        event.getChannel().sendMessage("http://www.google.com/search?q=" + messageStrings[1]);
    }

    // Action for pickRandom command
    private void pickRandomCommand() {
        // Get the number of arguments, select a random one, send that back
        int selection = rng.nextInt(messageStrings.length - 1);
        selection++;
        event.getChannel().sendMessage("Selected: " + messageStrings[selection]);
    }

    // Action for doMaths command
    private void doMathsCommand() {
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

    // Action for changePrefix command
    private void changePrefixCommand() {

        // Some useful vars
        String newPrefix = "";
        List<Role> adminRoleArray = null;
        List<Role> modRoleArray = null;
        List<Role> authorRoles = null;

        Boolean isAdminOrMod = false;

        try {
            // Get the admin, mod, and the roles the message author has
            adminRoleArray = server.getRolesByName("Admin");
            modRoleArray = server.getRolesByName("Mod");
            authorRoles = author.getRoles(server);

            // Get the new prefix
            newPrefix = messageStrings[1];

            // Go through all the roles called mod, see if the author has it
            for (Role r : modRoleArray) {
                if (authorRoles.contains(r)) {
                    isAdminOrMod = true;
                }
            }

            // Go through all the roles called admin, see if the author has it
            for (Role r : adminRoleArray) {
                if (authorRoles.contains(r)) {
                    isAdminOrMod = true;
                }
            }

            if (isAdminOrMod) { // If they're an admin or mod, change thee prefix, and update 'prefix.txt'
                setPrefix(newPrefix);
                prefix = getPrefix();
                event.getChannel().sendMessage("Prefix changed to '" + prefix + "'");
                log("Prefix changed to " + prefix);
            } else { // Tell the user that thay don't have the permissions
                event.getChannel().sendMessage("You need to be an Admin or a Mod to access this command");
            }
        } catch (Exception ex) { // In case it goes wrong
            event.getChannel().sendMessage("An exception occured! Prefix may not have been changed");
        }
    }

    // Action for makeMod command
    private void makeModCommand() {
        boolean isAdmin = false;
        List<Role> adminRoleArray = null;
        List<Role> authorRoles = null;
        List<Role> modRoleArray = null;

        try { // Get the admin role, author roles, and the mod role
            adminRoleArray = server.getRolesByName("Admin");
            authorRoles = author.getRoles(server);
            modRoleArray = server.getRolesByName("Mod");

            // See if the user is an admin
            for (Role r : adminRoleArray) {
                if (authorRoles.contains(r)) {
                    isAdmin = true;
                }
            }

            // Make sure the author hasn't menioned everyone
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

    // Get the prefix from the file
    static String getPrefix() {
        try {
            // Create a buffered reader to read the file
            BufferedReader br = new BufferedReader(new FileReader(new File("prefix.txt")));

            // Read the line, then close the reader
            String t = br.readLine();
            br.close();
            return t;
        } catch (Exception ex) {
            return null;
        }
    }

    // Set the prefix in the file
    static boolean setPrefix(String newPrefix) {
        BufferedWriter bw = null;
        try {
            // See if the file exitis. If it does, then delete it
            File f = new File("prefix.txt");
            if (f.exists()) {
                f.delete();
            }

            // Create the file
            f.createNewFile();

            // Write the prefix to the file
            try {
                FileWriter fw = new FileWriter(f);

                for (int i = 0; i < newPrefix.length(); i++) {
                    fw.write((int) newPrefix.charAt(i));
                }

                // Close the file reader, and return true
                fw.close();
                return true;
            } catch (IOException e) {
                System.out.println(e.toString());
                return false;
            }

            // Catch the exception
        } catch (Exception ex) {

            return false;
        } finally {
            // Try and close the buffered writer
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Log something
    void log(String msg) {
        String strDate = sdfDate.format(new Date());
        System.out.println(strDate + "\t" + msg);
    }
}