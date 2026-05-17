import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginDialog extends JDialog {

    private Map<String, String> users = new HashMap<>(); // username -> password
    private static final String USERS_FILE = "users.txt";
    private String loggedInUsername = null; // لحفظ الـ username للصلاحيات
    private boolean loginSuccessful = false; // ←←←← جديد: عشان نعرف إن اللوجين نجح ولا لأ

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        // Username: حروف بس (يمنع أي حاجة تانية بدون رسالة)
        userField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && c != '\b') {
                    evt.consume();
                }
            }
        });

        // Password: أرقام بس (يمنع أي حاجة تانية بدون رسالة)
        passField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                }
            }
        });

        loadUsers();

        add(new JLabel("Username:"));
        add(userField);
        add(new JLabel("Password:"));
        add(passField);

        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");

        JPanel buttons = new JPanel();
        buttons.add(loginBtn);
        buttons.add(cancelBtn);

        add(new JLabel(""));
        add(buttons);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter username and password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authenticate(username, password)) {
                loggedInUsername = username; // حفظ الـ username
                loginSuccessful = true; // ←←←← مهم جدًا: نعلم إن اللوجين نجح

                // رسالة نجاح تظهر 3 ثواني وتختفي تلقائي
                JDialog successDialog = new JDialog(this, "Success", true);
                successDialog.setLayout(new FlowLayout());
                successDialog.add(new JLabel("Login successful! Welcome " + username));
                successDialog.pack();
                successDialog.setLocationRelativeTo(this);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        successDialog.dispose();
                        dispose(); // يقفل اللوجين ويسمح للبرنامج الرئيسي يستمر
                    }
                }, 3000); // 3 ثواني

                successDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                userField.setText("");
                passField.setText("");
            }
        });

        cancelBtn.addActionListener(e -> System.exit(0));
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (FileNotFoundException e) {
            // أول مرة: مستخدمين افتراضيين
            users.put("admin", "1234");
            users.put("reception", "5678");
            saveUsers();
            JOptionPane.showMessageDialog(this, "Default users created:\nUsername: admin / Password: 1234\nUsername: reception / Password: 5678");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    // دالة ترجع الـ username
    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    // ←←←← دالة جديدة: ترجع إن اللوجين نجح ولا لأ
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}