package rpjvanaert;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private static final Path BASE_DIR = Path.of("data");

    private static Path resolveToData(Path path) {
        Path resolved = BASE_DIR.resolve(path).normalize();
        try {
            Files.createDirectories(resolved.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directories", e);
        }
        return resolved;
    }

    public static <T> T load(Class<T> clazz, Path path) {
        Path fullPath = resolveToData(path);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fullPath.toFile()))) {
            Object obj = ois.readObject();
            return clazz.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static void save(Object object, Path path) {
        Path fullPath = resolveToData(path);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath.toFile()))) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save: " + e.getMessage());
        }
    }
}
