package io.klever.techchallenge.application.services;

import io.klever.techchallenge.application.ports.out.ConvertMagicPriceOutputPort;
import io.klever.techchallenge.application.ports.out.GetAllMagicCoinsListOutputPort;
import io.klever.techchallenge.application.ports.out.HandleCacheOutputPort;
import io.klever.techchallenge.domain.models.ConversionInfoResponseData;
import io.klever.techchallenge.domain.models.ConversionQueryResponseData;
import io.klever.techchallenge.domain.models.ConvertMagicCoinPriceResponse;
import io.klever.techchallenge.domain.models.GetAllMagicCoinsListResponse;
import io.klever.techchallenge.domain.models.MagicCoin;
import io.klever.techchallenge.domain.models.QuoteResponseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTopMagicCoinsServiceTest {

  @Mock
  private GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort;

  @Mock
  private ConvertMagicPriceOutputPort convertMagicPriceOutputPort;

  @Mock
  private HandleCacheOutputPort handleCacheOutputPort;

  private GetTopMagicCoinsService service;

  @BeforeEach
  void setUp() {
    service = new GetTopMagicCoinsService(
      getAllMagicCoinsListOutputPort,
      convertMagicPriceOutputPort,
      handleCacheOutputPort
    );
  }

  @Test
  void shouldReturnTopCoinsFromCacheWhenAvailable() {

    List<GetAllMagicCoinsListResponse> cachedCoins = createMockCoinsList();
    when(handleCacheOutputPort.getList(eq("TOP_MAGIC_COINS"), eq(GetAllMagicCoinsListResponse.class)))
      .thenReturn(Optional.of(cachedCoins));


    List<MagicCoin> result = service.execute("USD", 3);


    assertEquals(3, result.size());
    assertEquals(1, result.get(0).getRank());
    assertEquals(2, result.get(1).getRank());
    assertEquals(3, result.get(2).getRank());

    verify(handleCacheOutputPort).getList(eq("TOP_MAGIC_COINS"), eq(GetAllMagicCoinsListResponse.class));
    verify(getAllMagicCoinsListOutputPort, never()).execute();
  }

  @Test
  void shouldFetchCoinsFromApiWhenCacheIsEmpty() {
    when(handleCacheOutputPort.getList(eq("TOP_MAGIC_COINS"), eq(GetAllMagicCoinsListResponse.class)))
      .thenReturn(Optional.empty());

    List<GetAllMagicCoinsListResponse> apiCoins = createMockCoinsList();
    when(getAllMagicCoinsListOutputPort.execute()).thenReturn(apiCoins);

    List<MagicCoin> result = service.execute("USD", 2);

    assertEquals(2, result.size());
    assertEquals(1, result.get(0).getRank());
    assertEquals(2, result.get(1).getRank());

    verify(handleCacheOutputPort).getList(eq("TOP_MAGIC_COINS"), eq(GetAllMagicCoinsListResponse.class));
    verify(getAllMagicCoinsListOutputPort).execute();
    verify(handleCacheOutputPort).set(eq("TOP_MAGIC_COINS"), eq(apiCoins));
  }

  @Test
  void shouldConvertCurrencyWhenNotUSD() {
    // Arrange
    List<GetAllMagicCoinsListResponse> cachedCoins = createMockCoinsList();
    when(handleCacheOutputPort.getList(eq("TOP_MAGIC_COINS"), eq(GetAllMagicCoinsListResponse.class)))
      .thenReturn(Optional.of(cachedCoins));

    // Setup currency conversion mock
    ConvertMagicCoinPriceResponse conversionResponse = new ConvertMagicCoinPriceResponse(
      true,
      "terms",
      "privacy",
      new ConversionQueryResponseData("USD", "EUR", 1),
      new ConversionInfoResponseData(System.currentTimeMillis(), 0.85),
      0.85
    );

    when(convertMagicPriceOutputPort.execute(eq("USD"), eq("EUR"), anyDouble()))
      .thenReturn(conversionResponse);

    // Act
    List<MagicCoin> result = service.execute("EUR", 2);

    // Assert
    assertEquals(2, result.size());
    assertTrue(result.get(0).getQuote().containsKey("EUR"));
    assertEquals(0.85, result.get(0).getQuote().get("EUR"));

    verify(convertMagicPriceOutputPort, times(2)).execute(eq("USD"), eq("EUR"), anyDouble());
  }

  @Test
  void shouldFilterCoinsByRank() {
    List<GetAllMagicCoinsListResponse> cachedCoins = createMockCoinsList();
    when(handleCacheOutputPort.getList(eq("TOP_MAGIC_COINS"), eq(GetAllMagicCoinsListResponse.class)))
      .thenReturn(Optional.of(cachedCoins));

    List<MagicCoin> result = service.execute("USD", 1);

    assertEquals(1, result.size());
    assertEquals(1, result.get(0).getRank());
    assertEquals("Bitcoin", result.get(0).getName());
  }

  private List<GetAllMagicCoinsListResponse> createMockCoinsList() {
    Map<String, QuoteResponseData> bitcoinQuotes = new HashMap<>();
    bitcoinQuotes.put("USD", new QuoteResponseData(
      50000.0, 30000000000.0, 5.0, 950000000000.0,
      2.3, 0.1, 0.2, 0.5, 1.5, 2.0, 3.0, 5.0,
      10.0, 20.0, 69000.0, "2021-11-10", -27.5));

    Map<String, QuoteResponseData> ethereumQuotes = new HashMap<>();
    ethereumQuotes.put("USD", new QuoteResponseData(
      3000.0, 15000000000.0, 3.0, 350000000000.0,
      1.5, 0.1, 0.3, 0.7, 1.2, 1.8, 2.5, 4.0,
      8.0, 15.0, 4800.0, "2021-11-15", -37.5));

    Map<String, QuoteResponseData> tetherQuotes = new HashMap<>();
    tetherQuotes.put("USD", new QuoteResponseData(
      1.0, 80000000000.0, 0.1, 80000000000.0,
      0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
      0.0, 0.0, 1.1, "2021-05-05", -9.0));

    Map<String, QuoteResponseData> bnbQuotes = new HashMap<>();
    bnbQuotes.put("USD", new QuoteResponseData(
      400.0, 2000000000.0, 2.0, 60000000000.0,
      1.0, 0.1, 0.2, 0.4, 0.8, 1.0, 1.5, 3.0,
      6.0, 12.0, 690.0, "2021-05-10", -42.0));

    return Arrays.asList(
      new GetAllMagicCoinsListResponse(
        "bitcoin", "Bitcoin", "BTC", 1,
        19000000, 19000000, 21000000, 1.0,
        "2009-01-03", "2023-10-01", bitcoinQuotes),
      new GetAllMagicCoinsListResponse(
        "ethereum", "Ethereum", "ETH", 2,
        120000000, 120000000, 0, 1.1,
        "2015-07-30", "2023-10-01", ethereumQuotes),
      new GetAllMagicCoinsListResponse(
        "tether", "Tether", "USDT", 3,
        80000000000L, 80000000000L, 0, 0.0,
        "2014-10-06", "2023-10-01", tetherQuotes),
      new GetAllMagicCoinsListResponse(
        "bnb", "BNB", "BNB", 4,
        150000000, 150000000, 200000000, 0.9,
        "2017-07-25", "2023-10-01", bnbQuotes)
    );
  }
}
