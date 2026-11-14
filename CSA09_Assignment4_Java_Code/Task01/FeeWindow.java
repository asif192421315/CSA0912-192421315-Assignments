import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FeeWindow extends JFrame {
    private final JTextField regNo = new JTextField(15);
    private final JTextField amount = new JTextField(15);
    private final JLabel status = new JLabel("Enter payment details");
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new GridLayout(4, 2, 10, 10));
        JButton pay = new JButton("Pay");
        JButton clear = new JButton("Clear");
        JButton receipt = new JButton("Receipt");

        add(new JLabel("Register Number")); add(regNo);
        add(new JLabel("Amount")); add(amount);
        add(pay); add(clear); add(receipt); add(status);

        pay.addActionListener(e ->
            status.setText(service.payFee(regNo.getText(), amount.getText())));
        clear.addActionListener(e -> {
            regNo.setText(""); amount.setText("");
            status.setText("Fields cleared");
        });
        receipt.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                service.generateReceipt(regNo.getText())));

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(450, 250);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
