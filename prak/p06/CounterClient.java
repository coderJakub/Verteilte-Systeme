import java.rmi.*;
import java.rmi.registry.*;

public class CounterClient{
    public static void main(String[] args) throws Exception{
        if (args.length != 1){
            System.out.println("Usage: java CounterClient <server>");
            return;
        }

        Counter counter = (Counter) Naming.lookup("rmi://" + args[0] + "/Counter");
        System.out.println("Counter is " + counter.inc());
        System.out.println("Counter is " + counter.inc());
        System.out.println("Counter is " + counter.dec());
        System.out.println("Counter is " + counter.reset());
    }
}