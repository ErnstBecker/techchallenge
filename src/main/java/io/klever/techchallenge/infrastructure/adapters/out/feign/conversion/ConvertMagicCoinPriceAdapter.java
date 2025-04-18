package io.klever.techchallenge.infrastructure.adapters.out.feign.conversion;

import io.klever.techchallenge.application.ports.out.ConvertMagicPriceOutputPort;
import io.klever.techchallenge.domain.models.ConvertMagicCoinPriceResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConvertMagicCoinPriceAdapter implements ConvertMagicPriceOutputPort {

  private final ConvertMagicCoinPriceClient client;


  @Override
  public ConvertMagicCoinPriceResponse execute(String from, String to, double amount) {

    return client.execute(from, to, amount);
  }

}
