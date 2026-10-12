import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static final int NAME_LENGTH = 50;

    public static void addStudent(String path, Student st) throws IOException {
        if (st == null) return;
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            int stuNumber = 0;
            if (raf.length() > 0) stuNumber = raf.readInt();
            int recordSize = 4 + NAME_LENGTH * 2 + 4 + 8;
            raf.seek(0);
            raf.writeInt(stuNumber + 1); //header
            raf.writeInt(recordSize);
            //Write Data
            raf.seek(8 + (long) recordSize * stuNumber);
            st.writeRAFStu(raf, NAME_LENGTH);
        }
    }

    public Student getStudent(String path, int index) throws IOException {
        if (index < 0) return null;
        File file = new File(path);
        if (!file.exists()) return null;
        try (RandomAccessFile raf = new RandomAccessFile(path, "r")) {
            //Read header
            int stuNumber = raf.readInt();
            int recordSize = raf.readInt();
            //Read records
            int recordPos = 8 + recordSize * index;
            if (index < stuNumber) {
                raf.seek(recordPos);
                Student st = new Student();
                st.readRAFStu(raf, NAME_LENGTH);
            }
        }

        return null;
    }

    public static void updateStudent(String path, int index, Student newSt) throws IOException {
        if (index < 0) return;
        File file = new File(path);
        if (!file.exists()) return;
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            //Read header
            int stuNumber = raf.readInt();
            int recordSize = raf.readInt();
            //Read records
            int recordPos = 8 + recordSize * index;
            if (index < stuNumber) {
                raf.seek(recordPos);
                newSt.readRAFStu(raf, NAME_LENGTH);
            }
        }
    }

    public Student findById(String path, int id) throws IOException {
        File file = new File(path);
        if (!file.exists()) return null;
        try (RandomAccessFile raf = new RandomAccessFile(path, "r")) {
            //Read header
            int stuNumber = raf.readInt();
            int recordSize = raf.readInt();
            for (int i = 0; i < stuNumber; i++) {
                int recordPos = 8 + recordSize * i;
                raf.seek(recordPos);
                int sid = raf.readInt();
                if (sid == id) {
                    Student st = new Student();
                    st.readRAFStu(raf, NAME_LENGTH);
                    return st;
                }
            }
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        Student s1 = new Student();
        Student s2 = new Student();
        Student s3 = new Student();
        Student s4 = new Student();
        Student s5 = new Student();

        Main m = new Main();
        addStudent("D:\\Network Programming\\st_list.txt", s1);
    }
}