import javafx.util.Pair;

import java.util.*;
public class VietPhapMaze {

    static final String HASH_CHAR = "#";
    static final String DOT_CHAR = ".";
    String [][] maze ;
    boolean [][] visited;
    //LinkedList<Point> path;
    PotentialVertex source;
    PotentialVertex destination;
    int count ;
    Set<PotentialVertex> potentialVertices = new HashSet<>();

    //xem tat ca dau . cung la 1 vertex tren do thi

    //TODO: chua phai
    Map<Point,List<Point>> adjacents ;

    Set<Point> realVertices = new HashSet<>();



    public VietPhapMaze(String [][] maze, PotentialVertex source, PotentialVertex destination){
        this.maze = maze;
        this.source = source;
        this.destination = destination;
        visited = new boolean[maze.length][maze[0].length];
        for (int i =0; i<maze.length;i++){
            for (int j =0;j<maze[0].length;j++){
                visited[i][j] = false;
            }
        }
        //path = new LinkedList<>();
        adjacents = new HashMap<>();

        //all dots are vertices
        buildPotentialVertices();

        //link dots to build edges
        linkVertices();

        //weight
        //for (Point p : realVertices
         //    ) {
        Point start = new Point(0,1);
            LinkedList<Point> path = new LinkedList<>();
            List<Pair<Point,Point>> parent = new ArrayList<>();
            parent.add(new Pair<>(start,null));
            Stack<Point> stack = new Stack<>();
            stack.push(start);
            List<Edge> edges = new ArrayList<>();
            dfs(start,new HashSet<Point>(),stack,edges);
            System.out.println(path);

        //}

    }


    private void linkVertices(){
        for (PotentialVertex potentialVertex: potentialVertices
                ) {
            List<PotentialVertex> ajd = new ArrayList<>();
            int x = potentialVertex.point.x;
            int y = potentialVertex.point.y;
            //left v
            Optional<PotentialVertex> temp = findAjdVertex(x,y-1);
            if(temp.isPresent()){
                ajd.add(temp.get());
            }

            //up
            temp = findAjdVertex(x -1,y);
            if(temp.isPresent()){
                ajd.add(temp.get());
            }

            //right
            temp = findAjdVertex(x,y+1);
            if(temp.isPresent()){
                ajd.add(temp.get());
            }

            //down
            temp = findAjdVertex(x+1,y);
            if(temp.isPresent()){
                ajd.add(temp.get());
            }

            //if degree/b?c is odd then
            if(ajd.size() % 2 != 0) {
                realVertices.add(potentialVertex.point);
            }

        }
    }



    private Optional<PotentialVertex> findAjdVertex(int x,int y){
        for (PotentialVertex p: potentialVertices
                ) {
            if(p.point.x == x && p.point.y == y){
                return Optional.of(p);
            }
        }

        return Optional.empty();
    }

    private void buildPotentialVertices() {

        int row = maze.length;
        int col = maze[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if(maze[i][j].equals(DOT_CHAR)) {
                    Point p = new Point(i, j);
                    PotentialVertex potentialVertex = new PotentialVertex(p);
                    List<Point> nextMoves = getNextMoves(p);
                    potentialVertex.setOneMoveDirectionCount(nextMoves.size());
                    potentialVertices.add(potentialVertex);
                }
            }
        }
    }



    private void dfs(Point p, Set<Point> visited,Stack<Point> path,List<Edge> edges){

        visited.add(p);
        //store the path
        //path.add(p);


        List<Point> adjs = getNextMoves(p);
        if(adjs.isEmpty()){
            if(realVertices.contains(p)){
                path.pop();

            }
        }
        for (Point temp : adjs
             ) {
            if(!visited.contains(temp)){
                /*if(realVertices.contains(temp)) {
                    path.add(new Pair<>(temp, p));
                }*/


                if(realVertices.contains(temp)){
                    //build edge
                    Point toV = temp;
                    int count = 0;
                    while (!path.isEmpty()){
                        Point from = path.pop();
                        if(!realVertices.contains(from)) {
                            count++;
                        } else{
                            edges.add(new Edge(from,toV,count));
                            //path.push(temp);
                        }
                    }
                   // path.push(temp);



                } //else{
                    path.push(temp);
                //}

                dfs(temp,visited,path,edges);
        }
        }
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
        if(curentP.x -1 >= 0 && visited[curentP.x - 1][curentP.y] == false && !maze[curentP.x - 1][curentP.y].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x - 1, curentP.y));
        }
        //left
        if(curentP.y -1 >= 0 && visited[curentP.x][curentP.y-1] == false && !maze[curentP.x][curentP.y-1].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x, curentP.y - 1));
        }
        //right
        if(curentP.y +1 < h && visited[curentP.x][curentP.y +1] == false && !maze[curentP.x][curentP.y +1].equals(HASH_CHAR) ) {
            moves.add(new Point(curentP.x, curentP.y + 1));
        }
        //down
        if(curentP.x+1 < w && visited[curentP.x +1][curentP.y] == false &&  !maze[curentP.x +1][curentP.y].equals(HASH_CHAR)) {
            moves.add(new Point(curentP.x + 1, curentP.y));
        }
        return moves;
    }




    static class Edge{
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


    //moi dau cham la 1 potential vertex
    static class PotentialVertex {
        Point point;
        //huong di chuyen co the cua diem hien tai
        int oneMoveDirectionCount;

        //so huong di chuyen la canh le
        public  boolean isVertex(){
            return oneMoveDirectionCount%2 !=0;
        }

        public PotentialVertex(Point point) {
            this.point = point;
        }

        public int getOneMoveDirectionCount() {
            return oneMoveDirectionCount;
        }

        public void setOneMoveDirectionCount(int oneMoveDirectionCount) {
            this.oneMoveDirectionCount = oneMoveDirectionCount;
        }
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

        String[][] maze = new String[][]{
                {"#",".","#","#","#","#","#","#"},
                {"#",".","#","#","#","#","#","#"},
                {"#",".",".",".",".",".",".","#"},
                {"#",".","#","#","#",".","#","#"},
                {"#",".","#","#","#",".","#","#"},
                {"#",".","#","#","#",".","#","#"},
                {"#",".",".",".",".",".",".","."},
                {"#",".","#","#","#","#","#","#"},
                {"#",".","#","#",".","#","#","#"},
                {"#",".",".",".",".",".",".","#"},
                {"#",".","#","#","#","#","#","#"}
        };

        Point source = new Point(0,0);
        Point destination = new Point(2,3);
        VietPhapMaze vietPhapMaze = new VietPhapMaze(maze,new PotentialVertex(source),new PotentialVertex(destination));

        //TODO
        //vietPhapMaze.solve(source);

        System.out.println("");

    }
}
