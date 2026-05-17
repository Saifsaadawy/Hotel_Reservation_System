
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeManager {
    private static final String FILE = "employees.txt";

    public static List<Employee> load() {
        return FileUtils.readAll(FILE).stream()
                .filter(l -> !l.isBlank())
                .map(Employee::fromCSV)
                .collect(Collectors.toList());
    }

    public static void saveAll(List<Employee> list) {
        FileUtils.writeAll(FILE, list.stream().map(Employee::toCSV).collect(Collectors.toList()));
    }

    public static void add(Employee e) {
        FileUtils.append(FILE, e.toCSV());
    }
    public static void delete(String employeeId) {
    List<Employee> list = load();
    list.removeIf(emp -> emp.getId().equals(employeeId));
    saveAll(list);
}
}
