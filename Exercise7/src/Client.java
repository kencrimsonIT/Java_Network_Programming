import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Client {
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private BufferedReader userIn;

    public Client() throws UnknownHostException, IOException {
        this.socket = new Socket("localhost", 2000);
        this.netIn = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        this.netOut = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
        this.userIn = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws IOException {
        String line, syntax, sourceFile, destFile, response;
        StringTokenizer st;
        long fileLength;
        InputStream is;
        while (true) {
            line = this.userIn.readLine();
            if ("QUIT".equalsIgnoreCase(line)) {
                sendResponse(netOut, "QUIT");
                break;
            }
            st = new StringTokenizer(line);
            if (st.countTokens() != 3) {
                System.out.println("Invalid syntax!");
                continue;
            }

            syntax = st.nextToken();
            sourceFile = st.nextToken();
            destFile = st.nextToken();

            if (!"UPLOAD".equalsIgnoreCase(syntax)) {
                System.out.println("Error: Unknown command!");
                continue;
            }

            //Send syntax & dest file
            this.netOut.writeUTF(syntax);
            this.netOut.writeUTF(destFile);
            this.netOut.flush();

            //Read response
            response = this.netIn.readUTF();
            if (!"OK".equalsIgnoreCase(response)) {
                System.out.println("Error: " + response);
                continue;
            }

            File sFile = new File(sourceFile);
            if (!sFile.exists() || !sFile.isFile()) {
                System.out.println("Error: Unknown file!");
                continue;
            }

            fileLength = sFile.length();
            this.netOut.writeLong(fileLength);

            is = new BufferedInputStream(new FileInputStream(sFile));
            copyFile(is, netOut, fileLength);
            this.netOut.flush();
            System.out.println(this.netIn.readUTF());
        }
        try {
            this.socket.close();
            this.netIn.close();
            this.netOut.close();
            this.userIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(DataOutputStream dos, String response) throws IOException {
        dos.writeUTF(response);
        dos.flush();
    }

    private static boolean copyFile(InputStream is, DataOutputStream dos, long pSize) throws IOException {
        byte[] buffer = new byte[102400];
        long remain = pSize;
        int byteMustRead, byteRead;
        while (remain > 0) {
            byteMustRead = (int) (remain > buffer.length ? buffer.length : remain);
            byteRead = is.read(buffer, 0, byteMustRead);
            if (byteRead == -1) return false;
            dos.write(buffer, 0, byteRead);
            remain -= byteRead;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}
