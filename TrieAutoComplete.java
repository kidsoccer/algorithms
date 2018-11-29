package net.aladdintech.api;

public class TrieAutoComplete {
    public static void main(String ...s){

        Trie trie = new Trie();
        trie.insert("hello");
        trie.insert("hella");
        trie.insert("hell");
        trie.insert("horse");
        trie.insert("how");
        trie.insert("hope");

       // System.out.println(trie.allPrefixes());
        System.out.println(trie.autoComplete("hel"));

    }
}
