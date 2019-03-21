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

    // Vars
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    DiscordApiBuilder apiBuilder = null;
    DiscordApi api = null;

    // Constructor
    public UtilitiesBot() {
        super();
    }

    // Launcher calls constructor then start()
    void start(String token) {
        log("Starting bot...");

        // See if we've actually got a token and not an error
        if (token.equals("TOKEN NOT FOUND")) {
            log("The token file was not found. Please insert the token in the file 'token.txt'");
        }
        // Start the bot
        else if (!token.equals(null)) {
            // Create new api with the builder, set the token, and login
            apiBuilder = new DiscordApiBuilder();
            apiBuilder.setToken(token);
            api = apiBuilder.login().join();

            // Sort out the prefix
            // If we can read it from a file, then great. If not, then default to '!'
            String prefix = getPrefix();
            if (prefix == null || prefix.equals(null) || prefix.equals("")) {
                log("Prefix not found. Defaulting to use '!'");
                prefix = "!";
            }

            // Add the commands
            api.addMessageCreateListener(new commands(prefix));
        }
        // Some sort of error happened, don't start the bot
        else {
            log("Token not found!");
        }
        log("Bot ready!");
    }

    // Method to log what's going on (Print it to console with timestamp)
    void log(String msg) {
        String strDate = sdfDate.format(new Date());
        System.out.println(strDate + "\t" + msg);
    }

    // Getter for the api
    DiscordApi getApi() {
        return api;
    }

    // Gets the token from the file
    String getToken() {
        try {
            // Find the file
            File f = new File("token.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));

            // Check if it exists
            if (!f.exists()) {

                // If it doesn't, then create a file and tell the user that they should put the
                // token here
                // For obvious reasons, this file is NOT synced with github
                f.createNewFile();
                br.close();
                return "TOKEN NOT FOUND";
            }

            // Read the token, close the reader and return the token
            String t = br.readLine();
            br.close();
            return t;
        } catch (Exception ex) { // Print out exception
            log(ex.getCause() + "\n" + ex.getMessage());
            return null;
        }
    }

    // Get the prefix from the file
    String getPrefix() {
        String t = null;
        try {
            // Read the file, close the reader and return
            BufferedReader br = new BufferedReader(new FileReader(new File("prefix.txt")));
            t = br.readLine();
            br.close();
            return t;
        } catch (Exception ex) { // Print out exception
            log(ex.getCause() + "\n" + ex.getMessage());
            return null;
        }
    }

    // Set the prefix. Returns true/false depending on if the operation was
    // successful
    boolean setPrefix(String newPrefix) {
        BufferedWriter bw = null;
        try {
            // Try and find the file, if it doesn't exist then just create, if it does,
            // delete first
            File f = new File("prefix.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            // Write the new prefix to the file
            try {
                FileWriter fw = new FileWriter(f);

                for (int i = 0; i < newPrefix.length(); i++) {
                    fw.write((int) newPrefix.charAt(i));
                }

                fw.close();
                return true;
            } catch (IOException e) { // Print out exception
                System.out.println(e.toString());
                return false;
            }

        } catch (Exception ex) { // Print out exception
            log(ex.getCause() + "\n" + ex.getMessage());
            return false;
        } finally { // Close the buffered writer
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) { // If there was an exception closing the buffered writer
                    e.printStackTrace();
                }
            }
        }
    }
}