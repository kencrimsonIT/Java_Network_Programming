import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 2000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Waiting for connection...");
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerProcess(socket).start();
                System.out.println("Connection established!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
