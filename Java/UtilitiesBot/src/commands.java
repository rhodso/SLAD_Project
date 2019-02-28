import java.util.Random;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.python.util.PythonInterpreter;

public class commands implements MessageCreateListener {
    private String prefix;
    private Random rng = new Random();
    PythonInterpreter interpreter = new PythonInterpreter();

    public commands(String prefix){
        this.prefix = prefix;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        String[] messageStrings = event.getMessageContent().split("\\|");
        messageStrings[0] = messageStrings[0].toLowerCase();

        //Ping command
        if(messageStrings[0].equals(prefix + "ping")){
            event.getChannel().sendMessage("Pong!");
        }

        //Flip a coin
        else if(messageStrings[0].equals(prefix + "flipacoin")){
            if(rng.nextInt(2) == 0){
                event.getChannel().sendMessage("Heads");
            }
            else{
                event.getChannel().sendMessage("Tails");
            }
        }

        //Meaning of life command
        else if(messageStrings[0].equals(prefix + "whatisthemeaningoflife")){
            event.getChannel().sendMessage("The meaning of life is...");
            event.getChannel().sendMessage("42");
        }

        //PickRandom
        else if(messageStrings[0].equals(prefix + "pickrandom")){
            int selection = rng.nextInt(messageStrings.length -1);
            selection++;
            event.getChannel().sendMessage("Selected: " + messageStrings[selection]);
        }

        //Maths command
        else if(messageStrings[0].equals(prefix + "domaths")){

        }
        else{}
    }
}