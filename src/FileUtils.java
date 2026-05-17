import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    // دالة توليد ID (اللي عندك أصلاً)
    public static String genId(String prefix) {
        return prefix + System.currentTimeMillis() % 100000;
    }

    // دالة قراءة كل أسطر الملف (لو الملف مش موجود بتعمله وترجع ليست فاضية)
    public static List<String> readAll(String filename) {
        try {
            Path path = Paths.get(filename);
            if (!Files.exists(path)) {
                Files.createFile(path);  // لو الملف مش موجود نعمله
                return new ArrayList<>();
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // دالة كتابة ليست من الأسطر في الملف (بتمسح القديم وتكتب الجديد)
    public static void writeAll(String filename, List<String> lines) {
        try {
            Files.write(Paths.get(filename), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // دالة إضافة سطر واحد في آخر الملف (اختيارية، لو لسه بتستخدم append في الـ add)
    public static void append(String filename, String line) {
        try {
            Files.write(Paths.get(filename),
                    Arrays.asList(line),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // دالة اختيارية: التأكد من وجود كل الملفات من البداية
    public static void ensureFilesExist(String... filenames) {
        for (String file : filenames) {
            try {
                Path path = Paths.get(file);
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}