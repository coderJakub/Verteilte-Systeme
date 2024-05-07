

import java.rmi.*;
import java.io.*;

public class ChatClientMainSimple
{
    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Nötige Kommandozeilenargumente: "
                               + "<Name des Servers> <eigener Name>");
            return;
        }
        try
        {
            ChatServer server = (ChatServer) Naming.lookup("rmi://" + args[0]
                                                           + "/ChatServer");
            System.out.println("Kontakt zu Server hergestellt");
            ChatClientImplSimple client = new ChatClientImplSimple(args[1]);
            if(server.addClient(client))
            {
                System.out.println("Ende durch Einagbe 'Ende' " 
                                   + "oder 'ende'");
                sendInputToServer(server, args[1]);
                server.removeClient(client);
            }
            else
            {
                System.out.println("Es ist schon jemand unter "
                                   + "diesem Namen angemeldet");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }

    private static void sendInputToServer(ChatServer server, String name)
    {
        try
        {
            BufferedReader input = new BufferedReader(
                                       new InputStreamReader(System.in));
            String line;
            while((line = input.readLine()) != null)
            {
                if(line.equals("ende") || line.equals("Ende"))
                {
                    break;
                }
                server.sendMessage(name, line);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}