import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
public class VietPhapMaze_Khoa {
    public static void main(String... args) {
        int[][] arr = new int[][]{
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 0, 0, 0, 1, 0, 0},
                {0, 1, 1, 0, 0, 1, 0, 0},
                {0, 1, 0, 0, 0, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        exercise(arr);
    }
    private static void exercise(int[][] arr) {
        int[][] visited = new int[arr.length][arr[0].length];
        Set<Edge> edges = new HashSet<>();
        dfs(arr, visited, edges, new Stack<>(), 0, 1, 0);
        int i = 1;
        for (Edge edge : edges)
            System.out.println(i++ + " " + edge);
    }
    private static void dfs(int[][] arr, int[][] visited, Set<Edge> edges, Stack<Point> stack, int x, int y, int weigh) {
        if (getValue(arr, x, y) == 0) return;
        boolean isVertex = isVertex(arr, x, y);
        if (!isVertex && visited[x][y] == 1) return;
        if (isVertex) {
            Point end = new Point(x, y);
            if (!stack.isEmpty()) {
                Point start = stack.peek();
                Edge edge = new Edge(start, end, weigh);
                if (edges.contains(edge) || start.equals(end))
                    return;
                edges.add(edge);
                edges.add(edge.revert());
            }
            stack.push(end);
        }
        visited[x][y] = 1;
        int w = isVertex ? 1 : weigh + 1;
        dfs(arr, visited, edges, stack, x, y + 1, w);
        dfs(arr, visited, edges, stack, x + 1, y, w);
        dfs(arr, visited, edges, stack, x - 1, y, w);
        dfs(arr, visited, edges, stack, x, y - 1, w);
        if (isVertex && !stack.isEmpty())
            stack.pop();
    }
    private static boolean isVertex(int[][] arr, int x, int y) {
        return (getValue(arr, x, y + 1) + getValue(arr, x, y - 1) + getValue(arr, x + 1, y) + getValue(arr, x - 1, y)) % 2 != 0;
    }
    private static int getValue(int[][] arr, int x, int y) {
        if (x >= arr.length || x < 0) return 0;
        if (y >= arr[0].length || y < 0) return 0;
        return arr[x][y];
    }
}
class Point {
    private int x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
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
}
class Edge {
    Point start, end;
    int distance;
    Edge(Point start, Point end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }
    Edge revert() {
        return new Edge(end, start, distance);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(start, edge.start) &&
                Objects.equals(end, edge.end);
    }
    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
    @Override
    public String toString() {
        return "Edge{" + "start=" + start + ", end=" + end + ", distance=" + distance + '}';
    }
}
