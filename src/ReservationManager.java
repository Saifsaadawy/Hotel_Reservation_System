import java.util.*;
import java.util.stream.Collectors;

public class ReservationManager {
    private static final String FILE = "reservations.txt";

    public static List<Reservation> load() {
        return FileUtils.readAll(FILE).stream()
                .filter(l -> !l.isBlank())
                .map(Reservation::fromCSV)
                .collect(Collectors.toList());
    }

    public static void saveAll(List<Reservation> list) {
        FileUtils.writeAll(FILE, list.stream().map(Reservation::toCSV).collect(Collectors.toList()));
    }

    public static void add(Reservation r) {
        FileUtils.append(FILE, r.toCSV());
    }
    
}
