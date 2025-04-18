package io.klever.techchallenge.application.ports.out;

import io.klever.techchallenge.domain.models.ConvertMagicCoinPriceResponse;


public interface ConvertMagicPriceOutputPort {

  ConvertMagicCoinPriceResponse execute(String from, String to, double amount);
}
