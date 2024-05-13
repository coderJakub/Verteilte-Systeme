

import java.rmi.*;
import java.io.*;

public class Worker
{
    public static void main(String[] args)
    {
        try
        {
            MasterRechner server = (MasterRechner) Naming.lookup("rmi://localhost:1099/ApfelModel");
            System.out.println("Kontakt zu Server hergestellt");

            WorkerRechnerImpl client = new WorkerRechnerImpl();
            System.out.println("Worker angemeldet");
            if(server.addWorker(client))
            {
                System.out.println("Erfolgreich angemeldet");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);

        System.out.println("Worker beendet");
    }
}