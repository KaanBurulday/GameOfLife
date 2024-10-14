import java.awt.*;
import javax.swing.*;

public class Grid {

    public static void main(String[] args) {
        new Grid();
    }

    public Grid() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        public TestPane() {
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int size = Math.min(getWidth() - 4, getHeight() - 4) / 10;
            System.out.println(size);;
            /**
             * getWidth = 200 -> 196
             * getHeight = 200 -> 196
             * size = 196 / 10 => 19.6 = 19
             *
             *
             * width = 200 - 38 => 162
             *
             * y = ( 200 - ( 190 ) ) / 2 => 5
             */
            int width = getWidth() - (size * 2);
            int height = getHeight() - (size * 2);

            int y = (getHeight() - (size * 10)) / 2;
            for (int horz = 0; horz < 10; horz++) {
                int x = (getWidth() - (size * 10)) / 2;
                for (int vert = 0; vert < 10; vert++) {
                    g.drawRect(x, y, size, size);
                    x += size;
                }
                y += size;
            }
            g2d.dispose();
        }

    }
}