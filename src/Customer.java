public class Customer {
    public String id;
    public String name;
    public String phone;

    public Customer(String id, String name, String phone) {
        this.id = id;
        this.name = name != null ? name : "";
        this.phone = phone != null ? phone : "";
    }

    // ============ Getters ============
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    // ============ Setters (اختياري، بس كويس لو عايز تعدل بعدين) ============
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone : "";
    }

    // ============ toCSV ============
    // تحويل الكائن إلى سطر CSV للتخزين في customers.txt
    public String toCSV() {
        return id + "," + name + "," + phone;
    }

    // ============ fromCSV ============
    // تحميل كائن Customer من سطر في الملف (لازم تكون static)
    public static Customer fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);  // -1 عشان يحافظ على الحقول الفاضية

        String id = parts[0].trim();
        String name = parts.length > 1 ? parts[1].trim() : "";
        String phone = parts.length > 2 ? parts[2].trim() : "";

        return new Customer(id, name, phone);
    }

    // ============ toString (مهم جدًا للـ ComboBox في الـ UI) ============
    @Override
    public String toString() {
        return id + " - " + name + (phone.isEmpty() ? "" : " (" + phone + ")");
    }
}