import java.util.HashSet;
import java.util.Set;

public class Demo {

    public static void main(String ...s){
        String[][] gameBoard = new String[][]{
                {"o", "x", "_"},
                {"o", "_", "_"},
                {"_", "_", "_"}
        };

        Game g = new Game(gameBoard);
        g.bestMoveX();
    }


    static class Board {
        static final String HUMMAN_PLAYER = "x"; //min player
        static final String AI_PLAYER = "o"; //max player

        static final String UN_TOUCHED = "_"; //max player
        String gameBorad [][] ;
        Board(String [][] board){
            this.gameBorad= board;
        }

        boolean gameOver() {

            //rows
            for (int row = 0; row < gameBorad.length; row++) {
                if (!gameBorad[row][0].equals(UN_TOUCHED) && gameBorad[row][0].equals(gameBorad[row][1]) && gameBorad[row][1].equals(gameBorad[row][2])) {
                    return true;
                }
            }
            //cols
            for (int col = 0; col < gameBorad.length; col++) {
                if (!gameBorad[0][col].equals(UN_TOUCHED) && gameBorad[0][col].equals(gameBorad[1][col]) && gameBorad[1][col].equals(gameBorad[2][col])) {
                    return true;
                }
            }

            //diagonal
            if (!gameBorad[0][0].equals(UN_TOUCHED) && gameBorad[0][0].equals(gameBorad[1][1]) && gameBorad[1][1].equals(gameBorad[2][2])) {
                return true;
            }

            //oposite diagonal
            if (!gameBorad[2][0].equals(UN_TOUCHED) && gameBorad[2][0].equals(gameBorad[1][1]) && gameBorad[1][1].equals(gameBorad[0][2])) {
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
                        test.add(new Move(i,j));

            return test;
        }

        int mimimax(boolean isMaximizerX,int depth){

            boolean isGameOVer = gameOver();
            if(isGameOVer && isMaximizerX){
                return 10;
            } else if(isGameOVer && ! isMaximizerX){
                return  -10;
            }
            Set<Move> ava = avaiableList();
            if(ava.isEmpty()){
                return 0;
            }

            if(isMaximizerX){
                double max = Double.NEGATIVE_INFINITY;
                for (Move m: ava
                     ) {
                    if(gameBorad[m.x][m.y].equals(UN_TOUCHED)) {
                        //max go
                        gameBorad[m.x][m.y] = HUMMAN_PLAYER;
                        int score = mimimax(!isMaximizerX, depth+1);
                        if (score > max) {
                            max = score;
                        }
                        gameBorad[m.x][m.y] = UN_TOUCHED;
                    }
                }
                return (int) max;
            } else {
                double min = Double.POSITIVE_INFINITY;
                for (Move m: ava
                        ) {
                    if(gameBorad[m.x][m.y].equals(UN_TOUCHED)) {
                        //max go
                        gameBorad[m.x][m.y] = AI_PLAYER;
                        int score = mimimax(!isMaximizerX, depth+1);
                        if (score < min) {
                            min = score;
                        }
                        gameBorad[m.x][m.y] = UN_TOUCHED;
                    }
                }
                return (int) min;
            }
        }


    }


    static class Game{

        Board board;

        Game(String [][] data){
            board = new Board(data);
        }


        Move bestMoveX(){
            Set<Move> avaiable = board.avaiableList();
            if(avaiable.isEmpty()){
                System.out.println("Game over , game draw");
                return null;
            }
            Move move = new Move(-1,-1);
            double max = Double.NEGATIVE_INFINITY;
            for (Move m: avaiable
                 ) {
            int score =    board.mimimax(true,0);
            if(score > max){
                max = score;
                move = new Move(m.x,m.y);
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
        public String toString() {
            return "Move{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
