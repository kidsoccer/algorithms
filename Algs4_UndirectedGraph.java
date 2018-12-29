package graph;

import java.util.*;

public class Algs4_UndirectedGraph {
    int numVertices;//number of vertices
    int numEdges; //number of edges


    //co the dung SET - tranh trung , va ko cho phep song song
    //co the dung map, neu vertex ko phai la kieu integer

    List<Integer> [] adjencyList;//danh sach ke


    public Algs4_UndirectedGraph(int v, int e){
        this.numEdges = e;
        this.numVertices =v;
        //khoi tao danh sach ke rong
        adjencyList = new List[numVertices];
        for (int i =0; i < numVertices; i++){
            adjencyList[i] = new ArrayList<>();
        }
    }

    public void addEdge(int fromVertex,int toVertex){
        //update danh sach ke cua from vertex
        adjencyList[fromVertex].add(toVertex);
        //update danh sach ke cua to vertex
        adjencyList[toVertex].add(fromVertex);

        //tang bien dem tong so canh cho toi hien tai
        numEdges++;
    }

    public List<Integer> getAdjacenList(int ofVertex){
        return adjencyList[ofVertex];
    }


    public void printGraph(){
        for (int i = 0; i< numVertices; i++){
            List<Integer> ajd = getAdjacenList(i);
            for (int j =0; j<ajd.size();j++){
                System.out.println(String.format("%s%s",i,j));
            }
        }
    }

   static class DepthFirstSearchGraph {

        Algs4_UndirectedGraph basicGraph;
        int sourceVertex;
        int count = 1;
        int [] pointToPreviousVertex;
        boolean [] visited; //danh dau nhung vertex da visit
        public DepthFirstSearchGraph(Algs4_UndirectedGraph graph, int sourceVertex){
            this.basicGraph = graph;
            pointToPreviousVertex = new int[basicGraph.numVertices];
            this.sourceVertex = sourceVertex;
            visited = new boolean[graph.numVertices];
            dfs(this.sourceVertex);
        }

        private void dfs(int sourceVertex){

            //danh dau dinh da tham

            visited[sourceVertex] = true;
            List<Integer> adjList = basicGraph.getAdjacenList(sourceVertex);
            for (Integer v : adjList
                 ) {
                if(!visited[v]){
                    count ++;
                    pointToPreviousVertex[v] = sourceVertex;
                    dfs(v);
                }
            }

        }


        public Stack<Integer> pathTo(int targetVertex){
            if(!visited[targetVertex]){
                return null;
            }

            Stack<Integer> path = new Stack<>();

            for (int x = targetVertex; x != sourceVertex; x = pointToPreviousVertex[x]){
                path.push(x);
            }

            return path;
        }



    }


    static class BreadthFirstSearch {

        private Algs4_UndirectedGraph basicGraph;
        private int source;
        private int [] parentLink;
        private boolean [] visited;

        public BreadthFirstSearch(Algs4_UndirectedGraph graph, int source){
            this.basicGraph = graph;
            this.source = source;
            parentLink = new int[graph.numVertices];
            visited = new boolean[graph.numVertices];
            bfs();
        }

        //o do thi vo huong ko co trong số, thi đây chinh là tim dduong đi ngan nhất tới mọi điểm
        // đô thị co trong số thi phai dung Djista de tim ra duong di ngan nhat
        private void bfs(){
            Queue<Integer> vertices = new LinkedList<>();
            vertices.add(source);
            visited[source] = true;
            while (!vertices.isEmpty()){
                int parent = vertices.poll();
                List<Integer> adjList = basicGraph.getAdjacenList(parent);
                for (Integer v: adjList
                     ) {
                    if(!visited[v]){
                        visited[v] = true;
                        vertices.add(v);
                        parentLink[v] = parent;
                    }
                }

            }
        }

        public Iterable<Integer> pathTo(int toVertex){
            if(!visited[toVertex]){
                return null;
            }
            Stack<Integer> vertices = new Stack<>();

            for (int x = toVertex; x != source; x = parentLink[x]
                 ) {
                vertices.push(x);
            }

            return vertices;
        }


    }


    static class Cycle {
        Algs4_UndirectedGraph basicGraph;
        int [] pointToPreviousVertex;
        boolean hasCycle ;
        boolean [] visited; //danh dau nhung vertex da visit
        public Cycle(Algs4_UndirectedGraph graph){
            this.basicGraph = graph;
            pointToPreviousVertex = new int[basicGraph.numVertices];
            visited = new boolean[graph.numVertices];
            for(int i =0; i < graph.numVertices; i ++){
                if(!visited[i]) {
                    dfs(i,i);
                }
            }
        }

        //với mỗi dỉnh v, có 1 đỉnh kề u đã được thăm và u không phải parent của v thi có 1 chu trình
        private void dfs(int sourceVertex,int parent){
            //danh dau dinh da tham
            visited[sourceVertex] = true;
            List<Integer> adjList =
                    basicGraph.getAdjacenList(sourceVertex);
            for (Integer v : adjList
            ) {
                if(!visited[v]){
                    pointToPreviousVertex[v] = sourceVertex;
                    dfs(v,sourceVertex);
                } else if(v != parent) {
                    hasCycle = true;
                }
            }

        }

        public boolean hasCycle(){
            return hasCycle;
        }
    }


    public static void main(String ...s){

        Algs4_UndirectedGraph basicGraph = new Algs4_UndirectedGraph(5,5);
        basicGraph.addEdge(0,1);
        basicGraph.addEdge(0,2);
        basicGraph.addEdge(0,3);
        basicGraph.addEdge(1,4);
        basicGraph.addEdge(2,3);


        basicGraph.printGraph();

        DepthFirstSearchGraph depthFirstSearchGraph = new DepthFirstSearchGraph(basicGraph,0);
        System.out.println(depthFirstSearchGraph.pathTo(4));

        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(basicGraph,0);
        System.out.println(breadthFirstSearch.pathTo(4));


        Cycle cycle = new Cycle(basicGraph);
        System.out.println(cycle.hasCycle);

    }

}
