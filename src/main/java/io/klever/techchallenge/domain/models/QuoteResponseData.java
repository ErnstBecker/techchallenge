package io.klever.techchallenge.domain.models;

public record QuoteResponseData(
  double price,
  double volume24h,
  double volume24hChange24h,
  double marketCap,
  double marketCapChange24h,
  double percentChange15m,
  double percentChange30m,
  double percentChange1h,
  double percentChange6h,
  double percentChange12h,
  double percentChange24h,
  double percentChange7d,
  double percentChange30d,
  double percentChange1y,
  Double athPrice,
  String athDate,
  Double percentFromPriceAth
) { }