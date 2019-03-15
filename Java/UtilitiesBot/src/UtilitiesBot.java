import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class UtilitiesBot {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    DiscordApiBuilder apiBuilder = null;
    DiscordApi api = null;

    public UtilitiesBot() {
        super();
    }

    // Launcher calls constructor then start()
    void start(String token) {
        log("Starting bot...");
        if (!token.equals(null)) {
            apiBuilder = new DiscordApiBuilder();
            apiBuilder.setToken(token);
            api = apiBuilder.login().join();
            String prefix = getPrefix();
            if (prefix == null || prefix.equals(null) || prefix.equals("")) {
                log("Prefix not found. Defaulting to use '!'");
                prefix = "!";
            }
            api.addMessageCreateListener(new commands(prefix));
        } else {
            log("Token not found!");
        }
        log("Bot ready!");
    }

    void log(String msg) {
        String strDate = sdfDate.format(new Date());
        System.out.println(strDate + "\t" + msg);
    }

    DiscordApi getApi() {
        return api;
    }

    String getToken() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("token.txt")));
            String t = br.readLine();
            br.close();
            return t;
        } catch (Exception ex) {
            log(ex.getCause() + "\n" + ex.getMessage());
            return null;
        }
    }

    String getPrefix() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("prefix.txt")));
            String t = br.readLine();
            br.close();
            return t;
        } catch (Exception ex) {
            log(ex.getCause() + "\n" + ex.getMessage());
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
            log(ex.getCause() + "\n" + ex.getMessage());
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