import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterC_AnonymousClass extends JFrame {
    public ConverterC_AnonymousClass() {
        JButton button = new JButton("Convert");
        setLayout(new FlowLayout());
        add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    ConverterC_AnonymousClass.this,
                    "Conversion handler executed");
            }
        });
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new ConverterC_AnonymousClass().setVisible(true));
    }
}
