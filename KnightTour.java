import java.util.HashSet;
import java.util.Set;

public class KnightTour {
    public static void main(String ...s){
        int [][] chessBoard = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}
        };

        Game g = new Game(new Board(chessBoard));
    }


   static class Board{
        int [][] board;
       int xMove[] = {2, 1, -1, -2, -2, -1, 1, 2};
       int yMove[] = {1, 2, 2, 1, -1, -2, -2, -1};

       Board(int [][] board){
            this.board = board;
        }

        Set<Point> getPossibleMoves(Point p){
            Set<Point> points = new HashSet<>();
            for (int k =0; k < 8;k++
                 ) {
                int x = p.x +xMove[k];
                int y = p.y+yMove[k];
                if( x>=0 && x < board.length && y >=0 && y<board.length && board[x][y] == 0) {
                    points.add(new Point(x, y));
                }
            }
            return points;
        }

        void printBoard(){
            for (int i =0; i < board.length;i ++){
                StringBuilder row = new StringBuilder();
                for (int j =0; j< board[0].length;j++){
                    row.append(board[i][j] + "\t");
                }
                System.out.println(row);
            }
        }

       boolean isDone(){
           for (int i =0; i < board.length;i ++){
               for (int j =0; j< board[0].length;j++){
                   board[i][j] = 0;
                   return false;
               }
           }
         return true;
       }
    }

    static class Game {
        Board board;
        int xMove[] = {2, 1, -1, -2, -2, -1, 1, 2};
        int yMove[] = {1, 2, 2, 1, -1, -2, -2, -1};

        int  solutionCount = 0;
        Game(Board board){
            this.board = board;

            Point startPoint = new Point(0,0);
            board.board[0][0] =1;
            solve(2,startPoint);
            //this.board.printBoard();
        }

        void solveFindAllSolution(int stepCount,Point p){

            // board.board[p.x][p.y] = stepCount;
            //end game condition
            //System.out.println("===================");
            //board.printBoard();
            if(stepCount == 64){
                //
                solutionCount++;
                System.out.println("Solution: " +solutionCount);
                board.printBoard();
                // looking for another solution
                board.board[p.x][p.y] = 0;
                return;
            }


            /*List<Point> possibleMoves = new ArrayList<>(board.getPossibleMoves(p));
            if(possibleMoves.isEmpty()){
                return false;
            }*/

            for (int k =0; k<8;k++
                    ) {
                int x = p.x + xMove[k];
                int y = p.y + yMove[k];

                if( x>=0 && x < board.board.length && y >=0 && y<board.board.length && board.board[x][y] == 0){
                    board.board[x][y] = stepCount;
                    /*if (solve(stepCount + 1, new Point(x,y))) {
                        return true;
                    } else {
                        //backtracking
                        board.board[x][y] = 0;
                        //return false;
                    }*/
                    solveFindAllSolution(stepCount+1,new Point(x,y));
                }
                //}
            }

            board.board[p.x][p.y] = 0;


        }




        boolean solve(int stepCount,Point p){

           // board.board[p.x][p.y] = stepCount;
            //end game condition
            //System.out.println("===================");
            //board.printBoard();
            if(stepCount == 64){
                board.printBoard();
                return true;
            }

            /*List<Point> possibleMoves = new ArrayList<>(board.getPossibleMoves(p));
            if(possibleMoves.isEmpty()){
                return false;
            }*/

            for (int k =0; k<8;k++
                 ) {
                int x = p.x + xMove[k];
                int y = p.y + yMove[k];

                if( x>=0 && x < board.board.length && y >=0 && y<board.board.length && board.board[x][y] == 0){
                    board.board[x][y] = stepCount;
                    if (solve(stepCount + 1, new Point(x,y))) {
                        return true;
                    } else {
                        //backtracking
                        board.board[x][y] = 0;
                        //return false;
                    }
                    }
                //}
            }

            return false;
        }


    }

   static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
