import jdk.swing.interop.DropTargetContextWrapper;

import javax.swing.*;
import java.awt.*;


public class GridTest {

    public static void main(String[] args) {
        new GridTest();
    }

    public GridTest() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                Board board = new Board(10, 15);

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(board);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);


            }
        });
    }

    public class Board extends JPanel {
        int[][] board;
        int rowCount, columnCount;

        public Board(int rowCount, int columnCount) {
            this.rowCount = rowCount;
            this.columnCount = columnCount;
            this.board = new int[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    board[i][j] = 0;
                }
            }
        }

        public void put(int x, int y, int value) {
            board[x][y] = value;
        }

        public void printBoard() {
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
        }

        /**
         * CASE 1: width > height
         *      the sizing of the cells must be accordingly to the width
         * CASE 2: width < height
         *      the sizing of the cells must be accordingly to the height
         * CASE 3: width = height
         *      the sizing of the cells must be accordingly to the bigger
         *      of the two: column count or width count
         * @param graphics the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2d = (Graphics2D) graphics.create();

            int cellSize = 10; // Default
            if ( getWidth() > getHeight() ) {
                System.out.println("width > height");
                cellSize = (getHeight() - 5) / rowCount;
                if ( (cellSize * columnCount) > getWidth() )
                    cellSize = (getWidth() - 5) / columnCount;
            }
            else if ( getWidth() < getHeight() ) {
                System.out.println("width < height");
                cellSize = (getWidth() - 5) / columnCount;
            } else {
                if ( rowCount > columnCount ) {
                    cellSize = getHeight() / rowCount;
                } else {
                    cellSize = getWidth() / columnCount;
                }
            }


            int x = 0, y = 0;
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    if ( board[i][j] == 1 )
                        g2d.fillRect(x, y, cellSize, cellSize);
                    else
                        g2d.drawRect(x, y, cellSize, cellSize);
                    x += cellSize;
                }
                x = 0;
                y += cellSize;
            }
            /*
            System.out.println("Width: " + getWidth());
            System.out.println("Height: " + getHeight());
            System.out.println("Cell Size: " + cellSize);
            System.out.println("----------------------------------------------------");

            int sizeFactor = Math.max(columnCount, rowCount);
            int size = Math.min(getWidth() - 4, getHeight() - 4) / sizeFactor;
            int width = getWidth() - (size * 2);
            int height = getHeight() - (size * 2);

            int y = (getHeight() - (size * rowCount)) / 2;
            int x = 0;
            for (int horz = 0; horz < width; horz++) {
                x = (getWidth() - (size * columnCount)) / 2;
                for (int vert = 0; vert < height; vert++) {
                    graphics.drawRect(x, y, size, size);
                    x += size;
                }
                y += size;
            }
            */
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 500);
        }
    }
}
