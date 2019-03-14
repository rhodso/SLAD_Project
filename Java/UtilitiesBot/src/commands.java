import java.io.IOException;
import java.util.Random;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class commands implements MessageCreateListener {
    private String prefix;
    private Random rng = new Random();

    public commands(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        String[] messageStrings = event.getMessageContent().split("\\|");
        messageStrings[0] = messageStrings[0].toLowerCase();

        User me = event.getApi().getYourself();

        // Ping command
        if (messageStrings[0].equals(prefix + "ping")) {
            event.getChannel().sendMessage("Pong!");
        }

        //Help Command (Using mention)
        else if(messageStrings[0].equals(me.getMentionTag() + " help")){
            event.getChannel().sendMessage("The prefix command is currently '" + prefix +
            "', you can use this to issue commands to me\n" + 
            "The 'ping' command makes me say 'Pong!', this is mainly used to make sure I am working fine\n" + 
            "The 'flipacoin' command is used to flip a coin, I will flip a coin and reply 'heads' or 'tails'\n" + 
            "The 'pickrandom' command is used to pick a random value from a list of values, usage is as follows: " + "\n\t!pickrandom|val1|val2|val3...\n " + 
            "The 'domaths' command is used to perform some basic maths operations, you can use it like this\n\t!domaths|[Expression]" + 
            ""
            );
        }

        //Help Command (Using prefix)
        else if(messageStrings[0].equals(prefix + "help")){
            event.getChannel().sendMessage("The prefix command is currently '" + prefix +
            "', you can use this to issue commands to me\n" + 
            "The 'ping' command makes me say 'Pong!', this is mainly used to make sure I am working fine\n" + 
            "The 'flipacoin' command is used to flip a coin, I will flip a coin and reply 'heads' or 'tails'\n" + 
            "The 'pickrandom' command is used to pick a random value from a list of values, usage is as follows: " + "\n\t!pickrandom|val1|val2|val3...\n " + 
            "The 'domaths' command is used to perform some basic maths operations, you can use it like this\n\t!domaths|[Expression]" + 
            ""
            );
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

        else if(messageStrings[0].equals(prefix + "changePrefix")){
            //TODO: Allow Admins / Mods to change the prefix. Could read from file? See https://docs.javacord.org/api/v/3.0.3/org/javacord/api/entity/user/User.html#getRoles-org.javacord.api.entity.server.Server-

        }

        else if(messageStrings[0].equals(prefix + "changeAvatar")){
            //TODO: Allow only the bot owner (rhodso) to change the avatar. See https://docs.javacord.org/api/v/3.0.3/org/javacord/api/entity/user/User.html#isBotOwner--
        }

        else{}
    }
}