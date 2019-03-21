public class launcher {
    public static void main(String[] args) {

        // Create new instance of the bot, and get the token
        UtilitiesBot ub = new UtilitiesBot();
        String token = ub.getToken();

        // If the token is not found, then terminate
        if (token.equals(null)) {
            System.out.println("\n   Token not found");
            System.out.println("\n\n***Application terminated***");
        } else {
            // Token seems fine, start the bot
            ub.start(token);
        }
    }
}