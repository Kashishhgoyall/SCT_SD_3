import java.util.Scanner;

public class SudokuSolver {

    private static final int N = 9; // board size 9x9

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] board = new int[N][N];

        System.out.println("Enter the Sudoku grid row by row.");
        System.out.println("Use 0 for empty cells. Each row should contain 9 numbers (0-9) separated by spaces.");
        System.out.println("Example row: 5 3 0 0 7 0 0 0 0");
        System.out.println();

        // Read 9 rows
        for (int r = 0; r < N; r++) {
            System.out.print("Row " + (r + 1) + ": ");
            String line = sc.nextLine().trim();
            // allow multiple spaces - split by whitespace
            String[] tokens = line.split("\\s+");
            if (tokens.length != N) {
                System.out.println("Invalid input: each row must have exactly 9 numbers. Exiting.");
                return;
            }
            for (int c = 0; c < N; c++) {
                try {
                    int val = Integer.parseInt(tokens[c]);
                    if (val < 0 || val > 9) {
                        System.out.println("Invalid number detected (must be 0..9). Exiting.");
                        return;
                    }
                    board[r][c] = val;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid token detected (not an integer). Exiting.");
                    return;
                }
            }
        }

        System.out.println("\nInput board:");
        printBoard(board);

        // Validate initial board for immediate contradictions
        if (!isValidInitialBoard(board)) {
            System.out.println("Initial board has conflicts (duplicate in row/column/box). Exiting.");
            return;
        }

        boolean solved = solveSudoku(board);
        if (solved) {
            System.out.println("\nSolved board:");
            printBoard(board);
        } else {
            System.out.println("\nNo solution exists for the given Sudoku.");
        }
    }

    // Backtracking solver
    private static boolean solveSudoku(int[][] board) {
        int[] emptyPos = findEmpty(board);
        if (emptyPos == null) {
            // no empty cell => solved
            return true;
        }
        int row = emptyPos[0];
        int col = emptyPos[1];

        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board)) {
                    return true;
                }
                // backtrack
                board[row][col] = 0;
            }
        }
        return false; // trigger backtracking
    }

    // Find an empty cell (value 0). Returns null if none.
    private static int[] findEmpty(int[][] board) {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] == 0) {
                    return new int[] { r, c };
                }
            }
        }
        return null;
    }

    // Check whether placing num at (row, col) is valid
    private static boolean isSafe(int[][] board, int row, int col, int num) {
        // check row
        for (int c = 0; c < N; c++) {
            if (board[row][c] == num) return false;
        }
        // check column
        for (int r = 0; r < N; r++) {
            if (board[r][col] == num) return false;
        }
        // check 3x3 box
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if (board[r][c] == num) return false;
            }
        }
        return true;
    }

    // Basic validation of initial board: ensure no duplicate non-zero values in row/col/box
    private static boolean isValidInitialBoard(int[][] board) {
        // check rows and columns
        for (int i = 0; i < N; i++) {
            boolean[] seenRow = new boolean[N + 1];
            boolean[] seenCol = new boolean[N + 1];
            for (int j = 0; j < N; j++) {
                int rv = board[i][j];
                if (rv != 0) {
                    if (seenRow[rv]) return false;
                    seenRow[rv] = true;
                }
                int cv = board[j][i];
                if (cv != 0) {
                    if (seenCol[cv]) return false;
                    seenCol[cv] = true;
                }
            }
        }
        // check 3x3 boxes
        for (int boxRow = 0; boxRow < N; boxRow += 3) {
            for (int boxCol = 0; boxCol < N; boxCol += 3) {
                boolean[] seen = new boolean[N + 1];
                for (int r = boxRow; r < boxRow + 3; r++) {
                    for (int c = boxCol; c < boxCol + 3; c++) {
                        int v = board[r][c];
                        if (v != 0) {
                            if (seen[v]) return false;
                            seen[v] = true;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Nicely print the board
    // Nicely print the board with better formatting
    private static void printBoard(int[][] board) {
    System.out.println("-------------------------");
    for (int r = 0; r < N; r++) {
        for (int c = 0; c < N; c++) {
            if (c % 3 == 0) System.out.print("| ");
            int v = board[r][c];
            System.out.print((v == 0 ? ". " : v + " "));
        }
        System.out.println("|");
        if ((r + 1) % 3 == 0) {
            System.out.println("-------------------------");
        }
    }
}

}
