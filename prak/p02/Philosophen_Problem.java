import java.util.*;
import java.lang.Thread;

class Table{
    boolean[] forksTaken;
    String[] names;

    Table(String n[]){
        names = n;
        forksTaken = new boolean[n.length];
        for(int i=0; i<n.length; i++)
            forksTaken[i] = false;
    }
    public synchronized void takeFork(int num){
        while(forksTaken[num] || forksTaken[num+1==forksTaken.length?0:num+1]){
            try{
                wait();
            }catch(InterruptedException e){}
        }
        forksTaken[num] = forksTaken[num+1==forksTaken.length?0:num+1] = true;
    }
    public synchronized void putFork(int num){
        forksTaken[num] = forksTaken[num+1==forksTaken.length?0:num+1] = false;
        notifyAll();
    }
}
class Philosopher extends Thread{
    Table t;
    String name;
    int num;

    Philosopher(Table table, String name, int num){
        this.t = table;
        this.name = name;
        this.num = num;
        start();
    }

    public void run(){
        while(true){
            think();
            t.takeFork(num);
            eat();
            t.putFork(num);
        }
    }
    public void think(){
        int waitFor = (int)(Math.random()*20000);
        System.out.println(name+"("+num+") denkt für "+waitFor/1000f);
        try{
            sleep(waitFor);
        }catch(InterruptedException e){}
    }
    public void eat(){
        int waitFor = (int)(Math.random()*20000);
        System.out.println(name+"("+num+") isst für "+waitFor/1000f);
        try{
            sleep(waitFor);
        }catch(InterruptedException e){}
    }
}

public class Philosophen_Problem{
    public static void main(String[] args){
        String[] names = {"Sokrates", "Aristoteles", "Immanuel", "Friedrich", "Jean-Paul"};
        Table t = new Table(names);
        for(int i=0; i<5; i++)new Philosopher(t, names[i], i);
    }
}