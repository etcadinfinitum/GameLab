package experiment.gamelab;

import java.util.Observable;
import java.util.Observer;
import android.graphics.Point;

/**
 * The tic-tac-toe game engine. Is an Observable-type object. The game view is added as an observer
 * upon instantiation.
 */
public class TicTacToe_model extends Observable {

    private boolean activeGame = false;
    private boolean stalemate = false;
    private TicTacToe_square[][] board;
    private boolean Xwon;
    private Point[] winningSquares;

    /**
     * Constructor for the game engine.
     * @param obs The observer-type object that will be notified of changes to the model
     */
    public TicTacToe_model(Observer obs) {
        super();
        addObserver(obs);

        // initialize values
        activeGame = true;
        Xwon = false;
        winningSquares = new Point[3];

        // set up game board array
        board = new TicTacToe_square[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = new TicTacToe_square();
            }
        }
    }

    /**
     * Make a move on the tic-tac-toe board. Called by the view class.
     * @param row The row number of the selected cell
     * @param col The column number of the selected cell
     * @param isXturn A boolean flag indicating whether X or O took this turn (true --> X)
     */
    public void move(int row, int col, boolean isXturn) {
        if (isXturn) {
            board[row][col].clicked("X");
        } else {
            board[row][col].clicked("O");
        }
        checkGameStatus();
        setChanged();
        notifyObservers();
    }

    /**
     * A utility method for the CPU turn to use to determine whether a winning move exists on the board
     * @return the Point object that represents
     */
    public Point findNextCPUMove(boolean block) {
        // System.out.println("calling findNextCPUMove with block boolean as: " + block);
        Point winner = null;
        // columns first
        for (int col = 0; col < 3; col++) {
            int oCount = 0;
            int emptyRow = -1;
            int emptyCol = -1;
            if (board[0][col].isClicked() && board[1][col].isClicked() && board[2][col].isClicked()) {
                // System.out.println("All squares for column# " + col + " are occupied; continuing");
                continue;
            } else {
                for (int row = 0; row < 3; row++) {
                    if (!board[row][col].isClicked()) {
                        emptyRow = row;
                        emptyCol = col;
                    } else {
                        if (!block && board[row][col].whichTeam().equals("O")) {
                            oCount++;
                        } else if (block && board[row][col].whichTeam().equals("X")) {
                            oCount++;
                        }
                    }
                }
                // System.out.println("Searching columns for valid move. Col value is " + col + "; emptyRow, emptyCol, and oCount are all " + emptyRow + ", " + emptyCol + ", " + oCount );
                if (oCount == 2 && emptyRow != -1 && emptyCol != -1) {
                    winner = new Point(emptyRow, emptyCol);
                    return winner;
                }
            }
        }
        // then rows
        for (int row = 0; row < 3; row++) {
            int oCount = 0;
            int emptyRow = -1;
            int emptyCol = -1;
            if (board[row][0].isClicked() && board[row][1].isClicked() && board[row][2].isClicked()) {
                // System.out.println("All squares for row# " + row + " are occupied; continuing");
                continue;
            } else {
                for (int col = 0; col < 3; col++) {
                    if (!board[row][col].isClicked()) {
                        emptyRow = row;
                        emptyCol = col;
                    } else {
                        if (!block && board[row][col].whichTeam().equals("O")) {
                            oCount++;
                        } else if (block && board[row][col].whichTeam().equals("X")) {
                            oCount++;
                        }
                    }
                }
                // System.out.println("Searching columns for valid move. row value is " + row + "; emptyRow, emptyCol, and oCount are all " + emptyRow + ", " + emptyCol + ", " + oCount );
                if (oCount == 2 && emptyRow != -1 && emptyCol != -1) {
                    winner = new Point(emptyRow, emptyCol);
                    return winner;
                }
            }
        }
        // then diagonals
        int oCount = 0;
        int emptyRow = -1;
        int emptyCol = -1;
        // first diagonal
        for (int idx = 0; idx < 3; idx++) {
            if (!board[idx][idx].isClicked()) {
                emptyRow = idx;
                emptyCol = idx;
            } else {
                if (!block && board[idx][idx].whichTeam().equals("O")) {
                    oCount++;
                } else if (block && board[idx][idx].whichTeam().equals("X")) {
                    oCount++;
                }
            }
        }
        if (oCount == 2 && emptyCol != -1 && emptyRow != -1) {
            winner = new Point(emptyRow, emptyCol);
            return winner;
        }
        // next diagonal
        for (int idx = 0; idx < 3; idx++) {
            if (!board[idx][idx].isClicked()) {
                emptyRow = idx;
                emptyCol = idx;
            } else {
                if (!block && board[idx][2 - idx].whichTeam().equals("O")) {
                    oCount++;
                } else if (block && board[idx][2 - idx].whichTeam().equals("X")) {
                    oCount++;
                }
            }
        }
        if (oCount == 2 && emptyCol != -1 && emptyRow != -1) {
            winner = new Point(emptyRow, emptyCol);
            return winner;
        }
        return winner;
    }

    /**
     * A helper method which determines whether the game is over or still ongoing.
     */
    private void checkGameStatus() {
        boolean isGameWon = false;
        // logic to see if one team has won a whole row

        // rows first
        for (int row = 0; row < 3; row++) {
            if (board[row][0].isClicked() && board[row][0].whichTeam().equals(board[row][1].whichTeam())
                    && board[row][0].whichTeam().equals(board[row][2].whichTeam()) ) {
                isGameWon = true;
                winningSquares[0] = new Point(row, 0);
                winningSquares[1] = new Point(row, 1);
                winningSquares[2] = new Point(row, 2);
                if (board[row][0].whichTeam().equals("X")) {
                    Xwon = true;
                }
            }
        }

        // then columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col].isClicked() && board[0][col].whichTeam().equals(board[1][col].whichTeam())
                    && board[0][col].whichTeam().equals(board[2][col].whichTeam()) ) {
                isGameWon = true;
                winningSquares[0] = new Point(0, col);
                winningSquares[1] = new Point(1, col);
                winningSquares[2] = new Point(2, col);
                if (board[0][col].whichTeam().equals("X")) {
                    Xwon = true;
                }
            }
        }

        // then diagonals
        if (board[0][0].isClicked() && board[0][0].whichTeam().equals(board[1][1].whichTeam())
                && board[0][0].whichTeam().equals(board[2][2].whichTeam()) ) {
            isGameWon = true;
            winningSquares[0] = new Point(0, 0);
            winningSquares[1] = new Point(1, 1);
            winningSquares[2] = new Point(2, 2);
            if (board[0][0].whichTeam().equals("X")) {
                Xwon = true;
            }
        }
        if (board[0][2].isClicked() && board[0][2].whichTeam().equals(board[1][1].whichTeam())
                && board[0][2].whichTeam().equals(board[2][0].whichTeam()) ) {
            isGameWon = true;
            winningSquares[0] = new Point(0, 2);
            winningSquares[1] = new Point(1, 1);
            winningSquares[2] = new Point(2, 0);
            if (board[0][2].whichTeam().equals("X")) {
                Xwon = true;
            }
        }

        // check to see if game is a stalemate
        if (!isGameWon) {
            boolean isStalemate = areAllSquaresSelected();
            stalemate = isStalemate;
        }

        // if the game was determined to be over, set appropriate flags
        if (isGameWon || stalemate) {
            activeGame = false;
        }
    }

    /**
     * An accessor method which provides game status.
     * @return A boolean flag indicating an active game (true --> active)
     */
    public boolean isGameStillGoing() {
        return activeGame;
    }

    /**
     * An accessor method which returns the Tictactoe_square object and its contained values.
     * @param row The row number of the queried cell
     * @param col The column number of the queried cell
     * @return The cell object (TicTacToe_square object)
     */
    public TicTacToe_square getSquare(int row, int col) {
        return board[row][col];
    }

    /**
     * An accessor method for the view to discover which squares are winning squares
     * @return The Point array which contains the indices for the winning squares
     */
    public Point[] getWinningSquares() {
        return winningSquares;
    }

    /**
     * A helper method to determine if there are any unselected cells on the board. If this returns
     * true, then the board is completely filled, indicating a stalemate if there isn't a declared winner.
     * @return A boolean flag indicating if there are any unselected cells (true --> 0 cells available, false --> 1+ available)
     */
    private boolean areAllSquaresSelected() {
        boolean selected = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!board[row][col].isClicked()) {
                    selected = false;
                }
            }
        }
        return selected;
    }

    /**
     * A helper method to determine if X won the game. If game is over & isStalemate returns false,
     * this method's return value will indicate which player won.
     * @return A boolean flag indicating which player won (true --> X won the game)
     */
    public boolean didXwin() {
        return Xwon;
    }

    /**
     * A helper method to determine if the game was a stalemate. If game is over & this method returns
     * true, then the game is considered to be a stalemate with no winner/loser.
     * @return A boolean flag indicating whether the game was a stalemate (true --> stalemate)
     */
    public boolean isStalemate() {
        return stalemate;
    }

}
