

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

@SuppressWarnings("serial")
public class MasterRechnerImpl extends UnicastRemoteObject implements MasterRechner
{
    private ArrayList<WorkerRechner> allClients;
    public ArrayList<int[]> images;
    public int workerCount;

    public MasterRechnerImpl() throws RemoteException
    {
        allClients = new ArrayList<WorkerRechner>();
        images = new ArrayList<int[]>();
    }

    public synchronized boolean addWorker(WorkerRechner objRef)throws RemoteException
    {
        allClients.add(objRef);
        objRef.setIdx(allClients.size()-1);
        System.out.println("Worker added");
        images.add(new int[100]);
        workerCount++;
        return true;
    }

    public synchronized void removeWorker(WorkerRechner objRef)throws RemoteException
    {
        boolean start = false;
        for(Iterator<WorkerRechner> iter = allClients.iterator(); iter.hasNext();)
        {
            WorkerRechner w = iter.next();
            try
            {
                if(w == objRef)
                {
                    start = true;
                }
                if(start)
                {
                    w.setIdx(w.getIdx()-1);
                    images.set(w.getIdx(), images.get(w.getIdx()+1));
                }
            }
            catch(RemoteException exc)
            {
                iter.remove();
            }
        }
        allClients.remove(objRef);
        images.remove(objRef.getIdx());
        workerCount--;
    }

    public synchronized int[] work(int start, int end, int idx)throws RemoteException
    {
        WorkerRechner w = allClients.get(idx);
        int[] x = w.work(start, end);
        images.set(idx, x);
        return x;
    }
}