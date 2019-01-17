import java.util.HashSet;
import java.util.Set;

/**
 * Minimax Algorithm in Game Theory
 * Minimax is a kind of backtracking algorithm that is used in decision making and game theory to find the optimal move for a player, assuming that your opponent also plays optimally. It is widely used in two player turn-based games such as Tic-Tac-Toe, Backgammon, Mancala, Chess, etc.
 *
 * In Minimax the two players are called maximizer and minimizer.
 * The maximizer tries to get the highest score possible while the minimizer tries to do the opposite and get the lowest score possible.
 *
 * Every board state has a value associated with it.
 * In a given state if the maximizer has upper hand then, the score of the board will tend to be some positive value.
 * If the minimizer has the upper hand in that board state then it will tend to be some negative value.
 * The values of the board are calculated by some heuristics which are unique for every type of game.
 */
public class MiniMaxTicTacToeGame {

    public static void main(String... s) {
        String[][] gameBoard = new String[][]{
                {"o", "x", "x"},
                {"o", "_", "o"},
                {"x", "_", "_"}
        };
        Game g = new Game(gameBoard);
        //computer turn
        Move bestMove = g.bestMoveAI();
        g.board.gameBorad[bestMove.x][bestMove.y] =Board.AI_PLAYER;
        //check game state first
        g.checkGameState();

        //TODO:
        //player turn
        //g.bestMoveHuman();
    }


    static class Board {
        static final String AI_PLAYER = "x"; //min player
        static final String HUMMAN_PLAYER = "o"; //max player

        static final String UN_TOUCHED = "_"; //max player
        String gameBorad[][];
        Board(String[][] board) {
            this.gameBorad = board;
        }
        boolean gameOver(String player) {
            //rows
            for (int row = 0; row < gameBorad.length; row++) {
                if (!gameBorad[row][0].equals(UN_TOUCHED) && gameBorad[row][0].equals(player) && gameBorad[row][0].equals(gameBorad[row][1]) && gameBorad[row][1].equals(gameBorad[row][2])) {
                    return true;
                }
            }
            //cols
            for (int col = 0; col < gameBorad.length; col++) {
                if (!gameBorad[0][col].equals(UN_TOUCHED) && gameBorad[0][col].equals(player) && gameBorad[0][col].equals(gameBorad[1][col]) && gameBorad[1][col].equals(gameBorad[2][col])) {
                    return true;
                }
            }
            //diagonal
            if (!gameBorad[0][0].equals(UN_TOUCHED) && gameBorad[0][0].equals(player) && gameBorad[0][0].equals(gameBorad[1][1]) && gameBorad[1][1].equals(gameBorad[2][2])) {
                return true;
            }
            //oposite diagonal
            if (!gameBorad[2][0].equals(UN_TOUCHED) && gameBorad[2][0].equals(player) && gameBorad[2][0].equals(gameBorad[1][1]) && gameBorad[1][1].equals(gameBorad[0][2])) {
                return true;
            }
            //game draw
            return false;
        }

        Set<Move> avaiableList() {
            Set<Move> test = new HashSet<>();
            for (int i = 0; i < gameBorad.length; i++)
                for (int j = 0; j < gameBorad[0].length; j++)
                    if (gameBorad[i][j].equals(UN_TOUCHED))
                        test.add(new Move(i, j));
            return test;
        }

        int mimimax(String player, Set<Move> result, int depth) {
            boolean aiWin = gameOver(AI_PLAYER);
            //maximizer
            if (aiWin) {
                return 10;
            }
            boolean humanWin = gameOver(HUMMAN_PLAYER);
            //minimizer
            if (humanWin) {
                return -10;
            }
            Set<Move> ava = avaiableList();
            if (ava.isEmpty()) {
                return 0;
            }

            for (Move m : ava) {
                if (gameBorad[m.x][m.y].equals(UN_TOUCHED)) {
                    Move bestMove = new Move(m);
                    gameBorad[m.x][m.y] = player;
                    if(AI_PLAYER.equals(player)){
                        int score = mimimax(HUMMAN_PLAYER,result,depth+1);
                        bestMove.setScore(score);
                    } else if(HUMMAN_PLAYER.equals(player)){
                       int score= mimimax(AI_PLAYER,result,depth+1);
                        bestMove.setScore(score);
                    }
                    //reset
                    gameBorad[m.x][m.y] = UN_TOUCHED;
                    result.add(bestMove);
                }
            }
            //end the avaiable list
            return 0;
        }
    }

    static class Game {
        Board board;
        Game(String[][] data) {
            board = new Board(data);
        }

        void checkGameState(){
            boolean aiWin = board.gameOver(Board.AI_PLAYER);
            //maximizer
            if (aiWin) {
                System.out.println("AI won");
                return;
            }
            boolean humanWin = board.gameOver(Board.HUMMAN_PLAYER);
            //minimizer
            if (humanWin) {
                System.out.println("Player won");
                return;
            }
            Set<Move> ava = board.avaiableList();
            if (ava.isEmpty()) {
                System.out.println("Game draw");
                return;
            }

        }

        Move bestMoveAI() {
            Set<Move> results = new HashSet<>();
            board.mimimax(Board.AI_PLAYER,results,0);
            double bestScore = Double.NEGATIVE_INFINITY;
            Move bestMove = new Move(-1,-1);
            for (Move p : results
                 ) {
                if(p.getScore() > bestScore){
                    bestScore = p.getScore();
                    bestMove = p;
                }
            }
            System.out.println("The optimal move for AI-Maximizer :" + bestMove.toString());
            return bestMove;
        }

        Move bestMoveHuman() {
            Set<Move> results = new HashSet<>();
            board.mimimax(Board.HUMMAN_PLAYER,results,0);
            double bestScore = Double.POSITIVE_INFINITY;
            Move bestMove = new Move(-1,-1);
            for (Move p : results
                    ) {
                if(p.getScore() < bestScore){
                    bestScore = p.getScore();
                    bestMove = p;
                }
            }
            System.out.println("The optimal move for player-Minimizer :" + bestMove.toString());
            return bestMove;
        }
    }

    static class Move {
        int x;
        int y;
        int score;

        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Move(Move other) {
            this.x = other.x;
            this.y = other.y;
            this.score = other.score;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "x=" + x +
                    ", y=" + y +
                    ", score=" + score +
                    '}';
        }
    }
}
