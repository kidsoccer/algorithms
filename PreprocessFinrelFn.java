package com.sentifi.greenflow.message;

import com.google.common.collect.ImmutableSet;
import com.sentifi.greenflow.lang.Token;
import com.sentifi.greenflow.lang.WordGramUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

public class FinRelTextHelper {

  private static final Set<String> ADDITIONAL_CITY_KEYWORDS = ImmutableSet.of("kobe");
  private static final Set<String> ADDITIONAL_COUNTRY_KEYWORD =
      ImmutableSet.of(
          "cuba", "fr", "f.r", "iraq", "jpan", "laos", "nz", "n.z", "peru", "uk", "u.k", "u.s",
          "u.s.", "usa", "u.s.a");

  Map<String, GeoNamesAndCommonWordsTrie> dictionary = new HashMap<>();

  public Map<String, GeoNamesAndCommonWordsTrie> getDictionary() {
    return dictionary;
  }

  Set<String> stopWords;

  static final String PREFIX_KEY_COMMON = "a:";
  static final String PREFIX_KEY_CITIES = "b:";
  static final String PREFIX_KEY_COUNTRIES = "c:";

  public FinRelTextHelper(
      Map<String, GeoNamesAndCommonWordsTrie> dictionary, final Set<String> stopWords) {
    this.dictionary = dictionary;
    this.stopWords = stopWords;
  }

  public FinRelTextHelper() {
    super();
  }

  public void addCities(Iterable<String> inputDic) {
    for (String word : inputDic) {
      addToDic(word, PREFIX_KEY_CITIES);
    }
  }

  public void addCountry(Iterable<String> inputDic) {
    for (String word : inputDic) {
      addToDic(word, PREFIX_KEY_COUNTRIES);
    }
  }

  public void addCommon(Iterable<String> inputDic) {
    for (String word : inputDic) {
      addToDic(word, PREFIX_KEY_COMMON);
    }
  }

  public String replace(String text, NeAnnotation annotation) {
    String[] tokens = text.split(StringUtils.SPACE);
    StringBuilder builder = new StringBuilder();

    String prefixKey = PREFIX_KEY_COUNTRIES;
    if (NeAnnotation.CITY.equals(annotation)) {
      prefixKey = PREFIX_KEY_CITIES;
    }

    for (int unigramIndex = 0; unigramIndex < tokens.length; unigramIndex++) {
      GeoNamesAndCommonWordsTrie longest = null;
      GeoNamesAndCommonWordsTrie current = dictionary.get(prefixKey + tokens[unigramIndex]);
      String keywords = tokens[unigramIndex];
      int ngramIndex = unigramIndex;
      int skip = 0;
      while (current != null && !NeAnnotation.isAlreadyAnnotated(tokens[ngramIndex])) {
        if ((NeAnnotation.COUNTRY.equals(annotation) && isCountry(keywords))
            || (NeAnnotation.CITY.equals(annotation) && isCity(keywords))) {
          longest = current;
          skip = ngramIndex - unigramIndex;
        }
        if(++ngramIndex >= tokens.length){
          current =null;
        } else {
         current= current.children.get(tokens[ngramIndex]);
         keywords  = keywords + StringUtils.SPACE + tokens[ngramIndex];
        }
      }
      String replacement = tokens[unigramIndex];
      if (longest != null && longest.terminal) {
        replacement = annotation.getText();
      }
      builder.append(replacement).append(StringUtils.SPACE);
      unigramIndex+=skip;
    }
    return builder.toString().trim();
  }

  private boolean isCountry(final String s) {
    if (ADDITIONAL_COUNTRY_KEYWORD.contains(s)) {
      return true;
    }

    if (s.length() >= 5
        && isInDic(s, PREFIX_KEY_COUNTRIES).isPresent()
        && !isInDic(s, PREFIX_KEY_COMMON).isPresent()) {
      return true;
    }

    return false;
  }

  private boolean isCity(final String s) {
    if (ADDITIONAL_CITY_KEYWORDS.contains(s)) {
      return true;
    }
    if (s.length() >= 5
        && isInDic(s, PREFIX_KEY_CITIES).isPresent()
        && !isInDic(s, PREFIX_KEY_COMMON).isPresent()) {

      return true;
    }

    return false;
  }

  private Optional<GeoNamesAndCommonWordsTrie> isInDic(String s, String prefixKey) {
    List<Token> strTokens = WordGramUtils.generateUnigrams(s);
    String prefix = prefixKey + strTokens.get(0).getContent();
    List<String> children =
        strTokens.stream().skip(1).map(m -> m.getContent()).collect(Collectors.toList());
    if (dictionary.containsKey(prefix)) {
      if (!children.isEmpty()) {
        GeoNamesAndCommonWordsTrie trie = dictionary.get(prefix);
        return trie.find(children);
      }
      //node with empty children
      GeoNamesAndCommonWordsTrie trie = dictionary.get(prefix);
      if (trie.terminal) {
        return Optional.of(trie);
      }
    }
    return Optional.empty();
  }

  void addToDic(String words, String prefixKey) {

    String[] wordArr = words.split(StringUtils.SPACE);
    String prefix = prefixKey + wordArr[0].toLowerCase();
    List<String> children =
        Arrays.stream(wordArr).skip(1).map(String::toLowerCase).collect(Collectors.toList());

    if (dictionary.containsKey(prefix)) {
      GeoNamesAndCommonWordsTrie trie = dictionary.get(prefix);
      trie.insert(children);
    } else {
      GeoNamesAndCommonWordsTrie trie = new GeoNamesAndCommonWordsTrie(wordArr[0].toLowerCase());
      trie.insert(children);
      dictionary.put(prefix, trie);
    }
  }

  public static class GeoNamesAndCommonWordsTrie {

    @JsonProperty("c")
    protected Map<String, GeoNamesAndCommonWordsTrie> children;

    @JsonProperty("t")
    public boolean terminal = false;

    @JsonProperty("v")
    private String value; // string so far

    public GeoNamesAndCommonWordsTrie(){
      this(null);
    }
    public GeoNamesAndCommonWordsTrie(String value) {
      this.value = value;
      children = new HashMap<>(); //tat ca node con cua node hien tai
    }

    private void add(String word) {
      String val = word;
      if (this.value != null) {
        //apend
        val = this.value + " " + word;
      }

      children.put(word, new GeoNamesAndCommonWordsTrie(val));
    }

    public void insert(List<String> word) {
      GeoNamesAndCommonWordsTrie node = this;
      if (word == null || word.isEmpty()) {
        node.terminal = true;
        return;
      }

      for (String w : word) {
        if (!node.children.containsKey(w)) {
          node.add(w);
        }
        node = node.children.get(w);
      }
      node.terminal = true;
    }

    public Optional<GeoNamesAndCommonWordsTrie> find(List<String> words) {
      GeoNamesAndCommonWordsTrie node = this;
      for (String w : words) {
        if (!node.children.containsKey(w)) {
          return Optional.empty();
        }
        node = node.children.get(w);
      }

      if (node.terminal) {
        return Optional.of(node);
      }
      return Optional.empty();
    }
  }
}
