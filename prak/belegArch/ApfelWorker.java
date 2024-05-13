import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;

public class ApfelWorker{
    public static void main(String[] args) throws Exception{
        MasterRechnerImpl server = new MasterRechnerImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("ApfelModel", server);
        System.out.println("ApfelModel started.");

        BufferedReader input = new BufferedReader(
                                       new InputStreamReader(System.in));
        String line;
        while((line = input.readLine()) != null)
        {
            if(line.equals("start"))
            {
                break;
            }
        }
        ImageMaker[] threads = new ImageMaker[server.workerCount];

        for(int i=0; i<server.workerCount; i++)
        {
            threads[i] = new ImageMaker(i+1,i+10,i, server);
            threads[i].start();
        }
        for(int i=0; i<server.workerCount; i++)
        {
            threads[i].join();
        }
        System.out.println("All images created.");

        for(int i=0; i<server.workerCount; i++)
        {
            System.out.println("Image "+i+":");
            for(int j=0; j<100; j++)
            {
                System.out.print(server.images.get(i)[j]+",");
            }
            System.out.println();
        }


        Runtime.getRuntime().addShutdownHook(new Thread(){
        public void run(){
            System.out.println("ApfelModel shutting down.");
            try{
            Registry registry = LocateRegistry.getRegistry();
            registry.unbind("ApfelModel");
            }
            catch(Exception exc)
            {
            exc.printStackTrace();
            }
        }
        });
    }
}

class ImageMaker extends Thread{
        private int start;
        private int end;
        private int idx;
        private MasterRechnerImpl server;

        public ImageMaker(int start, int end, int idx, MasterRechnerImpl server){
            this.start = start;
            this.end = end;
            this.idx = idx;
            this.server = server;
        }

        public void run(){
            try{
                int[] x = server.work(start, end, idx);
            }
            catch(RemoteException exc){
                exc.printStackTrace();
            }
        }
    }