import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Given a maze , 0: movable, 1: blocked , 2 start point, 3 destination point
 * Find all the paths from source to destination that step on every 0 point
 */

/**
 * You are in a maze, and you need to find all possible path from an entrance to an exit. Here are the constraints:
 * ● The maze is represented by a 2D grid.
 * ● Spots that you can step on are represented by a 0.
 * ● Pits that you will fall into (aka spots that you cannot step on) are represented by a 1.
 * ● The entrance is represented by a 2.
 * ● The exit is represented by a 3.
 * ● Each path can only have two endpoints; entrance and exit. You cannot use the entrance or exit more than
 * once for each path.
 * ● You have to step on every spot exactly once.
 * ● You can only move like a King in chess (horizontally or vertically but not diagonally)
 * Here is an example of the maze:
 * 2 0 0 0 0
 * 0 0 0 0 0
 * 0 0 0 3 1
 * Possible paths are:
 * The answer to the above example is 4.
 * Your program should read from standard input with a series of integers with whitespace as delimiter. The first two
 * integer represents the width and height of the maze. It will then be followed by width*height more integers. Your
 * output should be an integer which shows the total number of possible path. Marks will be given for efficiency as well.
 * 7 7
 * 2 0 0 0 0 0 0
 * 0 0 0 0 0 0 0
 * 0 0 0 0 0 0 0
 * 0 0 0 0 0 0 0
 * 0 0 0 0 0 0 0
 * 0 0 0 0 0 0 0
 * 0 0 0 0 3 1 1
 * 4. What is the total possibiities for the above maze?
 */
public class MazeSolution {

    int [][] maze ;
    boolean [][] visited;
    LinkedList<Point> path;
    Point source;
    Point destination;
    int count ;

    public MazeSolution(int [][] maze,Point source,Point destination){
        this.maze = maze;
        this.source = source;
        this.destination = destination;
        visited = new boolean[maze.length][maze[0].length];
        for (int i =0; i<maze.length;i++){
            for (int j =0;j<maze[0].length;j++){
                visited[i][j] = false;
            }
        }
        path = new LinkedList<>();
    }

    /**
     * Possible moves : left-up-right-down
     * @param curentP
     * @return
     */
    private List<Point> getNextMoves(Point curentP){

        int w = maze.length;
        int h = maze[0].length;

        //just allowed to move by vertical or horizontal
        final List<Point> moves = new ArrayList<>();
        //up
        if(curentP.x -1 >= 0 && visited[curentP.x - 1][curentP.y] == false && maze[curentP.x - 1][curentP.y] != 1) {
            moves.add(new Point(curentP.x - 1, curentP.y));
        }
        //left
        if(curentP.y -1 >= 0 && visited[curentP.x][curentP.y-1] == false && maze[curentP.x][curentP.y-1] != 1) {
            moves.add(new Point(curentP.x, curentP.y - 1));
        }
        //right
        if(curentP.y +1 < h && visited[curentP.x][curentP.y +1] == false && maze[curentP.x][curentP.y +1] != 1 ) {
            moves.add(new Point(curentP.x, curentP.y + 1));
        }
        //down
        if(curentP.x+1 < w && visited[curentP.x +1][curentP.y] == false &&  maze[curentP.x +1][curentP.y] != 1) {
            moves.add(new Point(curentP.x + 1, curentP.y));
        }
        return moves;
    }


    public void solve(Point current){

        //base condition to stop the recursion and print the found path
        //if reach to destination and all the zero are visited
        if(current.equals(destination) && allVisited()){
            count ++;
            //print path and return
            System.out.println("Solution: " + count);
            for (Point p: path
                 ) {
                System.out.println(p.toString());

            }
            return;
        }

        //get next moves
        List<Point> nextMoves = getNextMoves(current);
        if(nextMoves.isEmpty()){
            //cannot move any more
            return;
        }
        //marking as visited
        visited[current.x][current.y] = true;
        for (Point move: nextMoves
             ) {
            path.add(move);
            solve(move);
            path.removeLast();//remove the last
        }
        visited[current.x][current.y] = false;
    }

    private boolean allVisited(){
        for (int i =0; i<maze.length;i++){
            for (int j =0;j<maze[0].length;j++){
                if(maze[i][j] == 0 && visited[i][j] == false){
                    return false;
                }
            }
        }

        return true;
    }


    static class Point{
        int x;
        int y;
        public Point(int x,int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }


    public static void main(String ...s){

        int[][] maze = new int[][]{
                {0,0,0,0,2},
                {0,0,0,0,0},
                {0,0,0,3,1}
        };

        Point source = new Point(0,4);
        Point destination = new Point(2,3);
        MazeSolution mazeSolution = new MazeSolution(maze,source,destination);
        mazeSolution.solve(source);

    }

}
