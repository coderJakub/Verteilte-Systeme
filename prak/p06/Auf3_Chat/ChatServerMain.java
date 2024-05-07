

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServerMain{
  public static void main(String[] args) throws Exception{
  ChatServerImpl server = new ChatServerImpl();
  Registry registry = LocateRegistry.createRegistry(1099);
  registry.rebind("ChatServer", server);
  System.out.println("ChatServer started.");

  //if Strg+C is pressed, the server will be unbound
  Runtime.getRuntime().addShutdownHook(new Thread(){
    public void run(){
      System.out.println("ChatServer shutting down.");
      try{
        Registry registry = LocateRegistry.getRegistry();
        registry.unbind("ChatServer");
      }
      catch(Exception exc)
      {
        exc.printStackTrace();
      }
    }
  });
  }
}
