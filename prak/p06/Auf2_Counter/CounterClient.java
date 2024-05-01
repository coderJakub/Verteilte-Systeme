import java.rmi.*;
import java.rmi.registry.*;

public class CounterClient{
    public static void main(String[] args) throws Exception{
        if (args.length != 1){
            System.out.println("Usage: java CounterClient <server>");
            return;
        }

        Counter counter = (Counter) Naming.lookup("rmi://" + args[0] + "/Counter");
        String eingabe = "";
        int anz = 0;

        while (!eingabe.equals("exit")){
            eingabe = System.console().readLine("Enter 'inc', 'dec', 'reset', print or 'exit': ");
            if (eingabe.equals("exit"))continue;
            else if(eingabe.equals("print")){
                System.out.println("Counter is " + counter.get());
                continue;
            }
            else if(eingabe.equals("reset")){
                counter.reset();
                continue;
            }

            try{
                anz = Integer.parseInt(System.console().readLine("Enter number of times: "));
            }catch (NumberFormatException e){
                System.out.println("Invalid input.");
                continue;
            }
            
            boolean invalid = false;
            for (int i = 0; i < anz && !invalid; i++){
                switch (eingabe){
                    case "inc":
                        counter.inc();
                        break;
                    case "dec":
                        counter.dec();
                        break;
                    default:
                        System.out.println("Invalid input.");
                        invalid = true;
                }
            }
        }
    }
}