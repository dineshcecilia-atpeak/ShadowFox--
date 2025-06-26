import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String room;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            username = in.readLine();
            room = in.readLine();

            if (username == null || room == null) return;

            System.out.println("👤 " + username + " joined room: " + room);
            ChatServer.addToRoom(room, this);
            ChatServer.broadcast(room, "🔔 " + username + " has joined the room!", null);

            String input;
            while ((input = in.readLine()) != null) {
                String formatted = username + ": " + input;
                ChatServer.broadcast(room, formatted, null); // Send to everyone
            }
        } catch (IOException e) {
            System.out.println("Connection lost: " + username);
        } finally {
            ChatServer.removeClient(room, this);
            ChatServer.broadcast(room, "[Error] " + username + " left the chat.", null);
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
