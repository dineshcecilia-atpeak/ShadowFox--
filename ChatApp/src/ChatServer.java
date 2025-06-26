import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Map<String, List<ClientHandler>> chatRooms = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Chat Server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }

    public static synchronized void addToRoom(String room, ClientHandler client) {
        chatRooms.computeIfAbsent(room, r -> new ArrayList<>()).add(client);
    }

    public static synchronized void broadcast(String room, String message, ClientHandler sender) {
        List<ClientHandler> clients = chatRooms.get(room);
        if (clients != null) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    public static synchronized void removeClient(String room, ClientHandler client) {
        List<ClientHandler> clients = chatRooms.get(room);
        if (clients != null) {
            clients.remove(client);
            if (clients.isEmpty()) {
                chatRooms.remove(room);
            }
        }
    }
}
