package io.klever.techchallenge.infrastructure.adapters.in.controller.rest;

import io.klever.techchallenge.application.ports.in.GetTopMagicCoinsUseCase;
import io.klever.techchallenge.domain.models.MagicCoin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetTopMagicCoinsController implements GetTopMagicCoinsControllerOperations {

  private final GetTopMagicCoinsUseCase service;

  public GetTopMagicCoinsController(GetTopMagicCoinsUseCase service) {

    this.service = service;
  }

  @Override
  public List<MagicCoin> execute(String currency, int top) {
    return service.execute(currency, top);
  }
}
