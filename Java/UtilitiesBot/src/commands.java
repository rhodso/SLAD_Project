import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.entity.permission.*;
import org.javacord.api.entity.server.Server;

public class commands implements MessageCreateListener {
    private String prefix;
    private Random rng = new Random();

    public commands(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        
        String helpText = "The prefix for commands is currently '" + prefix + "', you can use this to issue commands to me\n" +
        "The 'help' command shows this text. You can also mention me, like such '@UtilitiesBot help'\n" + 
        "The 'ping' command makes me say 'Pong!', this is mainly used to make sure I am working fine\n" + 
        "The 'flipacoin' command is used to flip a coin, I will flip a coin and reply 'heads' or 'tails'\n" + 
        "The 'pickrandom' command is used to pick a random value from a list of values, usage is as follows: " + "\n\tpickrandom|[val1]|[val2]|[val3]...\n " + 
        "The 'domaths' command is used to perform some basic maths operations, you can use it like this\n\tdomaths|[Expression]" + 
        "The 'changeprefix' command is used to change the prefix. It can only be used by an administrator or moderator, usage is as follows:"+ "\n\tchangeprefix|[prefix]\n" +
        "The 'makemod' command is used to make a user a moderator. It can only be used by an administrator, this is how you use it:" + "\n\tmakemod|[user]" + 
        "";

        String[] messageStrings = event.getMessageContent().split("\\|");
        messageStrings[0] = messageStrings[0].toLowerCase();

        User me = event.getApi().getYourself();
        User author = event.getMessageAuthor().asUser().get();
        Server server = event.getServer().get();

        // Ping command
        if (messageStrings[0].equals(prefix + "ping")) {
            event.getChannel().sendMessage("Pong!");
        }

        //Help Command (Using mention)
        else if(messageStrings[0].equals(me.getMentionTag() + " help")){
            event.getChannel().sendMessage(helpText);
        }

        //Help Command (Using prefix)
        else if(messageStrings[0].equals(prefix + "help")){
            event.getChannel().sendMessage(helpText);
        }

        // Flip a coin
        else if (messageStrings[0].equals(prefix + "flipacoin")) {
            if (rng.nextInt(2) == 0) {
                event.getChannel().sendMessage("Heads");
            } else {
                event.getChannel().sendMessage("Tails");
            }
        }

        // Meaning of life command
        else if (messageStrings[0].equals(prefix + "whatisthemeaningoflife")) {
            event.getChannel().sendMessage("The meaning of life is...");
            event.getChannel().sendMessage("42");
        }

        // PickRandom
        else if (messageStrings[0].equals(prefix + "pickrandom")) {
            int selection = rng.nextInt(messageStrings.length - 1);
            selection++;
            event.getChannel().sendMessage("Selected: " + messageStrings[selection]);
        }

        // Maths command
        else if (messageStrings[0].equals(prefix + "domaths")) {
            try {
                //Command to execute python script
                String cmd = "python3 /home/rhodso/Documents/GitHub/SLAD_Project/Java/UtilitiesBot/maths.py " + messageStrings[1];
                Process p = Runtime.getRuntime().exec(cmd);
                
                //Grab the return string
                String str = "";
                str = new String(p.getInputStream().readAllBytes());

                //Catch if there was no output
                if(str.equals(null) || str.equals("")){
                    str = "Unable to parse expression. Did you type it correctly?";
                }

                //Send answer or error message
                event.getChannel().sendMessage(str);
            }
            catch(IOException ex){
                event.getChannel().sendMessage("An IOException occured");
                System.out.println(ex.getCause());
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace().toString());
            }
        }

        else if(messageStrings[0].equals(prefix + "changeprefix")){
            String newPrefix = "";
            List<Role> adminRoleArray = null;
            List<Role> modRoleArray = null;
            List<Role> authorRoles = null;

            Boolean isAdminOrMod = false;

            try{
                adminRoleArray = server.getRolesByName("Admin");
                modRoleArray = server.getRolesByName("Mod");
                authorRoles = author.getRoles(server);

                newPrefix = messageStrings[1];

                for(Role r : modRoleArray){
                    if(authorRoles.contains(r)){
                        isAdminOrMod = true;
                    }
                }

                for(Role r : adminRoleArray){
                    if(authorRoles.contains(r)){
                        isAdminOrMod = true;
                    }
                }

                if(isAdminOrMod){
                    setPrefix(newPrefix);
                    prefix = getPrefix();
                    event.getChannel().sendMessage("Prefix changed to '" + prefix + "'");
                }
                else{
                    event.getChannel().sendMessage("You need to be an Admin or a Mod to access this command");
                }
            }
            catch(Exception ex){
                event.getChannel().sendMessage("An exception occured! Prefix may not have been changed");
            }
        }

        else if(messageStrings[0].equals(prefix + "changeAvatar")){
            //TODO: Allow only the bot owner (rhodso) to change the avatar. See https://docs.javacord.org/api/v/3.0.3/org/javacord/api/entity/user/User.html#isBotOwner--
        }

        else if(messageStrings[0].equals(prefix + "makemod")){
            boolean isAdmin = false;
            List<Role> adminRoleArray = null;
            List<Role> authorRoles = null;
            List<Role> modRoleArray = null;
            
            try{
                adminRoleArray = server.getRolesByName("Admin");
                authorRoles = author.getRoles(server);
                modRoleArray = server.getRolesByName("Mod");

                for(Role r : adminRoleArray){
                    if(authorRoles.contains(r)){
                        isAdmin = true;
                    }
                }

                if(!event.getMessage().mentionsEveryone()){
                if(isAdmin){
                    List<User> users = event.getMessage().getMentionedUsers();
                    for(User u : users){
                        for(Role r : modRoleArray){
                            u.addRole(r);
                        }
                    }
                    event.getChannel().sendMessage("Gave 'Mod' role to all users mentioned in message");
                }
                else{
                    event.getChannel().sendMessage("You need to be an Admin to access this command");
                }
            }
            else{
                event.getChannel().sendMessage("Cannot make everyone a moderator!");
            }
            }
            catch(Exception ex){

            }

        }

        else{}
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
                
                for(int i = 0; i < newPrefix.length(); i++)
                {
                    fw.write((int)newPrefix.charAt(i));
                }
                
                fw.close();
                return true;
            }
            catch (IOException e) {
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
}