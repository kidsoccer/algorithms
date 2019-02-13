package com.sentifi.greenflow.message;

import com.google.common.collect.ImmutableSet;
import com.sentifi.greenflow.lang.PositionalNgram;
import com.sentifi.greenflow.lang.Token;
import com.sentifi.greenflow.lang.WordGramUtils;
import com.sentifi.greenflow.utils.StringUtils;
import java.util.*;
import java.util.stream.Collectors;
import org.codehaus.jackson.annotate.JsonProperty;

public class FinRelTextHelper {

  private static final Set<String> ADDITIONAL_CITY_KEYWORDS = ImmutableSet.of("kobe");
  private static final Set<String> ADDITIONAL_COUNTRY_KEYWORD =
      ImmutableSet.of(
          "cuba", "fr", "f.r", "iraq", "jpan", "laos", "nz", "n.z", "peru", "uk", "u.k", "u.s",
          "u.s.", "usa", "u.s.a");

  Map<String, TrieFinRel> dictionary = new HashMap<>();

  public Map<String, TrieFinRel> getDictionary() {
    return dictionary;
  }

  Set<String> stopWords;

  static final String PREFIX_KEY_COMMON = "a:";
  static final String PREFIX_KEY_CITIES = "b:";
  static final String PREFIX_KEY_COUNTRIES = "c:";

  public FinRelTextHelper(Map<String, TrieFinRel> dictionary, final Set<String> stopWords) {
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

  public String replaceCityAndCountry(String sentence, NeAnnotation annotation) {
    List<Token> tokens = WordGramUtils.generateUnigrams(sentence);
    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);
      String prefixKey = PREFIX_KEY_COUNTRIES;
      if (NeAnnotation.CITY.equals(annotation)) {
        prefixKey = PREFIX_KEY_CITIES;
      }
      String prefix = token.getContent().toLowerCase();
      if (!dictionary.containsKey(prefixKey + prefix.toLowerCase())) continue;
      int count = i;

      do {
        if ((NeAnnotation.COUNTRY.equals(annotation) && isCountry(prefix))
            || (NeAnnotation.CITY.equals(annotation) && isCity(prefix))) {
          //found , replace and stop, skip to the next word
          sentence = StringUtils.replaceWholeWord(sentence, prefix, annotation.getText());
          break; //stop next words
        }

        count++;
        if (count == tokens.size()
            || NeAnnotation.isPreAnnotated(tokens.get(count).getContent().toLowerCase())) {
          break;
        }
        prefix =
            new StringBuilder(prefix)
                .append(" ")
                .append(tokens.get(count).getContent().toLowerCase())
                .toString();
      } while (count < tokens.size() - 1);
    }
    return sentence;
  }

  boolean isNotCommonWords(final PositionalNgram ngram) {
    return !ngram.isUnigram() || Objects.isNull(isInDic(ngram.getContent(), PREFIX_KEY_COMMON));
  }

  private boolean isCountry(final String prefix) {
    if ((isInDic(prefix, PREFIX_KEY_COUNTRIES).isPresent()
            || ADDITIONAL_COUNTRY_KEYWORD.contains(prefix))
        && (!org.apache.commons.lang3.StringUtils.containsNone(
                prefix, org.apache.commons.lang3.StringUtils.SPACE)
            || (!isInDic(prefix, PREFIX_KEY_COMMON).isPresent() && !stopWords.contains(prefix)))) {
      return true;
    }

    return false;
  }

  private boolean isCity(final String prefix) {

    if ((isInDic(prefix, PREFIX_KEY_CITIES).isPresent()
            || ADDITIONAL_CITY_KEYWORDS.contains(prefix))
        && (!org.apache.commons.lang3.StringUtils.containsNone(
                prefix, org.apache.commons.lang3.StringUtils.SPACE)
            || (!isInDic(prefix, PREFIX_KEY_COMMON).isPresent() && !stopWords.contains(prefix)))) {

      return true;
    }

    return false;
  }

  private Optional<TrieFinRel> isInDic(String ngrams, String prefixKey) {
    String[] wordArr = ngrams.toLowerCase().split(" ");
    String prefix = prefixKey + wordArr[0];
    List<String> children =
        Arrays.stream(wordArr).skip(1).map(String::toLowerCase).collect(Collectors.toList());
    if (dictionary.containsKey(prefix)) {
      if (!children.isEmpty()) {
        TrieFinRel trie = dictionary.get(prefix);
        return trie.find(children);
      }

      //node with empty children
      TrieFinRel trie = dictionary.get(prefix);
      if (trie.terminal) {
        return Optional.of(trie);
      }
    }
    return Optional.empty();
  }

  void addToDic(String words, String prefixKey) {

    String[] wordArr = words.split(" ");
    String prefix = prefixKey + wordArr[0].toLowerCase();
    List<String> children =
        Arrays.stream(wordArr).skip(1).map(String::toLowerCase).collect(Collectors.toList());

    if (dictionary.containsKey(prefix)) {
      TrieFinRel trie = dictionary.get(prefix);
      trie.insert(children);
    } else {
      TrieFinRel trie = new TrieFinRel();
      trie.insert(children);
      dictionary.put(prefix, trie);
    }
  }

  public static class TrieFinRel {

    @JsonProperty("c")
    protected Map<String, TrieFinRel> children;

    @JsonProperty("t")
    public boolean terminal = false;

    public TrieFinRel() {
      super();
      children = new HashMap<>();
    }

    private void add(String word) {
      children.put(word, new TrieFinRel());
    }

    public void insert(List<String> word) {
      if (word == null) {
        throw new IllegalArgumentException("Cannot add null to a trie");
      }
      TrieFinRel node = this;
      for (String w : word) {
        if (!node.children.containsKey(w)) {
          node.add(w);
        }
        node = node.children.get(w);
      }
      node.terminal = true;
    }

    public Optional<TrieFinRel> find(List<String> words) {
      TrieFinRel node = this;
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
