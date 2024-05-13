
import java.rmi.*;

public interface MasterRechner extends Remote
{
    public boolean addWorker(WorkerRechner objRef) throws RemoteException;
    public void removeWorker(WorkerRechner objRef) throws RemoteException;

    public int[] work(int start, int end, int idx) throws RemoteException;
}