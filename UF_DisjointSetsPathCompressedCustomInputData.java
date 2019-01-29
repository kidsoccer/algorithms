import java.util.HashMap;
import java.util.Map;

public class UF_DisjointSetsPathCompressedCustomInputData {
    //access to component id/ identifiers
    //TODO: co the thay the bang Map
    private int [] id; //root
    int [] size; // size of component/ number of element in the component , used while union
    private int count;
    //use for second constructor - with input data from external
    private Map<Integer,Integer> revertedElement;
    private boolean isCustomData;
    private  int [] data;

    UF_DisjointSetsPathCompressedCustomInputData(int N){
        id = new int[N];
        size = new int[N];
        //init components id array, link to themselves
        for (int i =0; i<N;i++){
            id[i] =i;
            size[i] =1;//initially, all component has size =1
        }
        count = N;
    }

    UF_DisjointSetsPathCompressedCustomInputData(int [] elements){
       this(elements.length);
        revertedElement = new HashMap<>();
        for (int i =0; i<elements.length;i++){
            revertedElement.put(elements[i],i);
        }
        isCustomData = true;
        data = elements;
    }


    //find component of element x

    /**
     * Two
     * sites are in the same component if and
     * only if this process leads them to the
     * same root.
     * @param p
     * @return
     */
    int find(int p){
        /**
         *  we start
         * at the given site, follow its link to another site, follow that site’s link to yet
         * another site, and so forth, following
         * links until reaching a root, a site that
         * has a link to itself
         */

        /**
         * To implement path compression, we just add another loop to
         * find() that sets the id[] entry corresponding to each node encountered along the way
         * to link directly to the root. The net result is to flatten the trees almost completely, approximating the ideal achieved by the quick-find algorithm
         */

        if(isCustomData){
            p = revertedElement.get(p);
        }

        int root = p;
        while (root != id[root])
            //found the root
            root = id[root];

        //this loop for path compression/flatten the trees
        while (p != root) {
            int newp = id[p];
            id[p] = root;
            p = newp;
        }
        return root;
    }

    /**
     * for custom data from external source, need using reverted array
     * only use this method when using custom data
     * @param p
     * @return
     */
    int findCustom(int p){
        int root = find(p);
        if(isCustomData) {
            return data[root];
        }
        return root;
    }


    boolean connected(int p,int q){
        return find(p) == find(q);
    }

    /**
     * Rather than arbitrarily connecting the
     * second tree to the first for union(), we keep track
     * of the size of each tree and always connect the
     * smaller tree to the larger
     * keep the height of tree small
     * @param p
     * @param q
     */
    //put p and q to the same component/same set
    void union(int p,int q){

        int pRoot = find(p);
        int qRoot = find(q);

        //if already in the same component, so don't need do anything
        if(pRoot == qRoot)
            return;
        //join p to q
        //link root p to root q
        //make smaller (size) root point to larger root
        if(size[pRoot] <size[qRoot]){
            id[pRoot] = qRoot;
            size[qRoot] += size[pRoot];
        } else{
            id[qRoot] = pRoot;
            size[qRoot] += size[pRoot];
        }

        //comonent count
        count--;
    }


    public static void main(String ...s){
        int [] vertices = new int[]{2,5,3,9,0};
        UF_DisjointSetsPathCompressedCustomInputData uf_disjointSets = new UF_DisjointSetsPathCompressedCustomInputData(vertices);


        int component = uf_disjointSets.findCustom(9);
        System.out.println("Num of Components :" + uf_disjointSets.count );
        System.out.println("Component of 9 is :" + component );
        uf_disjointSets.union(0,2);
        System.out.println("Component of 0 is :" + uf_disjointSets.findCustom(0) );
        System.out.println("Component of 2 is :" + uf_disjointSets.findCustom(2) );
        System.out.println("Num of Components :" + uf_disjointSets.count );

        System.out.println("Is 0 connected to 2 ?:" + uf_disjointSets.connected(0,2) );
        uf_disjointSets.union(2,3);
        System.out.println("Component of 0 is :" + uf_disjointSets.findCustom(0) );
        System.out.println("Num of Components :" + uf_disjointSets.count );
    }

}
