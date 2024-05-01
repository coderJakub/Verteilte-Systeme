import java.rmi.*;
import java.rmi.server.*;

public class CounterImpl extends UnicastRemoteObject implements Counter {
    private int count;
    public CounterImpl() throws RemoteException {
        super();
        count = 0;
    }

    public synchronized int inc() throws RemoteException {
        System.out.println("Counter gets incremented.");
        return ++count;
    }

    public synchronized int dec() throws RemoteException {
        System.out.println("Counter gets decremented.");
        return --count;
    }

    public synchronized int reset() throws RemoteException {
        System.out.println("Counter gets reset.");
        count = 0;
        return count;
    }
}