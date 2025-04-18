package io.klever.techchallenge.domain.models;

public record ConvertMagicCoinPriceResponse(
    boolean success,
    String terms,
    String privacy,
    ConversionQueryResponseData query,
    ConversionInfoResponseData info,
    double result
  ) {}
