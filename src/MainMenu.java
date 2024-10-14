import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;

public class MainMenu extends JFrame {
    private JPanel contentPane;
    private JTextField widthTextField;
    private JTextField heightTextField;
    private JTextField millisecondsTextField;
    private JTextField maxGenerationTextField;
    private JTextField seedTextField;
    private JButton quitButton;
    private JButton startButton;
    private JButton generateButton;

    public MainMenu() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setContentPane(contentPane);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int width = 50;
                    int height = 50;
                    int milliseconds = 250;
                    int maxGeneration = 10_000;
                    String seed = "";
                    if (!widthTextField.getText().isEmpty())
                        width = Integer.parseInt(widthTextField.getText());
                    if (!heightTextField.getText().isEmpty())
                        height = Integer.parseInt(heightTextField.getText());
                    if (!millisecondsTextField.getText().isEmpty())
                        milliseconds = Integer.parseInt(millisecondsTextField.getText());
                    if (!maxGenerationTextField.getText().isEmpty())
                        maxGeneration = Integer.parseInt(maxGenerationTextField.getText());
                    if (!seedTextField.getText().isEmpty())
                        seed = seedTextField.getText();

                    AGameOfLife game = new AGameOfLife(width, height, milliseconds, maxGeneration);

                    if (seed.isEmpty())
                        seed = game.generateSeed();
                    game.initial(seed);
                    GameGUI gameGUI = new GameGUI(game);
                    dispose();

                } catch (NumberFormatException ex) {
                    ErrorForm.ShowMessage("All values must be integer!");
                } catch (InputMismatchException ex) {
                    ErrorForm.ShowMessage(ex.getMessage());
                } catch (Exception ex) {
                    ErrorForm.ShowMessage(ex.getMessage());
                }
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int width = 50, height = 50;
                    if (!widthTextField.getText().isEmpty())
                        width = Integer.parseInt(widthTextField.getText());
                    if (!heightTextField.getText().isEmpty())
                        height = Integer.parseInt(heightTextField.getText());
                    seedTextField.setText(AGameOfLife.generateSeed(width, height));
                } catch (NumberFormatException ex) {
                    ErrorForm.ShowMessage("Width and Height must be integer!");
                } catch (InputMismatchException ex) {
                    ErrorForm.ShowMessage(ex.getMessage());
                }
            }
        });


        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Quit();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void Quit() {
        System.exit(0);
    }


}
