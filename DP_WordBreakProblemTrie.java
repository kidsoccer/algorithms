package net.aladdintech.api;

import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Given a dictionary of words and a string of characters, find if the string of characters can be broken into individual valid words from the dictionary.
 Example:
 Dictionary: "this", "th", "is", "famous", "Word", "break", "b","r", "e", "a", "k", "br", "bre", "brea", "ak", "problem"
 String    : Wordbreakproblem
 Output   : true
 Because the string can be broken into valid words: IDeserve learning platform
 */
public class DP_WordBreakProblemTrie {

    final static Set<String> dic = ImmutableSet.of("this", "th", "is", "famous", "Word", "break", "b","r", "e", "a", "k", "br", "bre", "brea", "ak", "problem");

    static Trie trie = new Trie();
    static {
        for (String st :dic
                ) {
            trie.insert(st);
        }
    }

    //complexity worst case o(n^2)
    protected boolean canBreakIntoValidWord(final String word){
        final int n = word.length();
        boolean [] validWord = new boolean[n];
        List<String> validWords = new ArrayList<>();
        for (int i = 0; i < n; i ++
             ) {
            if(Objects.nonNull(trie.find(word.substring(0,i+1),true))){
                validWord[i] = true;
                validWords.add(word.substring(0,i+1));
            }

            if(i== n-1 && validWord[i] == true){
                System.out.println("Broken words");
                System.out.println(String.join(",",validWords));
                return true;
            }

            if(validWord[i] == true){

                for (int j = i +1; j <n;j++){
                    if(Objects.nonNull(trie.find(word.substring(i+1,j+1),true))){
                        validWord[j] = true;
                        validWords.add(word.substring(i+1,j+1));
                    }

                    if(j== n-1 && validWord[j] == true){
                        System.out.println("Broken words");
                        System.out.println(String.join(",",validWords));
                        return true;
                    }
                }
            }

        }


        return false;

    }


    public static void main(String ...s){

        DP_WordBreakProblemTrie dp_wordBreakProblem = new DP_WordBreakProblemTrie();
        boolean result = dp_wordBreakProblem.canBreakIntoValidWord("Wordbreakproblem");
        System.out.println(result);

    }

}
