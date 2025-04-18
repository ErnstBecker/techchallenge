package io.klever.techchallenge.domain.models;

import java.util.Map;

public class MagicCoin {

  private String name;
  private String symbol;
  private int rank;
  private Map<String, Double> quote;

  public MagicCoin() {}

  public MagicCoin(String name, String symbol, int rank, Map<String, Double> quote) {
    this.name = name;
    this.symbol = symbol;
    this.rank = rank;
    this.quote = quote;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }


  public Map<String, Double> getQuote() {
    return quote;
  }

  public void setQuote(Map<String, Double> quote) {
    this.quote = quote;
  }
}
