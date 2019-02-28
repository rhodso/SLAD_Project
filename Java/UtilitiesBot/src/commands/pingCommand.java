package commands;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class pingCommand implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        
        if(event.getMessageContent().equals("!ping")){
            event.getChannel().sendMessage("Pong!");
        }
        
        return;
    }

}