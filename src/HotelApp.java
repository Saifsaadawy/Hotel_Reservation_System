import javax.swing.*;

public class HotelApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hotel Reservation System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            // الـ frame فاضي في البداية (مش هيظهر أي loading)
            // مفيش add لأي JLabel

            // فتح اللوجين
            LoginDialog login = new LoginDialog(frame);
            login.setVisible(true);

            // بعد ما اللوجين يتقفل
            if (login.isLoginSuccessful()) {
                String username = login.getLoggedInUsername();

                JTabbedPane tabs = new JTabbedPane();

                // تبويبات عامة للكل
                tabs.add("Customers", new CustomersPanel());
                tabs.add("Rooms", new RoomsPanel());
                tabs.add("Services", new ServicesPanel());
                tabs.add("Reservations / Invoice", new ReservationsPanel());

                // تبويب Employees للـ admin بس
                if ("admin".equalsIgnoreCase(username)) {
                    tabs.add("Employees", new EmployeesPanel());
                }

                // إضافة الـ Tabs للـ frame
                frame.getContentPane().add(tabs);
                frame.revalidate();
                frame.repaint();
            } else {
                // لو فشل أو إلغى اللوجين، يقفل البرنامج
                System.exit(0);
            }

            frame.setVisible(true);  // يظهر الـ frame بعد اللوجين
        });
    }
}