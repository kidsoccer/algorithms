import java.util.*;

public class VietPhapMaze {

    static final String HASH_CHAR = "#";
    static final String DOT_CHAR = ".";
    String[][] maze;
    Set<Point> realVertices;


    public VietPhapMaze(String[][] maze) {
        this.maze = maze;
        //find the vertices
        realVertices = detectPoints();
        //link vertices together - make edges
        Map<Point, Set<Point>> adjacentList = linkVertices();
        Set<Edge> edges = updateWeight(adjacentList);
        System.out.println("==================");
    }

    private Set<Edge> updateWeight(Map<Point, Set<Point>> adjacentList) {

        Set<Edge> edges = new HashSet<>();
        adjacentList.entrySet().stream().forEach(
                e -> {
                    Set<Point> adj = e.getValue();
                    for (Point toV : adj
                            ) {
                        int count = 0;
                        Edge edge = new Edge(e.getKey(), toV, count);
                        PriorityQueue<Integer> allPathLengh = new PriorityQueue<>();
                        searchAllPaths(e.getKey(), toV, new HashSet<>(), new ArrayList<>(), allPathLengh);
                        if (!allPathLengh.isEmpty()) {
                            edge.setW(allPathLengh.peek());
                        }
                        edges.add(edge);
                    }
                }
        );

        return edges;
    }

    private Map<Point, Set<Point>> linkVertices() {
        Map<Point, Set<Point>> grap = new HashMap<>();
        for (Point r : realVertices
                ) {
            Stack<Point> paths = new Stack<>();
            paths.push(r);
            Set<Point> visited = new HashSet<Point>();
            dfs(r, visited, paths, grap);
        }

        return grap;
    }


    // A recursive function to print
    // all paths from 'u' to 'd'.
    // isVisited[] keeps track of
    // vertices in current path.
    // localPathList<> stores actual
    // vertices in the current path

    private void searchAllPaths(Point u, Point d,
                                Set<Point> isVisited,
                                List<Point> localPathList, PriorityQueue<Integer> allPathLengh) {

        // Mark the current node
        isVisited.add(u);

        if (u.equals(d)) {
            System.out.println(localPathList);
            allPathLengh.add(localPathList.size());
            // if match found then no need to traverse more till depth
            isVisited.remove(u);
            return;
        }

        // Recur for all the vertices
        // adjacent to current vertex
        List<Point> moves = getNextMoves(u, isVisited);
        for (Point i : moves) {
            if (!isVisited.contains(i)) {
                // store current node
                // in path[]
                localPathList.add(i);
                searchAllPaths(i, d, isVisited, localPathList, allPathLengh);

                // remove current node
                // in path[]
                localPathList.remove(i);
            }
        }

        // Mark the current node
        isVisited.remove(u);
    }

    private void dfs(Point p, Set<Point> visited, Stack<Point> path, Map<Point, Set<Point>> grap) {
        visited.add(p);
        if (realVertices.contains(p)) {
            path.push(p);
        }
        List<Point> adjs = getNextMoves(p, visited);
        for (Point temp : adjs
                ) {
            if (!visited.contains(temp)) {
                if (realVertices.contains(temp)) {
                    //build edge
                    Point toV = temp;
                    Point fromV = path.peek();
                    if (grap.containsKey(fromV)) {
                        grap.get(fromV).add(toV);
                    } else {
                        Set<Point> adjsList = new HashSet<>();
                        adjsList.add(toV);
                        grap.put(fromV, adjsList);
                    }
                }
                dfs(temp, visited, path, grap);
            }
        }
        //remove point after done all it's children
        if (realVertices.contains(p)) {
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
                    if (degree % 2 != 0) {
                        points.add(p);
                    } else {
                        //System.out.println(p);
                    }
                }
            }
        }
        return points;
    }

    private List<Point> getNextMoves(Point curentP, Set<Point> visited) {

        int w = maze.length;
        int h = maze[0].length;

        //just allowed to move by vertical or horizontal
        final List<Point> moves = new ArrayList<>();


        //left
        if (curentP.y - 1 >= 0 && !maze[curentP.x][curentP.y - 1].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x, curentP.y - 1);
            //neu chua duoc tham hoac la 1 dinh va chua co canh
            if (!visited.contains(temp)) {
                moves.add(new Point(curentP.x, curentP.y - 1));
            }
        }

        //up
        if (curentP.x - 1 >= 0 && !maze[curentP.x - 1][curentP.y].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x - 1, curentP.y);
            if (!visited.contains(temp)) {
                moves.add(new Point(curentP.x - 1, curentP.y));
            }
        }


        //right
        if (curentP.y + 1 < h && !maze[curentP.x][curentP.y + 1].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x, curentP.y + 1);
            if (!visited.contains(temp)) {
                moves.add(new Point(curentP.x, curentP.y + 1));
            }
        }

        //down
        if (curentP.x + 1 < w && !maze[curentP.x + 1][curentP.y].equals(HASH_CHAR)) {
            Point temp = new Point(curentP.x + 1, curentP.y);
            if (!visited.contains(temp)) {
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
        if (curentP.equals(new Point(0, 0))) {
            System.out.println("df");
        }

        //just allowed to move by vertical or horizontal
        final List<Point> moves = new ArrayList<>();
        //up
        if (curentP.x - 1 >= 0 && !maze[curentP.x - 1][curentP.y].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x - 1, curentP.y));
        }
        //left
        if (curentP.y - 1 >= 0 && !maze[curentP.x][curentP.y - 1].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x, curentP.y - 1));
        }
        //right
        if (curentP.y + 1 < h && !maze[curentP.x][curentP.y + 1].equals(HASH_CHAR)) {
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

        public Point getFrom() {
            return from;
        }

        public void setFrom(Point from) {
            this.from = from;
        }

        public Point getTo() {
            return to;
        }

        public void setTo(Point to) {
            this.to = to;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

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
    }
}
