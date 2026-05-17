import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

public class RoomsPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JComboBox<String> serviceCombo; // field عشان نوصلها من كل الدوال

    public RoomsPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Number", "Type", "Price", "Busy", "Services"}, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // محدش يقدر يعدل في الجدول مباشرة
            }
        };

        add(new JScrollPane(table), BorderLayout.CENTER);

        // الـ Form في الأعلى
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField noField = new JTextField(8);

        // ComboBox لأنواع الغرف
        String[] roomTypes = {"Single", "Double", "Twin", "Suite", "Deluxe", "Family"};
        JComboBox<String> typeCombo = new JComboBox<>(roomTypes);

        JLabel priceLabel = new JLabel("Price: ?????");

        // ComboBox للخدمات
        serviceCombo = new JComboBox<>();
        serviceCombo.addItem("None");
        loadServicesIntoCombo(serviceCombo); // تحميل الخدمات في البداية

        JCheckBox busyCheck = new JCheckBox("Busy");

        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton assignBtn = new JButton("Assign to Customer");
        JButton filterFreeBtn = new JButton("Filter Free");
        JButton nearCheckoutBtn = new JButton("Near Checkout (48h)");
        JButton refreshBtn = new JButton("Refresh");

        form.add(new JLabel("No.:"));
        form.add(noField);
        form.add(new JLabel("Type:"));
        form.add(typeCombo);
        form.add(priceLabel);
        form.add(new JLabel("Service (optional):"));
        form.add(serviceCombo);
        form.add(busyCheck);
        form.add(addBtn);
        form.add(deleteBtn);
        form.add(assignBtn);
        form.add(filterFreeBtn);
        form.add(nearCheckoutBtn);
        form.add(refreshBtn);

        add(form, BorderLayout.NORTH);

        // الأسعار الثابتة
        Map<String, Double> fixedPrices = new HashMap<>();
        fixedPrices.put("Single", 100.0);
        fixedPrices.put("Double", 150.0);
        fixedPrices.put("Twin", 180.0);
        fixedPrices.put("Suite", 300.0);
        fixedPrices.put("Deluxe", 400.0);
        fixedPrices.put("Family", 800.0);

        // تحديث السعر لما يختار النوع
        typeCombo.addActionListener(e -> {
            String selectedType = (String) typeCombo.getSelectedItem();
            Double price = fixedPrices.get(selectedType);
            if (price != null) {
                priceLabel.setText("Price: " + price);
            }
        });

        // Add Button
        addBtn.addActionListener(e -> {
            String number = noField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            boolean busy = busyCheck.isSelected();

            if (number.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter room number!");
                return;
            }

            double price = fixedPrices.getOrDefault(type, 0.0);
            if (price == 0.0) {
                JOptionPane.showMessageDialog(this, "Unknown room type!");
                return;
            }

            // جيب ID الخدمة المختارة
            String selectedService = (String) serviceCombo.getSelectedItem();
            List<String> serviceIds = new ArrayList<>();
            if (selectedService != null && !selectedService.equals("None")) {
                String serviceId = selectedService.split(" - ")[0];
                serviceIds.add(serviceId);
            }

            Room newRoom = new Room(number, type, price, busy, serviceIds);
            RoomManager.add(newRoom);

            noField.setText("");
            busyCheck.setSelected(false);
            typeCombo.setSelectedIndex(0);
            priceLabel.setText("Price: ?????");
            serviceCombo.setSelectedItem("None");
            loadServicesIntoCombo(serviceCombo); // تحديث الخدمات فورًا بعد الإضافة

            refreshRooms();
        });

        // Delete Button
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a room to delete!");
                return;
            }

            String number = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete room " + number + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                List<Room> list = RoomManager.load();
                list.removeIf(rm -> rm.number.equals(number));
                RoomManager.saveAll(list);
                refreshRooms();
            }
        });

        // Assign to Customer (مع فحص التوفر + اقتراح بدائل)
        assignBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a room first!");
                return;
            }

            String roomNumber = (String) model.getValueAt(row, 0);

            List<Customer> customers = CustomerManager.load();
            if (customers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No customers found. Add a customer first.");
                return;
            }

            String[] customerOptions = customers.stream()
                    .map(c -> c.getId() + " - " + c.getName())
                    .toArray(String[]::new);

            String selectedCustomer = (String) JOptionPane.showInputDialog(this, "Select customer:", "Assign Room",
                    JOptionPane.PLAIN_MESSAGE, null, customerOptions, customerOptions[0]);

            if (selectedCustomer == null) return;

            String customerId = selectedCustomer.split(" - ")[0];

            String checkIn = JOptionPane.showInputDialog(this, "Check-in date (yyyy-MM-dd)", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            if (checkIn == null || checkIn.trim().isEmpty()) return;

            String checkOut = JOptionPane.showInputDialog(this, "Check-out date (yyyy-MM-dd)", 
                    new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + 86400000L)));
            if (checkOut == null || checkOut.trim().isEmpty()) return;

            // فحص التوفر
            boolean isAvailable = true;
            List<Reservation> reservations = ReservationManager.load();

            for (Reservation res : reservations) {
                if (res.roomNumber.equals(roomNumber)) {
                    if (!(checkOut.compareTo(res.checkIn) <= 0 || checkIn.compareTo(res.checkOut) >= 0)) {
                        isAvailable = false;
                        break;
                    }
                }
            }

            if (!isAvailable) {
                StringBuilder suggestion = new StringBuilder("This room is not available in the selected dates.\n\nAlternative available rooms:\n");
                int alternatives = 0;
                for (Room altRoom : RoomManager.load()) {
                    if (altRoom.number.equals(roomNumber)) continue;

                    boolean altAvailable = true;
                    for (Reservation res : reservations) {
                        if (res.roomNumber.equals(altRoom.number)) {
                            if (!(checkOut.compareTo(res.checkIn) <= 0 || checkIn.compareTo(res.checkOut) >= 0)) {
                                altAvailable = false;
                                break;
                            }
                        }
                    }

                    if (altAvailable) {
                        suggestion.append(altRoom.number)
                                  .append(" - ")
                                  .append(altRoom.type)
                                  .append(" (Price: ").append(altRoom.price).append(")\n");
                        alternatives++;
                    }
                }

                if (alternatives == 0) {
                    suggestion.append("No alternative rooms available in these dates.");
                }

                JOptionPane.showMessageDialog(this, suggestion.toString(), "Room Not Available", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ←←←← تحديث الخدمات تلقائي كل مرة تضغط Assign
            List<ServiceItem> services = ServiceManager.load(); // يقرأ جديد كل مرة
            String[] serviceOptions = new String[services.size() + 1];
            serviceOptions[0] = "None";
            for (int i = 0; i < services.size(); i++) {
                ServiceItem s = services.get(i);
                serviceOptions[i + 1] = s.getId() + " - " + s.getName() + " (" + s.getPrice() + ")";
            }

            String selectedService = (String) JOptionPane.showInputDialog(this, "Select service to add (optional):", "Service",
                    JOptionPane.PLAIN_MESSAGE, null, serviceOptions, "None");

            if (selectedService == null) return;

            String serviceId = selectedService.equals("None") ? "" : selectedService.split(" - ")[0];
            List<String> servicesUsed = serviceId.isEmpty() ? new ArrayList<>() : Arrays.asList(serviceId);

            Reservation newRes = new Reservation(
                FileUtils.genId("R"),
                customerId,
                roomNumber,
                checkIn,
                checkOut,
                servicesUsed
            );
            ReservationManager.add(newRes);

            // تحديث حالة الغرفة
            List<Room> rooms = RoomManager.load();
            for (Room rm : rooms) {
                if (rm.number.equals(roomNumber)) {
                    rm.busy = true;
                    break;
                }
            }
            RoomManager.saveAll(rooms);

            JOptionPane.showMessageDialog(this, "Room assigned successfully!");
            refreshRooms();
        });

        // Filter Free
        filterFreeBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Room r : RoomManager.load()) {
                if (!r.busy) {
                    model.addRow(new Object[]{
                        r.number,
                        r.type,
                        r.price,
                        r.busy ? "Busy" : "Free",
                        r.services.isEmpty() ? "None" : String.join(";", r.services)
                    });
                }
            }
        });

        // Near Checkout
        nearCheckoutBtn.addActionListener(e -> showNearCheckout());

        // Refresh
        refreshBtn.addActionListener(e -> refreshRooms());

        // عبي الجدول والخدمات بعد ما كل حاجة اتعملت
        refreshRooms();
    }

    private void refreshRooms() {
        model.setRowCount(0);
        for (Room r : RoomManager.load()) {
            model.addRow(new Object[]{
                r.number,
                r.type,
                r.price,
                r.busy ? "Busy" : "Free",
                r.services.isEmpty() ? "None" : String.join(";", r.services)
            });
        }

        // تحديث الخدمات في الـ ComboBox كل مرة ترفرش
        if (serviceCombo != null) {
            loadServicesIntoCombo(serviceCombo);
        }
    }

    private void loadServicesIntoCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        combo.addItem("None");
        List<ServiceItem> services = ServiceManager.load();
        for (ServiceItem s : services) {
            combo.addItem(s.getId() + " - " + s.getName() + " (" + s.getPrice() + ")");
        }
    }

    private void showNearCheckout() {
        List<Reservation> reservations = ReservationManager.load();
        StringBuilder sb = new StringBuilder("Near Checkout (within 48 hours):\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        int count = 0;
        for (Reservation r : reservations) {
            long days = daysBetween(today, r.checkOut);
            if (days >= 0 && days <= 2) {
                Customer c = CustomerManager.load().stream()
                        .filter(cust -> cust.getId().equals(r.customerId))
                        .findFirst()
                        .orElse(null);

                String customerName = c != null ? c.getName() : r.customerId;

                sb.append("Res ID: ").append(r.id)
                  .append(" | Customer: ").append(customerName)
                  .append(" | Room: ").append(r.roomNumber)
                  .append(" | Checkout: ").append(r.checkOut).append("\n");
                count++;
            }
        }

        if (count == 0) {
            sb.append("No reservations near checkout.");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Near Checkout", JOptionPane.INFORMATION_MESSAGE);
    }

    private long daysBetween(String date1, String date2) {
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
            Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(date2);
            return (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            return 0;
        }
    }
}