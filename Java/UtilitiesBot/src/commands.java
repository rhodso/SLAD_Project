import java.io.IOException;
import java.io.InputStream;
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
                Process p = Runtime.getRuntime().exec("python3 maths.py " + messageStrings[1]);
                InputStream result = p.getInputStream();
                byte[] b = result.readAllBytes();
                String str = new String(b, "UTF-8");
                
                System.out.println(str);

                event.getChannel().sendMessage(str);
            }
            catch(IOException ex){
                event.getChannel().sendMessage("An IOException occured");
            }
        }
        else{}
    }
}