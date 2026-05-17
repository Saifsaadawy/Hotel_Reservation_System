import java.nio.file.*;
import java.util.*;


public class FileManager {
public static List<String> read(String file) {
try {
Path p = Paths.get(file);
if (!Files.exists(p)) Files.createFile(p);
return Files.readAllLines(p);
} catch (Exception e) {
return new ArrayList<>();
}
}


public static void write(String file, List<String> lines) {
try {
Files.write(Paths.get(file), lines);
} catch (Exception e) {}
}
}