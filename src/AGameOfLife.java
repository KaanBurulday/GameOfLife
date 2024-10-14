import java.util.*;

/**
 * The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells, each of which is in
 * one of two possible states, live or dead (or populated and unpopulated, respectively). Every cell interacts with its
 * eight neighbors, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time,
 * the following transitions occur:
 * <p>
 * 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
 * 2. Any live cell with two or three live neighbours lives on to the next generation.
 * 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
 * 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
 * <p>
 * The initial pattern constitutes the seed of the system. The first generation is created by applying the above rules
 * simultaneously to every cell in the seed, live or dead; births and deaths occur simultaneously, and the discrete moment
 * at which this happens is sometimes called a tick. Each generation is a pure function of the preceding one. The
 * rules continue to be applied repeatedly to create further generations.
 */

enum CellState {
    DEAD,
    LIVE
}

class Cell {
    private int x, y;
    private CellState state;

    public Cell(int x, int y, CellState state) {
        this.x = x;
        this.y = y;
        this.state = CellState.DEAD;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", state=" + state +
                "}\n";
    }
}


public class AGameOfLife {
    Cell[][] board, nextBoard;
    int width, height, generation, population, milliseconds, maxGeneration;

    public AGameOfLife(int width, int height, int milliseconds, int maxGeneration) {
        this.maxGeneration = maxGeneration;
        this.milliseconds = milliseconds;
        this.width = width;
        this.height = height;
        this.board = new Cell[width][height];
        this.nextBoard = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[i][j] = new Cell(i, j, CellState.DEAD);
                this.nextBoard[i][j] = new Cell(i, j, CellState.DEAD);
            }
        }

        this.generation = 0;
        this.population = 0;
    }

    public void printBoard() {
        System.out.println("Generation: " + generation);
        System.out.println("Population: " + population);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(board[i][j].getState().ordinal());
            }
            System.out.println();
        }
        System.out.println();
    }

    // width:height-width1:height1-...
    public String generateSeed(int numberOfCells) {
        StringBuilder seed = new StringBuilder();
        Random random = new Random();
        Set<String> usedCells = new HashSet<>();
        if (numberOfCells == 0) {
            return "0";
        } else if ((numberOfCells <= 0) || (numberOfCells > (width * height))) {
            return "-1"; //every cell is alive
        } else {
            while (usedCells.size() < numberOfCells) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                String cell = x + ":" + y;

                if (!usedCells.contains(cell)) {  // Avoid duplicates
                    usedCells.add(cell);
                    seed.append(cell).append("-");
                }
            }
            seed.delete(seed.length() - 1, seed.length());
        }
        return seed.toString();
    }

    public String generateSeed() {
        StringBuilder seed = new StringBuilder();
        Random random = new Random();
        Set<String> usedCells = new HashSet<>();
        int maxNumberOfCells = width * height;
        int numberOfCells = random.nextInt(maxNumberOfCells) + 1;
        while (usedCells.size() < numberOfCells) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            String cell = x + ":" + y;

            if (!usedCells.contains(cell)) {  // Avoid duplicates
                usedCells.add(cell);
                seed.append(cell).append("-");
            }
        }
        seed.delete(seed.length() - 1, seed.length());

        return seed.toString();
    }

    public static String generateSeed(int width, int height)
    {
        try {
            StringBuilder seed = new StringBuilder();
            Random random = new Random();
            Set<String> usedCells = new HashSet<>();
            int maxNumberOfCells = width * height;
            int numberOfCells = random.nextInt(maxNumberOfCells) + 1;
            while (usedCells.size() < numberOfCells) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                String cell = x + ":" + y;

                if (!usedCells.contains(cell)) {  // Avoid duplicates
                    usedCells.add(cell);
                    seed.append(cell).append("-");
                }
            }
            seed.delete(seed.length() - 1, seed.length());

            return seed.toString();
        } catch (Exception ex) {
            throw new InputMismatchException("Please check the format of the seed");
        }
    }

    public void initial(String seed) {
        ArrayList<Cell> cells = new ArrayList<>();
        String[] seedlets = seed.split("-");
        for (String seedlet : seedlets) {
            String[] seedInfo = seedlet.split(":");
            int x = Integer.parseInt(seedInfo[0]);
            int y = Integer.parseInt(seedInfo[1]);
            board[x][y].setState(CellState.LIVE);
            population += 1;
        }
    }

    /**
     * 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
     * 2. Any live cell with two or three live neighbours lives on to the next generation.
     * 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
     * 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     * <p>
     * 1. if aliveNeighbors < 2 and alive : die -> underpopulation
     * 2. if (aliveNeighbors == 2 or aliveNeighbors == 3) and alive : live -> next generation
     * 3. if aliveNeighbors > 3 and alive : die -> overpopulation
     * 4. if aliveNeighbors == 3 and dead : live -> reproduction
     */
    // ChatGPT version of the next() function
     public void next() {
        int aliveNeighbors;
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                aliveNeighbors = 0;

                for (int k = 0; k < 8; k++) {
                    int ni = i + dx[k];
                    int nj = j + dy[k];

                    if (ni >= 0 && ni < width && nj >= 0 && nj < height && board[ni][nj].getState() == CellState.LIVE) {
                        aliveNeighbors++;
                    }
                }

                if ((aliveNeighbors < 2 || aliveNeighbors > 3) && board[i][j].getState() == CellState.LIVE) {
                    nextBoard[i][j].setState(CellState.DEAD);
                    population--;
                } else if (aliveNeighbors == 3 && board[i][j].getState() == CellState.DEAD) {
                    nextBoard[i][j].setState(CellState.LIVE);
                    population++;
                } else if ((aliveNeighbors == 2 || aliveNeighbors == 3) && board[i][j].getState() == CellState.LIVE) {
                    nextBoard[i][j].setState(CellState.LIVE);
                }
            }
        }
        board = nextBoard;
        generation++;
    }



    public static void main(String[] args) {
        AGameOfLife game = new AGameOfLife(10, 10, 500, 10_000);
        Random random = new Random();
        game.initial(game.generateSeed());
        game.printBoard();

        int iteration = 0;;
        while( (iteration < game.maxGeneration) && (game.population != 0) ) {
            game.next();
            game.printBoard();
            iteration++;
            try {
                Thread.sleep(game.milliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


// My version of the next() function
//public void next() {
//    int aliveNeighbors;
//    for (int i = 0; i < width; i++) {
//        for (int j = 0; j < height; j++) {
//            aliveNeighbors = 0;
//            if (i > 0) {
//                if (board[i - 1][j].getState() == CellState.LIVE) {
//                    aliveNeighbors++;
//                }
//                if (j > 0) {
//                    if (board[i - 1][j - 1].getState() == CellState.LIVE) {
//                        aliveNeighbors++;
//                    }
//                }
//                if (j != width - 1) {
//                    if (board[i - 1][j + 1].getState() == CellState.LIVE) {
//                        aliveNeighbors++;
//                    }
//                }
//            }
//
//            if (j > 0) {
//                if (board[i][j - 1].getState() == CellState.LIVE) {
//                    aliveNeighbors++;
//                }
//            }
//            if (j != height - 1) {
//                if (board[i][j + 1].getState() == CellState.LIVE) {
//                    aliveNeighbors++;
//                }
//            }
//
//            if (i != width - 1) {
//                if (board[i + 1][j].getState() == CellState.LIVE) {
//                    aliveNeighbors++;
//                }
//                if (j > 0) {
//                    if (board[i + 1][j - 1].getState() == CellState.LIVE) {
//                        aliveNeighbors++;
//                    }
//                }
//                if (j != height - 1) {
//                    if (board[i + 1][j + 1].getState() == CellState.LIVE) {
//                        aliveNeighbors++;
//                    }
//                }
//            }
//            if( (aliveNeighbors < 2) && (board[i][j].getState() == CellState.LIVE) )
//            {
//                nextBoard[i][j].setState(CellState.DEAD);
//                population--;
//            }
//            else if( (aliveNeighbors == 2 || aliveNeighbors == 3) && (board[i][j].getState() == CellState.LIVE) )
//            {
//                nextBoard[i][j].setState(CellState.LIVE);
//            }
//            else if( (aliveNeighbors == 3) && (board[i][j].getState() == CellState.DEAD) )
//            {
//                nextBoard[i][j].setState(CellState.LIVE);
//                population++;
//            }
//            else if( (aliveNeighbors > 3) && (board[i][j].getState() == CellState.LIVE) )
//            {
//                nextBoard[i][j].setState(CellState.DEAD);
//                population--;
//            }
//        }
//    }
//    board = nextBoard;
//    generation++;
//}
