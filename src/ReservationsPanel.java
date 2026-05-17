import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ReservationsPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public ReservationsPanel() {
        setLayout(new BorderLayout());
        
        model = new DefaultTableModel(new String[]{"ResID", "Customer", "Room", "CheckIn", "CheckOut", "Services"}, 0);
        table = new JTable(model);
        refreshReservations();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshBtn = new JButton("Refresh");
        JButton invoiceBtn = new JButton("Generate Invoice for Selected");
        JButton deleteBtn = new JButton("Delete Reservation");

        top.add(refreshBtn);
        top.add(invoiceBtn);
        top.add(deleteBtn);

        add(top, BorderLayout.NORTH);

        refreshBtn.addActionListener(e -> refreshReservations());

        invoiceBtn.addActionListener(e -> generateInvoice());

        deleteBtn.addActionListener(e -> deleteReservation());
    }

    private void refreshReservations() {
        model.setRowCount(0);
        List<Reservation> reservations = ReservationManager.load();
        List<Customer> customers = CustomerManager.load();

        for (Reservation r : reservations) {
            String customerName = customers.stream()
                    .filter(c -> c.getId().equals(r.customerId))
                    .map(c -> c.getName())
                    .findFirst()
                    .orElse(r.customerId);

            String servicesStr = r.servicesUsed.isEmpty() ? "None" : String.join(";", r.servicesUsed);

            model.addRow(new Object[]{
                r.id,
                customerName,
                r.roomNumber,
                r.checkIn,
                r.checkOut,
                servicesStr
            });
        }
    }

    private void generateInvoice() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation first!");
            return;
        }

        String resId = (String) model.getValueAt(row, 0);
        Reservation reservation = ReservationManager.load().stream()
                .filter(r -> r.id.equals(resId))
                .findFirst()
                .orElse(null);

        if (reservation == null) return;

        // جيب بيانات العميل والغرفة والخدمات
        Customer customer = CustomerManager.load().stream()
                .filter(c -> c.getId().equals(reservation.customerId))
                .findFirst()
                .orElse(null);

        Room room = RoomManager.load().stream()
                .filter(rm -> rm.number.equals(reservation.roomNumber))
                .findFirst()
                .orElse(null);

        List<ServiceItem> allServices = ServiceManager.load();

        // حساب عدد الليالي
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long nights = 1;
        try {
            Date in = sdf.parse(reservation.checkIn);
            Date out = sdf.parse(reservation.checkOut);
            nights = (out.getTime() - in.getTime()) / (1000 * 60 * 60 * 24);
            if (nights < 1) nights = 1;
        } catch (Exception ex) {}

        // بناء الفاتورة
        StringBuilder invoice = new StringBuilder();
        invoice.append("============== INVOICE ==============\n");
        invoice.append("Reservation ID: ").append(reservation.id).append("\n");
        invoice.append("Customer: ").append(customer != null ? customer.getName() : reservation.customerId).append("\n");
        invoice.append("Room: ").append(reservation.roomNumber).append("\n");
        invoice.append("Check-in: ").append(reservation.checkIn).append("\n");
        invoice.append("Check-out: ").append(reservation.checkOut).append("\n");
        invoice.append("Nights: ").append(nights).append("\n\n");

        double total = 0;

        if (room != null) {
            double roomCost = room.price * nights;
            invoice.append("Room Price per Night: ").append(room.price).append("\n");
            invoice.append("Room Total: ").append(roomCost).append("\n");
            total += roomCost;
        }

        if (!reservation.servicesUsed.isEmpty()) {
            invoice.append("Additional Services:\n");
            for (String serviceId : reservation.servicesUsed) {
                ServiceItem service = allServices.stream()
                        .filter(s -> s.getId().equals(serviceId))
                        .findFirst()
                        .orElse(null);
                if (service != null) {
                    invoice.append(" - ").append(service.getName()).append(": ").append(service.getPrice()).append("\n");
                    total += service.getPrice();
                }
            }
        }

        invoice.append("=====================================\n");
        invoice.append("TOTAL: ").append(total).append("\n");
        invoice.append("=====================================");

        // عرض الفاتورة في نافذة منبثقة
        JTextArea textArea = new JTextArea(invoice.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scroll, "Invoice for Reservation " + resId, JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation to delete!");
            return;
        }

        String resId = (String) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this reservation?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // حذف الحجز
            List<Reservation> list = ReservationManager.load();
            list.removeIf(r -> r.id.equals(resId));
            ReservationManager.saveAll(list);

            // تحرير الغرفة (خليها free)
            String roomNumber = (String) model.getValueAt(row, 2);
            List<Room> rooms = RoomManager.load();
            for (Room rm : rooms) {
                if (rm.number.equals(roomNumber)) {
                    rm.busy = false;
                }
            }
            RoomManager.saveAll(rooms);

            refreshReservations();
        }
    }
}