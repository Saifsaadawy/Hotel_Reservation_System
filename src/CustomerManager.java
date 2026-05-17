
import java.util.*;

public class CustomerManager {
    private static final String FILE = "customers.txt";

    // تحميل كل العملاء
public static List<Customer> load() {
    List<Customer> customers = new ArrayList<>();
    List<String> lines = FileUtils.readAll(FILE);

    for (String line : lines) {
        if (!line.isBlank()) {
            customers.add(Customer.fromCSV(line));
        }
    }

    return customers;
}

// حفظ كل العملاء
public static void saveAll(List<Customer> list) {
    List<String> lines = new ArrayList<>();

    for (Customer customer : list) {
        lines.add(customer.toCSV());
    }

    FileUtils.writeAll(FILE, lines);
}


    // إضافة عميل جديد (أفضل من append عشان الأمان والاستقرار)
    public static void add(Customer c) {
        List<Customer> list = load();
        list.add(c);
        saveAll(list);
    }

    // ←←←← الدالة الجديدة: حذف عميل حسب الـ ID
    public static void delete(String customerId) {
        List<Customer> list = load();
        
        // نمسح كل عميل ID بتاعه يطابق اللي جاي
        list.removeIf(c -> c.getId().equals(customerId));

        saveAll(list);
    }
}