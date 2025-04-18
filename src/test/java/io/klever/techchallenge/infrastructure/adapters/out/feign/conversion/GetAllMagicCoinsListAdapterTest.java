package io.klever.techchallenge.infrastructure.adapters.out.feign.conversion;

import io.klever.techchallenge.domain.models.GetAllMagicCoinsListResponse;
import io.klever.techchallenge.domain.models.QuoteResponseData;
import io.klever.techchallenge.infrastructure.adapters.out.feign.GetAllMagicCoinsListAdapter;
import io.klever.techchallenge.infrastructure.adapters.out.feign.GetAllMagicCoinsListClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllMagicCoinsListAdapterTest {

  @Mock
  private GetAllMagicCoinsListClient client;

  private GetAllMagicCoinsListAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new GetAllMagicCoinsListAdapter(client);
  }

  @Test
  void shouldReturnCoinsListFromClient() {

    List<GetAllMagicCoinsListResponse> expectedCoins = createMockCoinsList();
    when(client.execute()).thenReturn(expectedCoins);


    List<GetAllMagicCoinsListResponse> result = adapter.execute();


    verify(client).execute();
    assertEquals(expectedCoins, result);
    assertEquals(3, result.size());
  }

  @Test
  void shouldHandleEmptyResponse() {

    List<GetAllMagicCoinsListResponse> emptyList = List.of();
    when(client.execute()).thenReturn(emptyList);


    List<GetAllMagicCoinsListResponse> result = adapter.execute();


    verify(client).execute();
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldPropagateCoinData() {
    // Arrange
    List<GetAllMagicCoinsListResponse> expectedCoins = createMockCoinsList();
    when(client.execute()).thenReturn(expectedCoins);


    List<GetAllMagicCoinsListResponse> result = adapter.execute();


    verify(client).execute();

    GetAllMagicCoinsListResponse bitcoin = result.get(0);
    assertEquals("bitcoin", bitcoin.id());
    assertEquals("Bitcoin", bitcoin.name());
    assertEquals("BTC", bitcoin.symbol());
    assertEquals(1, bitcoin.rank());

    assertTrue(bitcoin.quotes().containsKey("USD"));
    assertEquals(50000.0, bitcoin.quotes().get("USD").price());
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
        "2014-10-06", "2023-10-01", tetherQuotes)
    );
  }
}