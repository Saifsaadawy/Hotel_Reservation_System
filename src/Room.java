import java.util.*;

public class Room {
    public String number;
    public String type;
    public double price;
    public boolean busy;
    public List<String> services = new ArrayList<>();  // عشان لو نسيت تمرر services ميبقاش null

    public Room(String number, String type, double price, boolean busy, List<String> services) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.busy = busy;
        this.services = services != null ? services : new ArrayList<>();
    }

    // تحويل الكائن إلى سطر CSV للتخزين
    public String toCSV() {
        String servicesCsv = services.isEmpty() ? "" : String.join(";", services);
        return number + "," + type + "," + price + "," + (busy ? "1" : "0") + "," + servicesCsv;
    }

    // ←←←← أهم دالة: تحميل الكائن من سطر CSV (لازم تكون static)
    public static Room fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;  // أو رمي exception، حسب رغبتك
        }

        String[] parts = line.split(",", -1);  // -1 مهم عشان يحافظ على الحقول الفاضية

        String number = parts[0].trim();
        String type = parts[1].trim();
        double price = Double.parseDouble(parts[2].trim());
        boolean busy = parts[3].trim().equals("1");

        List<String> services = new ArrayList<>();
        if (parts.length > 4 && !parts[4].trim().isEmpty()) {
            services.addAll(Arrays.asList(parts[4].trim().split(";")));
        }

        return new Room(number, type, price, busy, services);
    }
}