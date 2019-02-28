import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class commands implements MessageCreateListener {
    private String prefix;

    public commands(String prefix){
        this.prefix = prefix;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        //Ping command
        if(event.getMessageContent().equals(prefix + "ping")){
            event.getChannel().sendMessage("Pong!");
        }
        else{}
    }
}