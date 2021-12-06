package com.closememo.autotag.infra.analyzer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.constant.SYMBOL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WordAnalyzer {

  private final static String GENERAL_NOUN = SYMBOL.NNG; // 일반명사
  private final static String PROPER_NOUN = SYMBOL.NNP; // 고유명사

  private final Komoran komoran;

  public WordAnalyzer(Komoran komoran) {
    this.komoran = komoran;
  }

  public Map<String, Long> getWordCountMap(String content) {
    KomoranResult result = komoran.analyze(content);

    List<Token> tokens = result.getTokenList();

    return tokens.stream()
        .filter(WordAnalyzer::needToCount)
        .collect(Collectors.groupingBy(Token::getMorph, Collectors.counting()));
  }

  private static boolean needToCount(Token token) {
    return GENERAL_NOUN.equals(token.getPos())
        || PROPER_NOUN.equals(token.getPos());
  }
}
