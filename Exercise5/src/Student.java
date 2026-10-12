import java.io.IOException;
import java.io.RandomAccessFile;

public class Student {
    private int id;
    private String name;
    private int bYear;
    private double grade;

    public Student() {
    }

    public Student(int id, String name, int bYear, double grade) {
        this.id = id;
        this.name = name;
        this.bYear = bYear;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bYear=" + bYear +
                ", grade=" + grade +
                '}';
    }

    public void writeRAFStu(RandomAccessFile raf, int nameLength) throws IOException {
        raf.writeInt(this.id);
        writeName(raf, nameLength);
        raf.writeInt(this.bYear);
        raf.writeDouble(this.grade);
    }

    private void writeName(RandomAccessFile raf, int nameLength) throws IOException {
        char c;
        for (int i = 0; i < nameLength; i++) {
            c = i < this.name.length() ? this.name.charAt(i) : 0;
            raf.writeChar(c);
        }
    }

    public void readRAFStu(RandomAccessFile raf, int nameLength) throws IOException {
        this.id = raf.readInt();
        this.name = readName(raf, nameLength);
        this.bYear = raf.readInt();
        this.grade = raf.readDouble();
    }

    private String readName(RandomAccessFile raf, int nameLength) throws IOException {
        String name = "";
        char c;
        for (int i = 0; i < nameLength; i++) {
            c = raf.readChar();
            if (c != 0) name += c;
        }

        return name;
    }
}
