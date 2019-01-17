import java.util.Objects;

public class Sudoku {

    public static void main(String... s) {
        int[][] board = new int[][]
                {
                        {3, 0, 6, 5, 0, 8, 4, 0, 0},
                        {5, 2, 0, 0, 0, 0, 0, 0, 0},
                        {0, 8, 7, 0, 0, 0, 0, 3, 1},
                        {0, 0, 3, 0, 1, 0, 0, 8, 0},
                        {9, 0, 0, 8, 6, 3, 0, 0, 5},
                        {0, 5, 0, 0, 9, 0, 6, 0, 0},
                        {1, 3, 0, 0, 0, 0, 2, 5, 0},
                        {0, 0, 0, 0, 0, 0, 0, 7, 4},
                        {0, 0, 5, 2, 0, 6, 3, 0, 0}
                };
        Game g = new Game(board);
    }

    static class Board {
        final static int EMPTY_CELL = 0;
        int[][] board;
        Board(int[][] board) {
            this.board = board;
        }
        boolean isSafe(int[][] board,
                       Move move,
                       int num) {
            //check row
            for (int d = 0; d < board.length; d++) {
                if (board[move.x][d] == num) {
                    return false;
                }
            }
            //check col
            for (int r = 0; r < board.length; r++) {
                if (board[r][move.y] == num) {
                    return false;
                }
            }

            //check subgrid 3x3
            int sqrt = (int) Math.sqrt(board.length);
            int xStart = move.x - move.x % sqrt;
            int yStart = move.y - move.y % sqrt;

            for (int r = xStart;
                 r < xStart + sqrt; r++) {
                for (int d = yStart;
                     d < yStart + sqrt; d++) {
                    if (board[r][d] == num) {
                        return false;
                    }
                }
            }
            return true;
        }

        boolean isDone() {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == EMPTY_CELL) {
                        return false;
                    }
                }
            }
            return true;
        }

        void printBoard() {
            for (int i = 0; i < board.length; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < board[0].length; j++) {
                    row.append(board[i][j] + "\t");

                }
                System.out.println(row);
            }
        }
    }

    static class Game {
        Board board;

        Game(int[][] board) {
            this.board = new Board(board);
            solve(this.board);
            this.board.printBoard();
        }

        boolean solve(Board board) {
            Move move = getEmptyCell(board.board);
            if (move.equals(new Move(-1, -1))) {
                return true;
            }
            if (board.isDone()) {
                return true;
            }
            for (int n = 1; n <= 9; n++) {
                if (board.isSafe(board.board, move, n)) {
                    board.board[move.x][move.y] = n;
                    if (solve(board)) {
                        return true;
                    } else {
                        //backtracking
                        board.board[move.x][move.y] = 0;
                    }
                }

            }
            // sau khi xong ca 9 so
            return false;
        }

        private Move getEmptyCell(int[][] board) {
            Move move = new Move(-1, -1);
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        move = new Move(i, j);
                        break;
                    }
                }
            }
            return move;
        }
    }


    static class Move {
        int x;
        int y;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return x == move.x &&
                    y == move.y;
        }

        @Override
        public int hashCode() {

            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Move{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}
