import java.io.File;

public class Main {
    public static boolean delete(String path) {
        File file = new File(path);

        if (!file.exists()) return false;
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (File child : childFiles) {
                    if (!delete(child.getAbsolutePath())) {
                        return false;
                    }
                }
            }
        }

        return file.delete();
    }

    public static void findAllByExts(String path, String ext) {
        File dir = new File(path);
        File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        for (File f : files) {
            if (f.isDirectory()) {
                findAllByExts(f.getAbsolutePath(), ext);
            } else if (f.isFile()) {
                if (f.getName().toLowerCase().endsWith("." + ext.toLowerCase())) {
                    System.out.println(f.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) {
        String path = "D:\\Documents\\Nhập môn Cơ sở dữ liệu";

        /*boolean result = delete(path);
        System.out.println(result);*/

        /*String etx = "pdf";
        findAllByExts(path, etx);*/
    }
}
