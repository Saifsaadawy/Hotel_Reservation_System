import java.util.*;

public class Reservation {
    public String id;
    public String customerId;
    public String roomNumber;
    public String checkIn;
    public String checkOut;
    public List<String> servicesUsed = new ArrayList<>();  // ←←←← ده الحقل المهم

    public Reservation(String id, String customerId, String roomNumber, String checkIn, String checkOut, List<String> servicesUsed) {
        this.id = id;
        this.customerId = customerId;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.servicesUsed = servicesUsed != null ? servicesUsed : new ArrayList<>();
    }

    // ←←←← Getter مهم جدًا عشان السطر في الـ Panel يشتغل
    public List<String> getServicesUsed() {
        return servicesUsed;
    }

    // toCSV و fromCSV (مهمين للتخزين)
    public String toCSV() {
        String servicesCsv = servicesUsed.isEmpty() ? "" : String.join(";", servicesUsed);
        return id + "," + customerId + "," + roomNumber + "," + checkIn + "," + checkOut + "," + servicesCsv;
    }

    public static Reservation fromCSV(String line) {
        String[] parts = line.split(",", -1);
        String id = parts[0];
        String customerId = parts[1];
        String roomNumber = parts[2];
        String checkIn = parts[3];
        String checkOut = parts[4];

        List<String> servicesUsed = new ArrayList<>();
        if (parts.length > 5 && !parts[5].isEmpty()) {
            servicesUsed = Arrays.asList(parts[5].split(";"));
        }

        return new Reservation(id, customerId, roomNumber, checkIn, checkOut, servicesUsed);
    }
}