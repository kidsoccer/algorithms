import java.util.*;

public class VietPhapMaze {

    static final String HASH_CHAR = "#";
    static final String DOT_CHAR = ".";
    String[][] maze;
    Set<Point> realVertices;

    public VietPhapMaze(String[][] maze) {
        this.maze = maze;
         realVertices = detectPoints();
        Set<Edge> edges = new HashSet<>();
        Map<Point,Set<Point>> grap = new HashMap<>();
        for (Point r : realVertices
             ) {
            Stack<Point> paths = new Stack<>();
            paths.push(r);
            Set<Point> visited = new HashSet<Point>();
            dfs(r, visited, paths, edges,grap);
        }

        System.out.println("==================");
    }


    private void dfs(Point p, Set<Point> visited,Stack<Point> path,Set<Edge> edges,Map<Point,Set<Point>> grap) {
        visited.add(p);
        if (realVertices.contains(p)){
            path.push(p);
            System.out.println(p);
        }
        List<Point> adjs = getNextMoves(p,visited,edges);
        for (Point temp : adjs
                ) {
            if(!visited.contains(temp)){
                if(realVertices.contains(temp)){
                    //build edge
                    Point toV = temp;
                    Point fromV = path.peek();
                    edges.add(new Edge(fromV, toV, 0));

                    if(grap.containsKey(fromV)){
                        grap.get(fromV).add(toV);
                    } else {
                        Set<Point> adjsList = new HashSet<>();
                        adjsList.add(toV);
                        grap.put(fromV,adjsList);
                    }
                }
                dfs(temp,visited,path,edges,grap);
            }
        }

        //remove point after done all it's children

        if(realVertices.contains(p)) {
            path.pop();
        }
    }

    private Set<Point> detectPoints() {
        Set<Point> points = new HashSet<>();
        int row = maze.length;
        int col = maze[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (maze[i][j].equals(DOT_CHAR)) {
                    Point p = new Point(i, j);
                    int degree = getDegree(p);
                    if(degree % 2 != 0){
                        points.add(p);
                    } else {
                        //System.out.println(p);
                    }
                }
            }
        }
        return points;
    }

    private List<Point> getNextMoves(Point curentP, Set<Point> visited,Set<Edge> edges) {

        int w = maze.length;
        int h = maze[0].length;

        //just allowed to move by vertical or horizontal
        final List<Point> moves = new ArrayList<>();


        //left
        if (curentP.y - 1 >= 0 && !maze[curentP.x][curentP.y - 1].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x, curentP.y - 1);
            //neu chua duoc tham hoac la 1 dinh va chua co canh
            if(!visited.contains(temp) ) {
                moves.add(new Point(curentP.x, curentP.y - 1));
            }
        }

        //up
        if (curentP.x - 1 >= 0  && !maze[curentP.x - 1][curentP.y].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x - 1, curentP.y);
            if(!visited.contains(temp) ) {
                moves.add(new Point(curentP.x - 1, curentP.y));
            }
        }


        //right
        if (curentP.y + 1 < h &&  !maze[curentP.x][curentP.y + 1].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x, curentP.y + 1);
            if(!visited.contains(temp) ) {
                moves.add(new Point(curentP.x, curentP.y + 1));
            }
        }

        //down
        if (curentP.x + 1 < w && !maze[curentP.x + 1][curentP.y].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x + 1, curentP.y);
            if(!visited.contains(temp)  ) {
                moves.add(new Point(curentP.x + 1, curentP.y));
            }
        }

        return moves;
    }

    /**
     * Possible moves : left-up-right-down
     *
     * @param curentP
     * @return
     */
    private int getDegree(Point curentP) {

        int w = maze.length;
        int h = maze[0].length;
        if(curentP.equals(new Point(0,0))){
            System.out.println("df");
        }

        //just allowed to move by vertical or horizontal
        final List<Point> moves = new ArrayList<>();
        //up
        if (curentP.x - 1 >= 0  && !maze[curentP.x - 1][curentP.y].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x - 1, curentP.y));
        }
        //left
        if (curentP.y - 1 >= 0 && !maze[curentP.x][curentP.y - 1].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x, curentP.y - 1));
        }
        //right
        if (curentP.y + 1 < h &&  !maze[curentP.x][curentP.y + 1].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x, curentP.y + 1));
        }
        //down
        if (curentP.x + 1 < w && !maze[curentP.x + 1][curentP.y].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x + 1, curentP.y));
        }
        return moves.size();
    }


static class Edge {
    Point from;
    Point to;
    int w;


    public Edge(Point from, Point to, int w) {
        this.from = from;
        this.to = to;
        this.w = w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return w == edge.w &&
                Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {

        return Objects.hash(from, to, w);
    }
}




static class Point {
    int x;
    int y;

    public Point(int x, int y) {
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

    private Set<Point> expected(){
        Set<Point> expected = new HashSet<>();
        expected.add(new Point(0,1));


        return expected;
    }

    public static void main(String... s) {

        String[][] maze = new String[][]{
                {"#", ".", "#", "#", "#", "#", "#", "#"},
                {"#", ".", "#", "#", "#", "#", "#", "#"},
                {"#", ".", ".", ".", ".", ".", ".", "#"},
                {"#", ".", "#", "#", "#", ".", "#", "#"},
                {"#", ".", ".", "#", "#", ".", "#", "#"},
                {"#", ".", "#", "#", "#", ".", "#", "#"},
                {"#", ".", ".", ".", ".", ".", ".", "."},
                {"#", ".", "#", "#", "#", "#", "#", "#"},
                {"#", ".", "#", "#", "#", ".", "#", "#"},
                {"#", ".", ".", ".", ".", ".", ".", "#"},
                {"#", "#", "#", "#", "#", "#", "#", "#"}
        };

        VietPhapMaze vietPhapMaze = new VietPhapMaze(maze);
        Set<Point> actual = vietPhapMaze.realVertices;
        Set<Point> expected = vietPhapMaze.expected();
        System.out.println(expected.containsAll(actual));
    }
}
