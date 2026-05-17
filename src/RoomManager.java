import java.util.*;
import java.util.stream.Collectors;

public class RoomManager {
    private static final String FILE = "rooms.txt";

    public static List<Room> load() {
        return FileUtils.readAll(FILE).stream()
                .filter(l -> !l.isBlank())
                .map(Room::fromCSV)
                .collect(Collectors.toList());
    }

    public static void saveAll(List<Room> list) {
        FileUtils.writeAll(FILE, list.stream().map(Room::toCSV).collect(Collectors.toList()));
    }

    public static void add(Room r) {
        FileUtils.append(FILE, r.toCSV());
    }
}
