package com.closememo.autotag.service;

import com.closememo.autotag.infra.analyzer.WordAnalyzer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AutoTagService {

  private static final int MINIMUM_WORD = 5;

  private final WordAnalyzer wordAnalyzer;

  public AutoTagService(WordAnalyzer wordAnalyzer) {
    this.wordAnalyzer = wordAnalyzer;
  }

  public List<String> getAutoTags(String content) {

    Map<String, Long> map = wordAnalyzer.getWordCountMap(content);
    int numberOfCount = map.size();

    if (numberOfCount < MINIMUM_WORD) {
      return Collections.emptyList();
    }

    return getTags(getNumberOfTags(map.size()), map);
  }

  private static int getNumberOfTags(int numberOfWords) {
    if (numberOfWords < 10) {
      return 1;
    }
    if (numberOfWords < 50) {
      return 2;
    }
    if (numberOfWords < 100) {
      return 3;
    }
    if (numberOfWords < 200) {
      return 4;
    }
    return 5;
  }

  private static List<String> getTags(int numberOfTag, Map<String, Long> wordCountMap) {

    List<String> tags = new ArrayList<>();

    Map<Long, Set<String>> countAndWordsMap = wordCountMap.entrySet().stream()
        .collect(Collectors.groupingBy(
            Entry::getValue,
            Collectors.mapping(Entry::getKey, Collectors.toSet())
        ));

    List<Set<String>> wordSetList = countAndWordsMap.entrySet().stream()
        .sorted(Entry.comparingByKey(Comparator.reverseOrder()))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());

    for (Set<String> wordSet : wordSetList) {
      if (tags.size() + wordSet.size() > numberOfTag) {
        return tags;
      }

      tags.addAll(wordSet);
    }

    return tags;
  }
}
