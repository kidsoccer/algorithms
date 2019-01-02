package graph;

import java.util.*;

public class Algs4_Digraph {
    private int numberVertices;
    private int numberEdges;
    List<Integer> [] adjancents;
    private int countVertex = 0;

    public Algs4_Digraph(final int v, final int e){
        this.numberEdges = e;
        this.numberVertices = v;
        adjancents = new List[v];
        for (int i =0; i< adjancents.length; i++
             ) {
            adjancents[i] = new ArrayList<>();
        }
    }

  public void addEdge(int fromV, int toV){
        //khac voi undirected graph - khi add edge, chi add 1 chieu
        adjancents[fromV].add(toV);
        countVertex++;
  }
    public List<Integer> getAdjacenList(int ofVertex){
        return adjancents[ofVertex];
    }


    public void printGraph(){
        for (int i = 0; i< numberVertices; i++){
            List<Integer> ajd = getAdjacenList(i);
            for (int j =0; j<ajd.size();j++){
                System.out.println(String.format("%s%s",i,j));
            }
        }
    }

    //TODO: DFS tuong tu undirected graph
    static class DFS{
        private Algs4_Digraph digraph;
        private boolean visited [];
        private int [] pointToParent;
        private int fromV;


        public DFS(final Algs4_Digraph digraph,int fromV){
            this.fromV = fromV;
            this.digraph = digraph;
            visited = new boolean[digraph.numberVertices];
            pointToParent = new int[digraph.numberVertices];
            dfs(fromV);
        }

        private void dfs(int fromV){
            visited[fromV] = true;
            List<Integer> ajd = digraph.getAdjacenList(fromV);
            for (int v : ajd
                 ) {
                if(!visited[v]){
                    pointToParent[v] = fromV;
                    dfs(v);
                }
            }

        }

        public Stack<Integer> pathTo(int toV){

            Stack<Integer> integers = new Stack<>();
            for (int x = toV; x != fromV; x= pointToParent[toV]
                 ) {
                integers.push(x);
            }

            return integers;
        }
    }

    static class BFS{

        private Algs4_Digraph digraph;
        private int fromV;
        boolean [] visited;
        int [] pointToParent;

        public BFS(Algs4_Digraph digraph,int fromV){
            this.digraph = digraph;
            this.fromV=fromV;
            visited = new boolean[digraph.numberVertices];
            pointToParent = new int[digraph.numberVertices];

        }

        private void bfs(){

        }



    }



    static class CyclicDigraph{
        private Algs4_Digraph digraph;
        boolean [] visited;
        int [] pointToParent;
        boolean [] onStack;
        Stack<Integer> cycle;



        public CyclicDigraph(Algs4_Digraph digraph){
            this.digraph = digraph;
            onStack = new boolean[digraph.numberVertices];
            visited = new boolean[digraph.numberVertices];
            pointToParent = new int[digraph.numberVertices];
            for (int i = 0; i < digraph.numberVertices; i ++){
                if(!visited[i]){
                    dfs(i);
                }
            }
        }

        private void dfs(int fromV){
            onStack[fromV] = true;
            visited[fromV] = true;
            List<Integer> adjs = digraph.getAdjacenList(fromV);
            for (Integer v: adjs
                 ) {
                if(hasCycle()){
                    return;
                }
                else if(!visited[v]){
                    pointToParent[v] = fromV;
                    dfs(v);
                } else if (onStack[v]){
                    //co cycle
                    cycle = new Stack<>();
                    for(int x =fromV; x !=v; x = pointToParent[x] ){
                        cycle.push(x);
                    }
                    cycle.push(v);
                    cycle.push(fromV);

                }

            }

            System.out.println(" debug: " + fromV);
            onStack[fromV] =false;
        }

        public boolean hasCycle(){
            return cycle != null;
        }

        public Stack<Integer> cycle(){
            return cycle;
        }


    }


    static class DepthFirstOrder {

        private Algs4_Digraph digraph;
        private Stack<Integer> topologicalOrder;
        private Queue<Integer> preOrder;
        private Queue<Integer> postOrder;
        private boolean [] visited;
        public DepthFirstOrder(final Algs4_Digraph digraph){
            this.digraph = digraph;
            topologicalOrder = new Stack<>();
            preOrder = new LinkedList<>();
            postOrder = new LinkedList<>();
            visited = new boolean[digraph.numberVertices];
            for(int i =0 ; i < digraph.numberVertices; i ++){
                if(!visited[i]){
                    dfs(i);
                }
            }
        }


        private void dfs(int fromV){

            visited[fromV] = true;
            preOrder.add(fromV);
            List<Integer> adjs = digraph.getAdjacenList(fromV);
            for (Integer v : adjs){
                if(!visited[v]){
                    dfs(v);              }
            }

            postOrder.add(fromV);
            topologicalOrder.push(fromV);
        }

        public Iterable<Integer> pre(){
            return preOrder;
        }

        public Iterable<Integer> post(){
            return postOrder;
        }

        public Iterable<Integer> reversePost(){
            return topologicalOrder;
        }

    }


    public static void main(String ...s){

        Algs4_Digraph basicGraph = new Algs4_Digraph(5,5);
        basicGraph.addEdge(0,1);
        basicGraph.addEdge(0,2);
        basicGraph.addEdge(3,0);
        basicGraph.addEdge(1,4);
        basicGraph.addEdge(2,3);

        basicGraph.printGraph();

        CyclicDigraph cyclicDigraph = new CyclicDigraph(basicGraph);
        System.out.println("Has cycle : " + cyclicDigraph.hasCycle());
        if(cyclicDigraph.hasCycle()){
            System.out.println("Cycle : " + cyclicDigraph.cycle());
        }

        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(basicGraph);
        System.out.println(" PreOrder :  " + depthFirstOrder.pre());
        System.out.println(" PostOrder :  " + depthFirstOrder.post());
        System.out.println(" Topological sort/stack");
        while(!depthFirstOrder.topologicalOrder.empty()){
            System.out.println(depthFirstOrder.topologicalOrder.pop());
        }
    }
}
