package io.klever.techchallenge.infrastructure.adapters.out.feign.conversion;

import io.klever.techchallenge.domain.models.ConversionInfoResponseData;
import io.klever.techchallenge.domain.models.ConversionQueryResponseData;
import io.klever.techchallenge.domain.models.ConvertMagicCoinPriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConvertMagicCoinPriceAdapterTest {

  @Mock
  private ConvertMagicCoinPriceClient client;

  private ConvertMagicCoinPriceAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new ConvertMagicCoinPriceAdapter(client);
  }

  @Test
  void shouldCallClientWithCorrectParameters() {
    String from = "USD";
    String to = "EUR";
    double amount = 100.0;

    ConvertMagicCoinPriceResponse expectedResponse = new ConvertMagicCoinPriceResponse(
      true,
      "terms",
      "privacy",
      new ConversionQueryResponseData(from, to, 100),
      new ConversionInfoResponseData(System.currentTimeMillis(), 0.85),
      85.0
    );

    when(client.execute(from, to, amount)).thenReturn(expectedResponse);

    ConvertMagicCoinPriceResponse result = adapter.execute(from, to, amount);

    verify(client).execute(from, to, amount);
    assertEquals(expectedResponse, result);
  }

  @Test
  void shouldHandleDifferentCurrencies() {
    String from = "BTC";
    String to = "JPY";
    double amount = 0.5;

    ConvertMagicCoinPriceResponse expectedResponse = new ConvertMagicCoinPriceResponse(
      true,
      "terms",
      "privacy",
      new ConversionQueryResponseData(from, to, 1),
      new ConversionInfoResponseData(System.currentTimeMillis(), 3000000.0),
      1500000.0
    );

    when(client.execute(from, to, amount)).thenReturn(expectedResponse);

    ConvertMagicCoinPriceResponse result = adapter.execute(from, to, amount);

    verify(client).execute(from, to, amount);
    assertEquals(expectedResponse, result);
    assertEquals(1500000.0, result.result());
  }
}