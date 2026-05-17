
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AssignRoomDialog extends JDialog {

    public AssignRoomDialog(JFrame parent, String roomNumber) {
        super(parent, "Assign Room", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Customer selection
        JComboBox<String> customerBox = new JComboBox<>();
        List<Customer> customers = CustomerManager.load();
        for (Customer c : customers) {
            customerBox.addItem(c.id + " - " + c.name);
        }

        JTextField checkInField = new JTextField("2025-01-01");
        JTextField checkOutField = new JTextField("2025-01-02");
        JButton saveBtn = new JButton("Save Reservation");

        add(new JLabel("Customer:"));
        add(customerBox);
        add(new JLabel("Check-in:"));
        add(checkInField);
        add(new JLabel("Check-out:"));
        add(checkOutField);
        add(new JLabel());
        add(saveBtn);

        saveBtn.addActionListener(e -> {
            if (customerBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Select a customer first.");
                return;
            }

            String custId = customerBox.getSelectedItem().toString().split(" - ")[0];
            String checkIn = checkInField.getText();
            String checkOut = checkOutField.getText();

            // Create reservation
            Reservation r = new Reservation(
                FileUtils.genId("R"), custId, roomNumber, checkIn, checkOut, List.of()
            );
            ReservationManager.add(r);

            // Mark room as busy
            List<Room> rooms = RoomManager.load();
            for (Room rm : rooms) {
                if (rm.number.equals(roomNumber)) rm.busy = true;
            }
            RoomManager.saveAll(rooms);

            JOptionPane.showMessageDialog(this, "Room assigned successfully!");
            dispose();
        });
    }
}
