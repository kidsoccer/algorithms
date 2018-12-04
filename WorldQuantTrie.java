import java.util.*;

public class WQ_Q3Trie {
    @Override
    public String toString() {
        return "WorldQuantTrie{" +
                "children=" + children +
                ", terminal=" + terminal +
                ", value='" + value + '\'' +
                ", isValidWord=" + isValidWord +
                '}';
    }

    protected Map<Character, WorldQuantTrie> children;
    protected boolean terminal = false;
    private String value; // string so far
    private boolean isValidWord = false;


    public WQ_Q3Trie(){
        this(null);
    }

    public WQ_Q3Trie(String value){
        this.value = value;
        children = new HashMap<Character, WorldQuantTrie>();//tat ca node con cua node hien tai
    }

    protected void add(char c,boolean isValidWord){
        //add new node
        String val;
        if(this.value == null){
            val = Character.toString(c);
        } else{
            //apend
            val = this.value +c;
        }

        WorldQuantTrie node = new WorldQuantTrie(val);
        node.isValidWord=isValidWord;
        children.put(c,node);

    }


    public void insert(String word){
        if(word == null){
            throw new IllegalArgumentException("Cannot add null to a trie");
        }
        WorldQuantTrie node = this;
        int count = 1;
        for (char c : word.toCharArray()){

            boolean valid = false;
            if(count == word.length()){
                valid = true;
            }
            if(!node.children.containsKey(c)){
                node.add(c,valid);
            }
            node = node.children.get(c);


            count ++;
        }
        node.terminal = true;
    }



    public List<String> longestChain(String [] trings){
        Map<String,List<String>> reduced = new HashMap<>();
        for (String tr: trings
             ) {
            reduced.put(tr,new ArrayList<>(reduceWords(tr)));
        }

        int max = 0;
        int temp = 0;
        String maxkey ="",tempkey = "";
        for (Map.Entry<String, List<String>> entry : reduced.entrySet()) {
            temp = entry.getValue().size();
            tempkey = entry.getKey();
            if(temp > max){
                max = temp;
                maxkey = tempkey;
            }
        }
        System.out.println(String.format("Lenght of longest chains : %s",max));
        System.out.println(String.format("Longest chains : %s",reduced.get(maxkey)));
        return reduced.get(maxkey);
    }


    private Collection<String> reduceWords(String prefix) {
        WorldQuantTrie node = this;
        List<String> results = new ArrayList<String>();

        char [] temp = prefix.toCharArray();
        for (int i =0; i <temp.length;i++) {
            if (!node.children.containsKey(temp[i])) {
                return Collections.emptyList();
            }

            if (node.isValidWord && node.children.get(temp[i]).isValidWord) {
                System.out.println(String.format("Value: %s, valid: %s",node.value,node.isValidWord));
                results.add(node.value);
            }
            node = node.children.get(temp[i]);
        }
        //add itself
        if(!results.isEmpty()) {
            //don't count itself as chain
            results.add(prefix);
        }
        return results;
    }

    public static void main(String ...s){

        WQ_Q3Trie trie = new WorldQuantTrie();
        trie.insert("a");
        trie.insert("an");
        trie.insert("and");
        trie.insert("bear");

        System.out.println(trie);
        trie.longestChain(new String []{"and","love"});

    }
}
