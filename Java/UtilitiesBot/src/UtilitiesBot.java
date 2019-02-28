import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


public class UtilitiesBot {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    DiscordApi api = null;
    public UtilitiesBot(){
        super();
        
    }
    //Launcher calls constructor then start()
    void start(String token){
        log("Starting bot...");
        api = new DiscordApiBuilder().setToken(token).login.join();
        
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