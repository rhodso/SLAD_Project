import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javacord.org.javacord.api.DiscordApiBuilder;

import javacord.org.javacord.api.DiscordApi;


public class UtilitiesBot {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    DiscordApiBuilder apiBuilder = null;
    DiscordApi api = null;
    public UtilitiesBot(){
        super();

    }
    //Launcher calls constructor then start()
    void start(String token){
        log("Starting bot...");
        apiBuilder = new DiscordApiBuilder();
        apiBuilder.setToken(token);
        api = apiBuilder.login().join();
        
    }
    void log(String msg){
        String strDate = sdfDate.format(new Date());
        System.out.println(strDate + "\t" + msg);
    }

    String getToken(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(new File("token.txt")));
            String t = br.readLine();
            br.close();
            return t;
        }
        catch(Exception ex){
            log(ex.getCause() + "\n" + ex.getMessage());
            return null;
        }
    }
}