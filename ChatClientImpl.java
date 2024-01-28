import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClientInterface {
    private String username;
    private ChatClient client;

    public ChatClientImpl(String username, ChatClient client) throws RemoteException {
        super();
        this.username = username;
        this.client = client;
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }
}
