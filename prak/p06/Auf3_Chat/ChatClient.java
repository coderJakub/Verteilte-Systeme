

public interface ChatClient extends java.rmi.Remote
{
    public String getName() throws java.rmi.RemoteException;
    public void print(String msg) throws java.rmi.RemoteException;
}