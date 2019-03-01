import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

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

        // Ping command
        if (messageStrings[0].equals(prefix + "ping")) {
            event.getChannel().sendMessage("Pong!");
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
        else{}
    }
}