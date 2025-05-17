import java.io.*;
import java.nio.file.*;

public class FileHandler {

    // Read file and return content as byte array
    public static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    // Write byte array to file
    public static void writeFile(File file, byte[] data) throws IOException {
        Files.write(file.toPath(), data);
    }
}
