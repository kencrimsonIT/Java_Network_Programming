import java.io.*;

public class Main {
    public static void pack(String folder, String packedFile) throws IOException {
        File sFolder = new File(folder);
        if (!sFolder.exists() || !sFolder.isDirectory()) return;

        File[] files = sFolder.listFiles();
        if (files == null) return;

        File pFile = new File(packedFile);
        File packedCanonical = pFile.getCanonicalFile();

        //Get file number
        int fileNumber = 0;
        for (File file : files) {
            if (file.isFile() && !file.equals(packedCanonical)) {
                fileNumber++;
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(sFolder, "rw")) {
            raf.setLength(0);
            raf.writeInt(fileNumber);
            //Placeholder
            for (int i = 0; i < fileNumber; i++) {
                raf.writeLong(0);
            }

            byte[] buffer = new byte[102400]; //initialize buffer with size=10MB
            int byteRead;
            long currentPos, pos;
            int index = 0;
            long endPos;

            for (File file : files) {
                if (!file.isFile() || file.equals(packedCanonical)) continue;
                pos = 4 + 8 * index; //Calculate the position
                currentPos = raf.getFilePointer(); //get current position before writing
                raf.writeUTF(file.getName()); //write file name
                raf.writeLong(file.length()); //write file length

                try (InputStream is = new BufferedInputStream(new FileInputStream(file))) { //try-catch resources
                    while ((byteRead = is.read(buffer)) != -1) {
                        raf.write(buffer);
                    }
                }

                endPos = raf.getFilePointer();

                raf.seek(pos);
                raf.writeLong(currentPos);
                raf.seek(endPos);
                index++;
            }
        }
    }

    public static void unpack(String packedFile, String extractFile, String destFile) throws IOException {
        File pFile = new File(packedFile);
        if (!pFile.exists() || !pFile.isFile()) return;

        try (RandomAccessFile raf = new RandomAccessFile(packedFile, "r")) {
            int fileNumber = raf.readInt();
            long[] fPos = new long[fileNumber];
            for (int i = 0; i < fileNumber; i++) {
                fPos[i] = raf.readLong();
            }

            for (int i = 0; i < fileNumber; i++) {
                raf.seek(fPos[i]);
                String fileName = raf.readUTF();
                if (fileName.equalsIgnoreCase(extractFile)) {
                    //Extract file
                    long fileLength = raf.readLong();
                    File dest = new File(destFile);

                    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(dest))) {
                        copyFile(raf, os, fileLength);
                    }
                    return; //Stop extracting
                }
            }
        }
    }

    private static boolean copyFile(RandomAccessFile raf, OutputStream os, long pSize) throws IOException {
        byte[] buffer = new byte[102400];
        long remain = pSize;
        int byteMustRead, byteRead;
        while (remain > 0) {
            byteMustRead = (int) (remain > buffer.length ? buffer.length : remain);
            byteRead = raf.read(buffer, 0, byteMustRead);
            if (byteRead == -1) return false;
            os.write(buffer, 0, byteRead);
            remain -= byteRead;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        String folder = "";
        String packedFile = "";
        String extractFile = "";
        String destFile = "";

        /*pack(folder, packedFile);*/
        unpack(packedFile, extractFile, destFile);
    }
}