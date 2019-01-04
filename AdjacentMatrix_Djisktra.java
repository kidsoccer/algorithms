import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AdjacentMatrix_Djisktra {

    /**
     * Implement AdjacentMatrix_Djisktra algorithm using adjacent matrix
     */

    static class Graph {
        static final int v = 6;
        final int[][] graph = new int[v][v];

        public Graph() {
            for (int i = 0; i < v; i++) {
                for (int j = 0; j < v; j++) {
                    if (i == j) {
                        //vertex connect target itself
                        graph[i][j] = 0;
                    } else {
                        graph[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
        }

        public void addEdge(int from, int to, int weight) {
            if (from < v && to < v) {
                graph[from][to] = weight;
            }
        }

        public List<Pair<Integer, Integer>> getAdjList(int v) {
            int[] adjVertices = graph[v];
            List<Pair<Integer, Integer>> verticesAndWeight = new ArrayList<>();
            for (int x = 0; x < adjVertices.length; x++
            ) {
                if (adjVertices[x] != Integer.MAX_VALUE) {
                    //lay ra dinh ke voi dinh hien tai
                    verticesAndWeight.add(new Pair<>(x, adjVertices[x]));
                }
            }

            return verticesAndWeight;
        }
    }

      static   class ShortTestPath{

            int from;
            int target;
            Graph graph;
            boolean [] stp ;//luu nhung dinh da visit
            int [] minVertexDistance; //luu lai khaong cach ngan nhat toi đỉnh hiện tai
            int [] prev; //đỉnh trước đó của đỉnh hiện tại trong đường đi ngăn nhất

            public ShortTestPath(int from, int to){
                //init data : buoc 1
                this.from = from;
                graph = new Graph();
                this.target = to;
                minVertexDistance = new int[graph.graph.length];
                stp = new boolean[graph.graph.length];
                minVertexDistance[from] = 0;
                prev = new int[graph.graph.length];
                for (int i = 0; i < graph.graph.length; i ++){
                    //add vertex
                    stp[i] = false;
                    if( i != from){
                        minVertexDistance[i] = Integer.MAX_VALUE;
                    }

                    prev[i] = -1;
                }

                //add cac canh va trong so di kem
                graph.addEdge(0,1,6);
                graph.addEdge(0,3,4);
                graph.addEdge(1,3,3);
                graph.addEdge(1,2,5);
                graph.addEdge(2,5,2);
                graph.addEdge(4,5,3);
                graph.addEdge(3,2,3);
                graph.addEdge(3,4,9);
            }

            private void shortestPath(){

                //buoc 2 : khi dinh target van chua duoc visit
                while (!stp[target]){
                    int minVertex = minDistanceVertext();

                    //buoc 3: danh dau da thanh
                    stp[minVertex] = true;

                    //buoc 4:
                    List<Pair<Integer,Integer>> adjs = graph.getAdjList(minVertex);

                    int minDistance = this.minVertexDistance[minVertex];
                    for(Pair<Integer,Integer> k: adjs){
                        //canh ke ma chua duoc tham
                        if(!stp[k.getKey()] && graph.graph[minVertex][k.getKey()] != Integer.MAX_VALUE){
                            //distance from i target k
                            int Dik = graph.graph[minVertex][k.getKey()];
                            if(this.minVertexDistance[k.getKey()] > minDistance + Dik){
                                this.minVertexDistance[k.getKey()] = minDistance + Dik;
                                prev[k.getKey()] = minVertex;
                            }
                        }
                    }
                }


                printPath();
            }


            private void printPath(){

                System.out.println("Path from :" + from + " target " + target);
                System.out.println("Distance:" + minVertexDistance[target]);
                boolean run = true;
                Stack<Integer> path = new Stack<>();
                path.push(target);
                while (run){
                    int prevVertex = prev[target];
                    if(prevVertex != -1){
                        path.push(prevVertex);
                        target = prevVertex;
                    } else {
                        run = false;
                    }
                }
                while (!path.isEmpty()){
                    System.out.println(path.pop());
                }
            }


            private int minDistanceVertext(){
                int vertexIndex = -1;
                int minDistance = Integer.MAX_VALUE;
                for (int i = 0; i < this.minVertexDistance.length; i ++){
                    //xet cac dinh chua duoc tham va lay ra dinh co khoang cach nho nhat
                    if(stp[i] == false && this.minVertexDistance[i] < minDistance ){
                        minDistance = this.minVertexDistance[i];
                        vertexIndex = i;
                    }

                }
                return vertexIndex;
            }


    }

    public static void main(String ...s){

        ShortTestPath shortTestPath = new ShortTestPath(0,5);
        shortTestPath.shortestPath();

    }

}
