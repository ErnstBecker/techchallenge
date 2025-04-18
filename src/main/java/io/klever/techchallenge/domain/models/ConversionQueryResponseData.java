package io.klever.techchallenge.domain.models;

public record ConversionQueryResponseData(
    String from,
    String to,
    int amount
  ) {}
