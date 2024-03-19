import java.util.*;
import java.lang.Thread;

class BooleanThread implements Runnable{
    int start;
    int end;
    int res;
    static boolean boolArr[];

    public BooleanThread(int s, int e){
        start=s;
        end=e;
    }

    public void run(){
        for(int i=start; i<end; i++)if(boolArr[i])res++;
    }

    public static void main(String[] args) throws InterruptedException{
        int lenArr = 50_000_000;
        int sum=0;
        int nAnz = (args.length>0)?Integer.parseInt(args[0]):1;
        int chunkSize = lenArr/nAnz;
        boolArr = new boolean[lenArr];
        BooleanThread bt[] = new BooleanThread[nAnz];
        Thread t[] = new Thread[nAnz];

        for(int i=0; i<lenArr;i++){
            Random rand = new Random();
            boolArr[i] = rand.nextBoolean();
        } 

        long startTime = System.currentTimeMillis();
        for(int i=0; i<nAnz; i++){
            bt[i] = new BooleanThread(i*chunkSize, i==nAnz-1?lenArr:(i+1)*chunkSize);
            t[i] = new Thread(bt[i]);
            t[i].start();
        }

        for(int i=0; i<nAnz; i++)t[i].join();
        long endTime = System.currentTimeMillis();

        for(int i=0; i<nAnz; i++)sum+=bt[i].res;
        float timeTaken = (endTime - startTime) / 1000.0f;
        System.out.println("Rechenzeit: " + timeTaken);
        System.out.println("Ergebnis: " + sum);
    }
}