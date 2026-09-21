import java.io.*;
import java.util.Locale;

public class Main {
    /*private static final int MAX_PARTS = 999;
    private static final int BUFFER_SIZE = 64 * 1024;*/

    public static void split(String source, long pSize) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(source));
            OutputStream os;
            String dest;
            int count = 1;
            while (true) {
                dest = source + createExt(count);
                os = new FileOutputStream(dest);
                boolean hasMoreData = copyFile(is, os, pSize);
                if (!hasMoreData) break;
                os.close();
                count++;
            }
            is.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean copyFile(InputStream is, OutputStream os, long pSize) throws IOException {
        byte[] buffer = new byte[102400];
        long remain = pSize;
        int byteMustRead, byteRead;
        while (remain > 0) {
            byteMustRead = (int) (remain > buffer.length ? buffer.length : remain);
            byteRead = is.read(buffer, 0, byteMustRead);
            if (byteRead == -1) return false;
            os.write(buffer, 0, byteRead);
            remain -= byteRead;
        }
        return true;
    }

    private static String createExt(int count) {
        if (count < 10) return ".00" + count;
        if (count < 100) return ".0" + count;
        return "." + count;
    }

    public static void join(String source) {
        File file = new File(source);
        if (file.exists()) {
            System.out.println("File " + file.getName() + " already exists");
            return;
        }

        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            InputStream is;
            String partFileName;
            int count = 1;
            while (true) {
                partFileName = source + createExt(count);
                File partFile = new File(partFileName);
                if(!partFile.exists()) break;
                is = new BufferedInputStream(new FileInputStream(partFile));
                copyFile(is, os, (int) partFile.length());
                is.close();
                count++;
            }
            os.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        String source = "D:\\Network Programming\\Download.mp4";
        int pSize = 47 * 1024;

//        split(source, pSize);
        join(source);

        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("Performance time: " + time + "ms");
    }
}