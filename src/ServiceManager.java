import java.util.*;
import java.util.stream.Collectors;

public class ServiceManager {
    private static final String FILE = "services.txt";

    public static List<ServiceItem> load() {
        return FileUtils.readAll(FILE).stream()
                .filter(l -> !l.isBlank())
                .map(ServiceItem::fromCSV)
                .collect(Collectors.toList());
    }

    public static void saveAll(List<ServiceItem> list) {
        FileUtils.writeAll(FILE, list.stream().map(ServiceItem::toCSV).collect(Collectors.toList()));
    }

    public static void add(ServiceItem s) {
        FileUtils.append(FILE, s.toCSV());
    }
    public static void delete(String serviceId) {
    List<ServiceItem> list = load();
    list.removeIf(s -> s.getId().equals(serviceId));
    saveAll(list);
}
}