import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterB_NamedInnerClass extends JFrame {
    private final JButton button = new JButton("Convert");

    public ConverterB_NamedInnerClass() {
        setLayout(new FlowLayout());
        add(button);
        button.addActionListener(new ConvertHandler());
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private class ConvertHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(
                ConverterB_NamedInnerClass.this, "Conversion handler executed");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new ConverterB_NamedInnerClass().setVisible(true));
    }
}
