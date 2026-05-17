public abstract class User {
    private String id;
    private String name;
    private String phone;

    public User(String id, String name, String phone) {
        this.id = id != null ? id : "";
        this.name = name != null ? name : "";
        this.phone = phone != null ? phone : "";
    }

    // ←←←← Getters مهمة جدًا
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    // اختياري: setters لو عايز تعدل بعدين
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}