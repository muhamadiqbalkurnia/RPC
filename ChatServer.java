import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

// Interface untuk klien
interface ChatClientInterface extends java.rmi.Remote {
    void receiveMessage(String message) throws RemoteException;
    String getUsername() throws RemoteException;
}

// Interface untuk server
interface ChatServerInterface extends java.rmi.Remote {
    void registerClient(ChatClientInterface client) throws RemoteException;
    void unregisterClient(ChatClientInterface client) throws RemoteException;
    void sendMessage(ChatClientInterface sender, String message) throws RemoteException;
    void broadcastMessage(ChatClientInterface sender, String message) throws RemoteException;
}

// Implementasi kelas ChatServer
public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private List<ChatClientInterface> clients;

    public ChatServer() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void registerClient(ChatClientInterface client) throws RemoteException {
        try {
            clients.add(client);
            System.out.println(client.getUsername() + " terhubung.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void unregisterClient(ChatClientInterface client) throws RemoteException {
        try {
            clients.remove(client);
            System.out.println(client.getUsername() + " terputus.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void sendMessage(ChatClientInterface sender, String message) throws RemoteException {
        try {
            for (ChatClientInterface client : clients) {
                if (!client.equals(sender)) {
                    client.receiveMessage(sender.getUsername() + ": " + message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void broadcastMessage(ChatClientInterface sender, String message) throws RemoteException {
        try {
            for (ChatClientInterface client : clients) {
                if (!client.equals(sender)) {
                    client.receiveMessage("Broadcast dari " + sender.getUsername() + ": " + message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer chatServer = new ChatServer();
            java.rmi.registry.LocateRegistry.createRegistry(2225);
            Naming.rebind("rmi://localhost:2225/ChatServer", chatServer);
            System.out.println("Server Ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
