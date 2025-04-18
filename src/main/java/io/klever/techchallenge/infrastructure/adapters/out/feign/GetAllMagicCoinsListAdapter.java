package io.klever.techchallenge.infrastructure.adapters.out.feign;

import io.klever.techchallenge.application.ports.out.GetAllMagicCoinsListOutputPort;
import io.klever.techchallenge.domain.models.GetAllMagicCoinsListResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetAllMagicCoinsListAdapter implements GetAllMagicCoinsListOutputPort {

  private final GetAllMagicCoinsListClient client;


  @Override
  public List<GetAllMagicCoinsListResponse> execute() {

    return client.execute();
  }
}
