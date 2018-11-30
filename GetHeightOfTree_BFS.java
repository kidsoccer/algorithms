import java.util.LinkedList;
import java.util.List;

/**
 * Get the height of tree using Broad-First-Search
 */
public class GetHeightOfTree_BFS {
    public static void main(String[] args) {
        Node six = new Node(null, null, 6);
        Node five = new Node(null, null, 5);
        Node four = new Node(null, six, 4);
        Node two = new Node(four, five, 2);
        Node three = new Node(null, null, 3);
        Node one = new Node(two, three, 1);
        System.out.println(high(one));
    }
    private static int high(Node root){
        List<Node> nodes = new LinkedList<>();
        if (root == null) return 0;
        nodes.add(root);
        int high = 0;

        //using BFS : , each level of tree, get all children , each children increase counter
        while (!nodes.isEmpty()){
            high++;
            List<Node> allChild = new LinkedList<>();
            for (Node item : nodes){
                System.out.println(item.val);
                if (item.left != null) allChild.add(item.left);
                if (item.right != null) allChild.add(item.right);
            }
            nodes = allChild;
        }
        return high;
    }
    private static class Node{
        Node left;
        Node right;
        int val;
        Node(Node left, Node right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

}
