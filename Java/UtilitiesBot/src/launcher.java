public class launcher{
    public static void main(String[] args){
        UtilitiesBot ub = new UtilitiesBot();
        String token = ub.getToken();
        if(token.equals(null)){
            System.out.println("\n\n***Application terminated***");
        }
        else{
            ub.start(token);
        }
    }
}