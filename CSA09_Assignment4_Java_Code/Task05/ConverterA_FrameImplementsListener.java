import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterA_FrameImplementsListener extends JFrame
        implements ActionListener {
    private final JButton button = new JButton("Convert");

    public ConverterA_FrameImplementsListener() {
        setLayout(new FlowLayout());
        add(button);
        button.addActionListener(this);
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        convert();
    }

    private void convert() {
        JOptionPane.showMessageDialog(this, "Conversion handler executed");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new ConverterA_FrameImplementsListener().setVisible(true));
    }
}
