package com.sentifi.greenflow;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrieNER {

    public static void main(String ... s){

        final Map<String,Trie> dictionary = new HashMap<>();

        List<String> inputDic = ImmutableList.of(
                "Digitisation",
                "Axon Active",
                "Axon Lover",
                "Donal Trump",
                "General Motors",
                "EMIS Group PLC",
                "GS Engineering Construction Corp",
                "NagaCorp Ltd",
                "Bitcoin test",
                "Innotree Co",
                "Morelia",
                "Mornington",
                "SIIC Environment Holdings Ltd",
                "Surgical Care Affiliates",
                "Shanghai Stock Exchange Composite Index",
                "Shanghai Stock Exchange Composite Index",
                "OnVista Group"
        );
        for (String word: inputDic
                ) {
            buildTrie(word,dictionary);
        }

        System.out.println(isInDic("Morelia",dictionary));
        System.out.println(isInDic("Surgical Care Affiliates",dictionary));
        System.out.println(isInDic("Stock Exchange",dictionary));
    }

    static String isInDic(String words,Map<String,Trie> dictionary){
        String [] wordArr = words.toLowerCase().split(" ");
        String prefix = wordArr[0];
        List<String> children =  Arrays.stream(wordArr).skip(1).map(String::toLowerCase).collect(Collectors.toList());
        if (dictionary.containsKey(prefix)){
            if(!children.isEmpty()) {
                Trie trie = dictionary.get(prefix);
                return
                        trie.find(children);
            }
            return prefix;
        }
        return null;
    }

    static void buildTrie(String words,Map<String,Trie> dictionary){

        String [] wordArr = words.split(" ");
        String prefix = wordArr[0].toLowerCase();
        List<String> children =  Arrays.stream(wordArr).skip(1).map(String::toLowerCase).collect(Collectors.toList());

        if(dictionary.containsKey(prefix)){
            Trie trie = dictionary.get(prefix);
            trie.insert(children);
        } else {
            Trie trie = new Trie(prefix);
            trie.insert(children);
            dictionary.put(prefix,trie );
        }
    }


    static class Trie {

        protected Map<String, Trie> children;
        protected boolean terminal = false;
        private String value; // string so far


        public Trie() {
            this(null);
        }

        public Trie(String value) {
            this.value = value;
            children = new HashMap<String, Trie>();//tat ca node con cua node hien tai
        }

        private void add(String word) {
            //add new node
            String val = word;
            if (this.value != null) {
                //apend
                val = this.value + " " + word;
            }

            children.put(word, new Trie(val));
        }


        public void insert(List<String> word){
            if(word == null){
                throw new IllegalArgumentException("Cannot add null to a trie");
            }
            Trie node = this;
            for (String w : word){
                if(!node.children.containsKey(w)){
                    node.add(w);
                }
                node = node.children.get(w);
            }
            node.terminal = true;
        }


        public String find(List<String> words) {
            Trie node = this;
            for (String w : words) {
                if (!node.children.containsKey(w)) {
                    return null;
                }
                node = node.children.get(w);
            }

            if ( node.terminal) {
                return node.value;
            }
            return null;
        }
    }
}
