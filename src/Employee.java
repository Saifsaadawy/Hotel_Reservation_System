public class Employee extends User {
    private String role;

    public Employee(String id, String name, String phone, String role) {
        super(id, name, phone);
        this.role = role != null ? role : "";
    }

    // ←←←← Getter للـ role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String toCSV() {
        return getId() + "," + getName() + "," + getPhone() + "," + role;
        // أحسن نستخدم getters بدل الحقول مباشرة
    }

    public static Employee fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] p = line.split(",", -1);
        return new Employee(p[0].trim(), p[1].trim(), p[2].trim(), p.length > 3 ? p[3].trim() : "");
    }
}