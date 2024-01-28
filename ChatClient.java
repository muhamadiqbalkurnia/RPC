import java.rmi.Naming;
import java.util.Scanner;

public class ChatClient {
    private String username;
    private ChatServerInterface server;

    public ChatClient(String username) {
        this.username = username;
    }

    public void start() {
        try {
            // Contoh URL di dalam ChatClient.java
			server = (ChatServerInterface) Naming.lookup("rmi://localhost:2225/ChatServer");
            ChatClientInterface client = new ChatClientImpl(this.username, this);
            server.registerClient(client);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Ketik 'exit' untuk keluar.");
            while (true) {
                System.out.print("Ketik pesan: ");
                String message = scanner.nextLine();
                if (message.toLowerCase().equals("exit")) {
                    break;
                }
                server.sendMessage(client, message);
            }
            server.unregisterClient(client);
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.print("Masukkan nama pengguna: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        ChatClient chatClient = new ChatClient(username);
        chatClient.start();
    }
}
