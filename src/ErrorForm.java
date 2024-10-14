import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorForm extends JFrame {
    private JPanel contentPane;
    private JButton okButton;
    private JLabel errorMessageLabel;

    public ErrorForm(String message) {
        errorMessageLabel.setText(message);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        pack();

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public static void ShowMessage(String message)
    {
        ErrorForm errorForm = new ErrorForm(message);

        errorForm.setLocationRelativeTo(null);

        errorForm.setVisible(true);
    }
}
