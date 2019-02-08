package com.sentifi.greenflow;

import java.util.*;
import java.util.stream.Collectors;

public class TrieNER {

   public static void main(String ... s){

        final Map<String,Trie> dictionary = new HashMap<>();

        List<String> inputDic = Arrays.asList(new String []{
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
        });
        for (String word: inputDic
        ) {
            buildTrie(word,dictionary);
        }

        System.out.println(isInDic("Donal",dictionary));
        System.out.println(isInDic("Surgical Care Affiliates",dictionary));
        System.out.println(isInDic("Stock Exchange",dictionary));

        replaceNGram("Donal Trump OnVista Group hello axon active hate axon lover but he likes Morelia very much ",dictionary);
    }

    static List<String> replaceNGram(String sentence,Map<String,Trie> dictionary){

        List<String> result = new ArrayList<>();

        String [] arr = sentence.split(" ");
        for (int i = 0; i< arr.length;i++){
                String prefix = arr[i];
                if(!dictionary.containsKey(prefix.toLowerCase()))
                    continue;

                    int count = i;
                    while (count < arr.length - 1) {
                        if (Objects.nonNull(isInDic(prefix, dictionary))) {
                            result.add(prefix);
                        }
                        count++;
                        prefix = prefix + " " + arr[count];
                }

        }
        return result;

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

            //node with empty children
            Trie trie = dictionary.get(prefix);
            if(trie.terminal){
                return trie.value;
            }

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
