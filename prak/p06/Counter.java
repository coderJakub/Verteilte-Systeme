import java.rmi.*;

public interface Counter extends Remote {
    public int inc() throws RemoteException;
    public int dec() throws RemoteException;
    public int reset() throws RemoteException;
}