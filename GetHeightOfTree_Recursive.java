import java.util.Objects;

/**
 * Get the height of tree using recursive
 */
public class GetHeightOfTree_Recursive {
    public static void main(String[] args) {
        Node seven = new Node(null, null, 7);
        Node six = new Node(null, seven, 6);
        Node five = new Node(null, null, 5);
        Node four = new Node(null, six, 4);
        Node two = new Node(four, five, 2);
        Node three = new Node(null, null, 3);


        Node one = new Node(two, three, 1);
        System.out.println(getHeigtFrom(one));
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


   static int getHeigtFrom(Node from){

        if(Objects.isNull(from)){
            return 0;
        }
        //duyet trai truoc
        //moi lan qua 1 node , tang len 1
        int currentCountLeft = getHeigtFrom(from.left);
       int currentCountRight = getHeigtFrom(from.right);
        if(currentCountLeft > currentCountRight){
            return currentCountLeft +1 ;
        } else {
            return currentCountRight +1;
        }

    }



}
