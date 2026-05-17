public class ServiceItem {
    public String id;
    public String name;
    public String description;
    public double price;

    // الـ constructor الرئيسي
    public ServiceItem(String id, String name, String description, double price) {
        this.id = id != null ? id : "";
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.price = price >= 0 ? price : 0.0;
    }

    // constructor بسيط (لو الوصف فاضي)
    public ServiceItem(String id, String name, double price) {
        this(id, name, "", price);
    }

    // Getters
    public String getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public String getDescription() { 
        return description; 
    }

    public double getPrice() { 
        return price; 
    }

    // toCSV للحفظ في الملف
    public String toCSV() {
        return id + "," + name + "," + description + "," + price;
    }

    // fromCSV للتحميل من الملف (مع حماية من crash)
    public static ServiceItem fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",", -1);  // -1 عشان يحافظ على الحقول الفاضية

        String id = parts.length > 0 ? parts[0].trim() : "";
        String name = parts.length > 1 ? parts[1].trim() : "";
        String description = parts.length > 2 ? parts[2].trim() : "";
        double price = 0.0;

        if (parts.length > 3) {
            String priceStr = parts[3].trim();
            if (!priceStr.isEmpty()) {
                try {
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    price = 0.0;  // لو السعر غلط (زي "drying")، يحط 0 بدل crash
                    System.err.println("Invalid price in services.txt: " + priceStr + " - set to 0.0");
                }
            }
        }

        return new ServiceItem(id, name, description, price);
    }

    // toString للعرض في ComboBox أو JList
    @Override
    public String toString() {
        return name + " (" + price + ")";
    }
}