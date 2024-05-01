import java.rmi.*;
import java.rmi.server.*;

public class ListClient{
    public static void main(String[] args) throws Exception{
        if (args.length != 1){
            System.out.println("Usage: java ListClient <server>");
            return;
        }

        //Verbindung: rmi://localhost:1099/Counter
        String[] list = Naming.list(args[0]);

        System.out.println("RMI registry on " + args[0] + " has the following names:");
        for (int i = 0; i < list.length; i++){
            System.out.println(list[i]);
        }
    }
}