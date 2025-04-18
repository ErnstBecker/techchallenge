package io.klever.techchallenge.domain.models;

import java.util.Map;

public record GetAllMagicCoinsListResponse(
  String id,
  String name,
  String symbol,
  int rank,
  long circulatingSupply,
  long totalSupply,
  long maxSupply,
  double betaValue,
  String firstDataAt,
  String lastUpdated,
  // "USD": {QuoteResponseData{...}}
  Map<String, QuoteResponseData> quotes
){ }