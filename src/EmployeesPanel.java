import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class EmployeesPanel extends JPanel {
DefaultTableModel model;


public EmployeesPanel() {
setLayout(new BorderLayout());
model = new DefaultTableModel(new String[]{"ID","Name","Phone","Role"}, 0);
JTable table = new JTable(model);
refresh();
add(new JScrollPane(table), BorderLayout.CENTER);


JPanel form = new JPanel();
JTextField name = new JTextField(10);
JTextField phone = new JTextField(10);
JTextField role = new JTextField(8);
JButton add = new JButton("Add");
JButton del = new JButton("Delete");


form.add(new JLabel("Name")); form.add(name);
form.add(new JLabel("Phone")); form.add(phone);
form.add(new JLabel("Role")); form.add(role);
form.add(add); form.add(del);
add(form, BorderLayout.NORTH);


add.addActionListener(e -> {
EmployeeManager.add(new Employee(FileUtils.genId("E"), name.getText(), phone.getText(), role.getText()));
name.setText(""); phone.setText(""); role.setText("");
refresh();
});


del.addActionListener(e -> {
int r = table.getSelectedRow();
if (r == -1) return;
String id = (String) model.getValueAt(r,0);
EmployeeManager.delete(id);
refresh();
});
}

void refresh() {
    model.setRowCount(0);
    for (Employee e : EmployeeManager.load()) {
        model.addRow(new Object[]{
            e.getId(),
            e.getName(),
            e.getPhone(),
            e.getRole()
        });
    }
}
}