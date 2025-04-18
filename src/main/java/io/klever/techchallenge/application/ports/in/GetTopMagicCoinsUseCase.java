package io.klever.techchallenge.application.ports.in;

import io.klever.techchallenge.domain.models.MagicCoin;

import java.util.List;

public interface GetTopMagicCoinsUseCase {

  List<MagicCoin> execute(String fiatCurrency, int topLimit);
}
