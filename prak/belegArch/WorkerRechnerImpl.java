import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class WorkerRechnerImpl extends UnicastRemoteObject
                                  implements WorkerRechner
{
    private int idx;

    public WorkerRechnerImpl() throws RemoteException
    {
        idx = -1;
    }

    public int setIdx(int idx) throws RemoteException
    {
        this.idx = idx;
        return idx;
    }

    public int getIdx() throws RemoteException
    {
        return idx;
    }

    public int[] work(int start, int end) throws RemoteException
    {
        int[] x = new int[100];
        for(int i=0; i<100; i++)
        {
            if(i>=start && i<=end)
            {
                x[i] = 1;
            }
            else
            {
                x[i] = 0;
            }
        }
        return x;
    }
}
