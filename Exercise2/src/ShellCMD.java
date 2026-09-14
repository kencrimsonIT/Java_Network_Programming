import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class ShellCMD {
    /*private Path currentDir;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private PrintStream out = System.out;

    public ShellCMD(String initialPath) {
        Path path = Paths.get(initialPath).toAbsolutePath().normalize();
        if (!path.toFile().exists() || !path.toFile().isDirectory()) {
            out.println("Initial path is invalid: " + path);
            out.println("Using current directory...");
            path = Paths.get(".").toAbsolutePath().normalize();
        }
        this.currentDir = path;
    }

    public void run() {
        while (true) {
            out.print("\n" + currentDir + "> ");
            out.flush();

            String commandLine = "";

            try {
                commandLine = reader.readLine();
            } catch (IOException e) {
                out.println("Error: " + e.getMessage());
            }

            commandLine = commandLine.trim();
            if (commandLine.isEmpty()) continue;

            String[] parts = commandLine.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String arg = parts.length > 1 ? parts[1] : "";

            try {
                switch (command) {
                    case "cd":
                        changeDirectory(arg);
                        break;
                    case "dir":
                        listDirectory();
                        break;
                    case "delete":
                        deleteFile(arg);
                        break;
                    case "exit":
                        closeQuietly();
                        return;
                    default:
                        out.println("Unknown command: " + command);
                }
            } catch (IOException e) {
                out.println("Error: " + e.getMessage());
            }
        }
        
        closeQuietly();
    }

    private void changeDirectory(String arg) {
        if (arg.isEmpty()) out.println("This directory is empty");

        Path newPath;
        if (arg.equals("..")) {
            newPath = currentDir.getParent();
            if (newPath == null) {
                out.println("No need to return previous directory");
                return;
            }
        } else {
            Path p = Paths.get(arg);
            newPath = p.isAbsolute() ? p.normalize() : currentDir.resolve(arg).normalize();
        }

        File dir = newPath.toFile();
        if (!dir.exists()) {
            out.println("Directory does not exist: " + dir);
        } else if (!dir.isDirectory()) {
            out.println("Directory is not a directory: " + dir);
        } else {
            currentDir = newPath;
        }
    }

    private void listDirectory() {
        File dir = currentDir.toFile();
        File[] files = dir.listFiles();

        if (files == null) {
            out.println("Does not have permission to executing");
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int dirCount = 0;
        int fileCount = 0;
        long totalSize = 0;

        for (File file : files) {
            String date = sdf.format(file.lastModified());
            if (file.isDirectory()) {
                out.printf("%-17s %-6s %12s  %s%n", date, "<DIR>", "", file.getName());
                dirCount++;
            } else {
                out.printf("%-17s %-6s %12s  %s%n", date, "", formatSize(file.length()), file.getName());
                fileCount++;
                totalSize += file.length();
            }
        }
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        exp = Math.min(exp, 6);
        char unitChar = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), unitChar);
    }

    private void deleteFile(String arg) {
        Path target = currentDir.resolve(arg).normalize();
        File f = target.toFile();

        if (!f.exists()) {
            out.println("File does not exist: " + f);
            return;
        }

        String type = f.isDirectory() ? "dir" : "file";
        out.println("Are you sure you want to delete " + type + " \"" + f.getName() + "\"? (y/n): ");
        out.flush();

        String answer = reader.readLine();
        answer = (answer == null) ? ""
    }

    private void closeQuietly() {
        try {
            reader.close();
        } catch (IOException e) {

        }
    }*/




    private File defaultDir;
    private BufferedReader userInput;
    private PrintStream userOutput;

    public ShellCMD(String defaultDir) {
        this.defaultDir = new File(defaultDir);
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userOutput = System.out;
    }

    public void run() throws IOException {
        String line, syntax, param, response;
        StringTokenizer st;

        while (true) {
            line = "";
            syntax = "";
            param = "";
            response = "";
            getDefaultDir();
            line = this.userInput.readLine();
            if (line.isBlank()) continue;
            st = new StringTokenizer(line);
            syntax = st.nextToken();
            if (st.hasMoreTokens()) param = line.substring(syntax.length()).trim();
            switch(syntax.toUpperCase()) {
                case "EXIT":
                    return;
                case "CD":
                    response = changeDirectory(param);
                    break;
                case "DIR":
                    response = listDirectory();
                    break;
                case "DEL":
                    response = del(param);
                    break;
                default:
                    response = "\'" + syntax + "\'" + " is not a valid command";
                    break;
            }
            this.userOutput.println(response);
        }
    }

    private String del(String param) {
        File delFile = new File(this.defaultDir, param);
        if (!delFile.exists()) return "Could not find " + this.defaultDir.getAbsolutePath() + "\\" + param;
        boolean result = deleteFunc(delFile);
        return result ? "Successfully deleted " + param : "Could not delete " + param;
    }

    private boolean deleteFunc(File delFile) {
        File[] files = delFile.listFiles();
        if (files != null) for (File file : files) {
            if (!deleteFunc(file)) return false;
        }
        return delFile.delete();
    }

    private String listDirectory() {
        File[] files = this.defaultDir.listFiles();
        StringBuilder fileNames = new StringBuilder();
        StringBuilder folderNames = new StringBuilder();
        if (files != null) for (File file : files) {
            if (file.isDirectory()) folderNames.append(file.getName().toUpperCase()).append("\n");
            if (file.isFile()) fileNames.append(file.getName().toLowerCase()).append("\n");
        }
        return fileNames.toString();
    }

    private String changeDirectory(String param) throws IOException {
        if (param.isBlank()) return this.defaultDir.getAbsolutePath() + "\n";

        File cdFile = new File(param);
        if (!cdFile.isAbsolute()) cdFile = new File(this.defaultDir, param);
        cdFile = cdFile.getCanonicalFile();
        if (cdFile.exists() && cdFile.isDirectory()) {
            this.defaultDir = cdFile;
            return "";
        }

        return "Cannot find the path.";
    }

    public void getDefaultDir() {
        this.userOutput.print(this.defaultDir.getAbsolutePath() + ">");
    }

    static void main(String[] args) throws IOException {
        /*String initialDir = args.length > 0 ? args[0] : System.getProperty("user.dir");
        ShellCMD shellCMD = new ShellCMD(initialDir);
        shellCMD.run();*/

        String defaultDir = System.getProperty("user.dir");
        ShellCMD cmd = new ShellCMD(defaultDir);
        cmd.run();
    }
}
