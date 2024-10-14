import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {
    private JLabel gameLabel;
    private JLabel generationLabel;
    private JLabel populationLabel;
    private JButton quitButton;
    private JButton backToMenuButton;
    private JPanel contentPane;
    private JPanel gameBoardPanel;

    private GameBoard gameBoard;
    private AGameOfLife game;

    public GameGUI(AGameOfLife game) {
        this.game = game;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Quit();
            }
        });

        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu();
                dispose();
            }
        });

        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        startGame();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        gameBoard = new GameBoard(game.board);
        gameBoardPanel = gameBoard;
    }

    private void Quit() {
        System.exit(0);
    }

    private void startGame() {
        new Thread(() -> {
            int maxGeneration = 10_000;
            int iteration = 0;
            while (iteration < maxGeneration && game.population != 0) {
                game.next(); // Update the game state
                generationLabel.setText(String.valueOf(game.generation));
                populationLabel.setText(String.valueOf(game.population));
                gameBoard.updateBoard(game.board); // Update the GameBoard with the new board state
                gameBoard.repaint(); // Repaint the board to reflect the new state

                iteration++;
                try {
                    Thread.sleep(game.milliseconds); // Adjust speed as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class GameBoard extends JPanel {
        private Cell[][] board;
        private int rowCount, columnCount, cellSize;

        public GameBoard(Cell[][] board) {
            this.rowCount = board.length;
            this.columnCount = board[0].length;
            this.board = board;
        }

        public void updateBoard(Cell[][] newBoard) {
            this.board = newBoard; // Update the board reference
            this.rowCount = newBoard.length;
            this.columnCount = newBoard[0].length;
        }

        /**
         * CASE 1: width > height
         * the sizing of the cells must be accordingly to the width
         * CASE 2: width < height
         * the sizing of the cells must be accordingly to the height
         * CASE 3: width = height
         * the sizing of the cells must be accordingly to the bigger
         * of the two: column count or width count
         *
         * @param graphics the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2d = (Graphics2D) graphics.create();

            cellSize = 10; // Default
            if (getWidth() > getHeight()) {
                cellSize = (getHeight() - 5) / rowCount;
                if ((cellSize * columnCount) > getWidth())
                    cellSize = (getWidth() - 5) / columnCount;
            } else if (getWidth() < getHeight()) {
                cellSize = (getWidth() - 5) / columnCount;
            } else {
                if (rowCount > columnCount) {
                    cellSize = getHeight() / rowCount;
                } else {
                    cellSize = getWidth() / columnCount;
                }
            }

            int x = 5, y = 0;
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    if (board[i][j].getState() == CellState.LIVE)
                        g2d.fillRect(x, y, cellSize, cellSize);
                    else
                        g2d.drawRect(x, y, cellSize, cellSize);
                    x += cellSize;
                }
                x = 5  ;
                y += cellSize;
            }
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(cellSize*columnCount, cellSize*rowCount);
        }
    }

}
