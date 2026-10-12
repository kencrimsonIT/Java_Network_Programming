import java.io.*;
import java.net.Socket;

public class ServerProcess extends Thread {
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        this.netOut = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
    }

    public void run() {
        String syntax, destFile;
        while (true) {
            try {
                syntax = this.netIn.readUTF();
                if ("QUIT".equalsIgnoreCase(syntax)) {
                    break;
                }
                if (!"UPLOAD".equalsIgnoreCase(syntax)) {
                    sendResponse(netOut, "Error: Unknow command!");
                    continue;
                }
                destFile = this.netIn.readUTF();
                OutputStream out;
                try {
                    out = new BufferedOutputStream(new FileOutputStream(destFile));
                } catch (Exception e) {
                    sendResponse(netOut, e.getMessage());
                    continue;
                }
                sendResponse(netOut, "OK");
                long fileLength = this.netIn.readLong();
                copyFile(netIn, out, fileLength);
                sendResponse(netOut, "Invalid command!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.socket.close();
            this.netIn.close();
            this.netOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(DataOutputStream dos, String response) throws IOException {
        dos.writeUTF(response);
        dos.flush();
    }

    private static boolean copyFile(DataInputStream dis, OutputStream os, long pSize) throws IOException {
        byte[] buffer = new byte[102400];
        long remain = pSize;
        int byteMustRead, byteRead;
        while (remain > 0) {
            byteMustRead = (int) (remain > buffer.length ? buffer.length : remain);
            byteRead = dis.read(buffer, 0, byteMustRead);
            if (byteRead == -1) return false;
            os.write(buffer, 0, byteRead);
            remain -= byteRead;
        }
        return true;
    }
}
