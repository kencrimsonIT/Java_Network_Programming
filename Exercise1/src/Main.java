import java.io.File;

public class Main {
    public static boolean delete(String path) {
        File file = new File(path);

        if (!file.exists()) return false;
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (File child : childFiles) {
                    delete(child.getAbsolutePath());
                }
            }
        }

        return file.delete();

        /*if (path == null) return false;
        File file = new File(path);
        if (!file.exists()) return false;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delete(f.getAbsolutePath());
            }
        }

        return file.delete();*/

        // => Return NullPointerExceptionError when the path deny accessing permissions
    }

    public static void findAllByExts(String path, String... exts) {
        /*File dir = new File(path);
        File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                findAllByExts(f.getAbsolutePath(), exts);
            } else if (f.isFile()) {
                if (f.getName().toLowerCase().endsWith("." + ext.toLowerCase())) {
                    System.out.println(f.getAbsolutePath());
                }
            }
        }*/ // Wrong answer



        if (path == null) return;
        File file = new File(path);
        if (!file.exists()) return;

        if (file.isFile()) {
            for (String ext : exts) {
                if (file.getName().endsWith(ext)) {
                    System.out.println(file.getAbsolutePath());
                }
            }
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (File child : childFiles) {
                    findAllByExts(child.getAbsolutePath(), exts);
                }
            }
        }
    }

    public static void main(String[] args) {
        String path = "D:\\Documents\\Nhập môn Cơ sở dữ liệu";
        System.out.println();

        /*boolean result = delete(path);
        System.out.println(result);*/

        String etx = "pdf";
        findAllByExts(path, etx);
    }
}
