import java.lang.Thread;

public class TestThread implements Runnable {
    int arr[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int num;

    public TestThread(int n){
        num = n;
    }

    public void run() {
        System.out.println("Hello from a thread!");
        int j = 0;
        for (int i:arr){
            synchronized(this){
                System.out.println("Thread "+num+ ": "+ i);
                j++;
            }
        }
    }

    public static void main(String args[]) throws InterruptedException{
        TestThread t1 = new TestThread(1);
        TestThread t2 = new TestThread(2);
        Thread tr1 = new Thread(t1);
        Thread tr2 = new Thread(t2);
        tr1.start();
        tr2.start();
        tr1.join();
        System.out.println("Main thread");
    }
}