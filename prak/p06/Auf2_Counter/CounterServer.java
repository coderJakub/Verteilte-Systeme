import java.rmi.*;
import java.rmi.registry.*;

public class CounterServer{
    public static void main(String[] args) throws Exception{
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("Counter", new CounterImpl());        
        System.out.println("CounterServer ready.");

        //if Strg+C is pressed, the server will be unbound
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                System.out.println("CounterServer shutting down.");
                try{
                    reg.unbind("Counter");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
} 