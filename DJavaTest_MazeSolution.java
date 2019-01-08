import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
public class ThanhTour {
    private static List<P> POSSIBLE_NEXT = new ArrayList<P>(){{
        add(new P(0, -1));
        add(new P(0, 1));
        add(new P(-1, 0));
        add(new P(1, 0));
    }} ;
    private static int COUNT = 0;
    public static void main(String...args){
        int[][] matrix = new int[][]{
                {0,0,0,0,2},
                {0,0,0,0,0},
                {0,0,0,3,1}
        };
        printAllWays(matrix, new P(0, 4), new P(2, 3));
    }
    private static void printAllWays(int[][] matrix, P start, P end) {
        int numberSteps = 0;
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                numberSteps += matrix[i][j] == 0 ? 1 : 0;
        int[][] visited = new int[matrix.length][matrix[0].length];
        hasPath(matrix, visited, start, end, numberSteps, new LinkedList<>());
    }
    private static boolean hasPath(int[][] matrix, int[][] visited, P current, P end, int numberSteps, LinkedList<P> path) {
        if (numberSteps == 0 && oneStepToNext(current, end)){
            printPath(path);
            return true;
        }
        List<P> possibleSteps = possibleStep(matrix, visited, current);
        if (possibleSteps.isEmpty())
            return false;
        visited[current.x][current.y] = 1;
        for (P possibleStep : possibleSteps){
            path.add(possibleStep);
            hasPath(matrix, visited, possibleStep, end, numberSteps - 1, path);
            path.removeLast();
        }
        visited[current.x][current.y] = 0;
        return true;
    }
    private static boolean oneStepToNext(P from, P to){
        if (to.x == from.x)
            return to.y == from.y + 1 || to.y == from.y - 1;
        if (to.y == from.y)
            return to.x == from.x + 1 || to.x == from.x - 1;
        return false;
    }
    private static List<P> possibleStep(int[][] matrix, int[][] visited, P current){
        List<P> possibles = new ArrayList<>();
        for (P next : POSSIBLE_NEXT){
            int nextX = current.x + next.x;
            int nextY = current.y + next.y;
            if (getValue(matrix, nextX, nextY) == 0 && getValue(visited, nextX, nextY) != 1)
                possibles.add(new P(nextX, nextY));
        }
        return possibles;
    }
    private static int getValue(int[][] matrix, int x, int y){
        if (x >= matrix.length || y >= matrix[0].length) return -1;
        if (x < 0 || y < 0) return -1;
        return matrix[x][y];
    }
    private static void printPath(LinkedList<P> path){
        COUNT++;
        System.out.println("The " + COUNT + " solution");
        for (P item : path)
            System.out.print(item.toString() + " ");
        System.out.println();
    }
}
class P {
    int x, y;
    P(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "P{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        P p = (P) o;
        return x == p.x &&
                y == p.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
