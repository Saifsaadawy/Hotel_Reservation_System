

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class CustomersPanel extends JPanel {
DefaultTableModel model;


public CustomersPanel() {
setLayout(new BorderLayout());
model = new DefaultTableModel(new String[]{"ID","Name","Phone"},0);
JTable table = new JTable(model);
refresh();
add(new JScrollPane(table), BorderLayout.CENTER);


JPanel form = new JPanel();
JTextField name = new JTextField(10);
JTextField phone = new JTextField(10);
JButton add = new JButton("Add");
JButton del = new JButton("Delete");


form.add(new JLabel("Name")); form.add(name);
form.add(new JLabel("Phone")); form.add(phone);
form.add(add); form.add(del);
add(form, BorderLayout.NORTH);


add.addActionListener(e -> {
CustomerManager.add(new Customer(FileUtils.genId("C"), name.getText(), phone.getText()));
name.setText(""); phone.setText(""); refresh();
});


del.addActionListener(e -> {
int r = table.getSelectedRow(); if (r==-1) return;
CustomerManager.delete((String)model.getValueAt(r,0)); refresh();
});
}


void refresh(){
model.setRowCount(0);
for(Customer c: CustomerManager.load())
model.addRow(new Object[]{c.id,c.name,c.phone});
}
}