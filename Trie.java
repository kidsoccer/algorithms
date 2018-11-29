package net.aladdintech.api;

import java.util.*;

public class Trie {

    protected Map<Character,Trie> children;
    protected boolean terminal = false;
    private String value; // string so far


    public Trie(){
        this(null);
    }

    public Trie(String value){
        this.value = value;
        children = new HashMap<Character, Trie>();//tat ca node con cua node hien tai
    }

    protected void add(char c){
        //add new node
        String val;
        if(this.value == null){
            val = Character.toString(c);
        } else{
            //apend
            val = this.value +c;
        }

        children.put(c,new Trie(val));

    }


    public void insert(String word){
        if(word == null){
            throw new IllegalArgumentException("Cannot add null to a trie");
        }
        Trie node = this;
        for (char c : word.toCharArray()){
            if(!node.children.containsKey(c)){
                node.add(c);
            }
            node = node.children.get(c);
        }
        node.terminal = true;
    }

    public String find(String word,boolean isWholeWordSearch){
        Trie node = this;
        for (char c : word.toCharArray()){
            if(!node.children.containsKey(c)){
                return null;
            }
            node = node.children.get(c);
        }

        if(isWholeWordSearch && node.terminal) {
            return node.value;
        }
        return null;
    }

    public Collection<String> autoComplete(String prefix) {
        Trie node = this;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        return node.allPrefixes();
    }

    protected Collection<String> allPrefixes() {
        List<String> results = new ArrayList<String>();
        if (this.terminal) {
            results.add(this.value);
        }
        for (Map.Entry<Character, Trie> entry : children.entrySet()) {
            Trie child = entry.getValue();
            Collection<String> childPrefixes = child.allPrefixes();
            results.addAll(childPrefixes);
        }
        return results;
    }
}
