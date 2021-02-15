package tudbut.squarey;

import tudbut.squarey.client.Client;

public class Main {
    
    public static void main(String[] args) {
        if(args.length == 1)
            new Client(args[0], 23432);
        else
            new Client(null, 23432);
    }
}
