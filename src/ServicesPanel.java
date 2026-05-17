import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;

public class ServicesPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public ServicesPanel() {
        setLayout(new BorderLayout());

        // الجدول
        model = new DefaultTableModel(new String[]{"ID", "Name", "Desc", "Price"}, 0);
        table = new JTable(model);
        refreshServices();

        add(new JScrollPane(table), BorderLayout.CENTER);

        // الـ Form في الأعلى
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(20);
        JTextField priceField = new JTextField(10);

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton statsBtn = new JButton("Stats");
        JButton refreshBtn = new JButton("Refresh");

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Desc:"));
        form.add(descField);
        form.add(new JLabel("Price:"));
        form.add(priceField);
        form.add(addBtn);
        form.add(deleteBtn);
        form.add(statsBtn);
        form.add(refreshBtn);

        add(form, BorderLayout.NORTH);

        // Add Button
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter service name!");
                return;
            }
            if (priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter price!");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr.replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price! Use numbers only.");
                return;
            }

            ServiceItem newService = new ServiceItem(
                FileUtils.genId("S"),
                name,
                desc,
                price
            );

            ServiceManager.add(newService);

            nameField.setText("");
            descField.setText("");
            priceField.setText("");

            refreshServices();
        });

        // Delete Button
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a service to delete!");
                return;
            }

            String serviceId = (String) model.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Delete this service?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ServiceManager.delete(serviceId);
                refreshServices();
            }
        });

        // Stats Button
        statsBtn.addActionListener(e -> showServiceStats());

        // Refresh Button
        refreshBtn.addActionListener(e -> refreshServices());
    }

    private void refreshServices() {
        model.setRowCount(0);
        for (ServiceItem s : ServiceManager.load()) {
            model.addRow(new Object[]{
                s.getId(),
                s.getName(),
                s.getDescription(),
                s.getPrice()
            });
        }
    }

    // ←←←← الكود الكامل للإحصائيات (ده اللي ضفناه)
    private void showServiceStats() {
        List<ServiceItem> services = ServiceManager.load();
        List<Reservation> reservations = ReservationManager.load();

        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No services available yet.", "Service Stats", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Map<String, Integer> usageCount = new HashMap<>();
        Map<String, Double> revenue = new HashMap<>();

        // تهيئة
        for (ServiceItem s : services) {
            usageCount.put(s.getId(), 0);
            revenue.put(s.getId(), 0.0);
        }

        // حساب الاستخدام والإيراد من الحجوزات
        for (Reservation r : reservations) {
            for (String serviceId : r.getServicesUsed()) {
                if (usageCount.containsKey(serviceId)) {
                    usageCount.put(serviceId, usageCount.get(serviceId) + 1);

                    ServiceItem service = services.stream()
                            .filter(s -> s.getId().equals(serviceId))
                            .findFirst()
                            .orElse(null);

                    if (service != null) {
                        revenue.put(serviceId, revenue.get(serviceId) + service.getPrice());
                    }
                }
            }
        }

        // بناء النص
        StringBuilder sb = new StringBuilder("Service Statistics:\n\n");
        double totalRevenue = 0;
        for (ServiceItem s : services) {
            int count = usageCount.getOrDefault(s.getId(), 0);
            double rev = revenue.getOrDefault(s.getId(), 0.0);
            totalRevenue += rev;

            sb.append(s.getName())
            .append(" (").append(s.getDescription()).append(")\n")
            .append("   Used: ").append(count).append(" times\n")
            .append("   Revenue: ").append(rev).append("\n\n");
        }

        sb.append("================================\n");
        sb.append("Total Revenue from Services: ").append(totalRevenue);

        JOptionPane.showMessageDialog(this, sb.toString(), "Service Stats", JOptionPane.INFORMATION_MESSAGE);
    }
}