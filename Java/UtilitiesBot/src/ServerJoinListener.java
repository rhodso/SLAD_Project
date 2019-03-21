import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

public class ServerJoinListener implements ServerMemberJoinListener {

    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event){

        /*
        User newUser = event.getUser();
        newUser.openPrivateChannel();
        newUser.getPrivateChannel().get().sendMessage("Hi there, it seems you've joined" + event.getServer().getName());
        newUser.getPrivateChannel().get().sendMessage(commands.helpText);
        */

    }
    
}