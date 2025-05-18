import java.io.*;
import java.nio.file.*;

public class FileHandler {

    public static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }
}
