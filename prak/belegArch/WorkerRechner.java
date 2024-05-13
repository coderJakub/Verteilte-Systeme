import java.rmi.*;

public interface WorkerRechner extends Remote
{
    public int setIdx(int idx) throws RemoteException;
    public int getIdx() throws RemoteException;
    public int[] work(int start, int end) throws RemoteException;
}