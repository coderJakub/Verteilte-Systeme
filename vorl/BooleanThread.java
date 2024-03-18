import java.lang.Thread;
import java.util.Random;

public class BooleanThread implements Runnable{
    static boolean boolArr[] = new boolean[5_000_000];
    int start;
    int stop;
    int sum;

    public void run(){
        for(int i=start; i<stop; i++){
            if(boolArr[i]){
                sum++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        int nThreads = args.length>0?Integer.parseInt(args[0]):1;
        for(int i=0; i<5_000_000; i++){
            Random rand = new Random();
            boolArr[i] = rand.nextBoolean();
        }
        long startTime = System.currentTimeMillis();

        BooleanThread trArr[] = new BooleanThread[nThreads];
        Thread thArr[] = new Thread[nThreads];
        int chunkSize = 5_000_000/trArr.length;
        for(int i=0; i<trArr.length; i++){
            trArr[i] = new BooleanThread();
            thArr[i] = new Thread(trArr[i]);
            trArr[i].start = i*chunkSize;
            trArr[i].stop = (i+1)*chunkSize;
            thArr[i].start();
        }
        for(int i=0; i<trArr.length; i++)thArr[i].join();
        
        long endTime = System.currentTimeMillis();
        int sum = 0;

        for(int i=0; i<trArr.length; i++)sum+=trArr[i].sum;

        System.out.println("Sum: "+sum);
        System.out.println("Time taken: "+(endTime-startTime));
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}