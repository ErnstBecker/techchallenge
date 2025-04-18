package io.klever.techchallenge.application.services;

import io.klever.techchallenge.application.ports.in.GetTopMagicCoinsUseCase;
import io.klever.techchallenge.application.ports.out.HandleCacheOutputPort;
import io.klever.techchallenge.application.ports.out.ConvertMagicPriceOutputPort;
import io.klever.techchallenge.application.ports.out.GetAllMagicCoinsListOutputPort;
import io.klever.techchallenge.domain.models.MagicCoin;
import io.klever.techchallenge.domain.models.GetAllMagicCoinsListResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetTopMagicCoinsService implements GetTopMagicCoinsUseCase {

  private final GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort;
  private final ConvertMagicPriceOutputPort convertMagicPriceOutputPort;
  private final HandleCacheOutputPort handleCacheOutputPort;

  public GetTopMagicCoinsService(
    GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort,
    ConvertMagicPriceOutputPort convertMagicPriceOutputPort,
    HandleCacheOutputPort handleCacheOutputPort
  ) {

    this.getAllMagicCoinsListOutputPort = getAllMagicCoinsListOutputPort;
    this.convertMagicPriceOutputPort = convertMagicPriceOutputPort;
    this.handleCacheOutputPort = handleCacheOutputPort;
  }

  @Override
  public List<MagicCoin> execute(String fiatCurrency, int topLimit) {

    List<MagicCoin> allMagicCoins = GetMagicCoins();

    var topMagicCoins = filterTopMagicCoins(allMagicCoins, topLimit);

    if (fiatCurrency.equals("USD")) return topMagicCoins;

    return convertFiatCurrency(topMagicCoins, fiatCurrency);
  }

  private List<MagicCoin> GetMagicCoins() {

    Optional<List<GetAllMagicCoinsListResponse>> cachedMagicCoins =
      handleCacheOutputPort.getList("TOP_MAGIC_COINS", GetAllMagicCoinsListResponse.class);

    if (cachedMagicCoins.isPresent() && !cachedMagicCoins.get().isEmpty()) {

      return convertToMagicCoins(cachedMagicCoins.get());
    }

    List<GetAllMagicCoinsListResponse> freshCoins = getAllMagicCoinsListOutputPort.execute();
    handleCacheOutputPort.set("TOP_MAGIC_COINS", freshCoins);

    return convertToMagicCoins(freshCoins);
  }

  private List<MagicCoin> filterTopMagicCoins(List<MagicCoin> allMagicCoinsList, int topLimit) {
    return allMagicCoinsList.stream()
      .filter(
        magicCoin -> magicCoin.getRank() >= 1 && magicCoin.getRank() <= topLimit
      ).toList();
  }

  private List<MagicCoin> convertFiatCurrency(List<MagicCoin> topMagicCoins, String fiatCurrency) {
    return topMagicCoins.stream()
      .map(coin -> {
        var convertedCoin = new MagicCoin();
        convertedCoin.setName(coin.getName());
        convertedCoin.setSymbol(coin.getSymbol());
        convertedCoin.setRank(coin.getRank());

        Map<String, Double> quotes = coin.getQuote();
        Double usdPrice = quotes.get("USD");

        var response = convertMagicPriceOutputPort.execute("USD", fiatCurrency, usdPrice);
        System.out.println(response);

        Map<String, Double> convertedQuotes = new HashMap<>(quotes);
        convertedQuotes.put(fiatCurrency, response.result());

        convertedCoin.setQuote(convertedQuotes);
        return convertedCoin;
      })
      .collect(Collectors.toList());
  }

  private List<MagicCoin> convertToMagicCoins(List<GetAllMagicCoinsListResponse> allMagicCoinsListResponses) {

    return allMagicCoinsListResponses.stream()
      .map(magicCoinResponse -> {
        MagicCoin coin = new MagicCoin();
        coin.setName(magicCoinResponse.name());
        coin.setSymbol(magicCoinResponse.symbol());
        coin.setRank(magicCoinResponse.rank());

        Map<String, Double> quote = new HashMap<>();
        if (magicCoinResponse.quotes() != null) {
          magicCoinResponse.quotes().forEach((currency, data) ->
            quote.put(currency, data.price())
          );
        }

        coin.setQuote(quote);
        return coin;
      })
      .collect(Collectors.toList());
  }
}