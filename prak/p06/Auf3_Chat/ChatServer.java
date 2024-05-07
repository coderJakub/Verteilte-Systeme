

import java.rmi.*;

public interface ChatServer extends Remote
{
    public boolean addClient(ChatClient objRef) throws RemoteException;
    public void removeClient(ChatClient objRef) throws RemoteException;
    public void sendMessage(String name, String msg) throws RemoteException;
}