package io.klever.techchallenge.application.ports.out;

import io.klever.techchallenge.domain.models.GetAllMagicCoinsListResponse;

import java.util.List;

public interface GetAllMagicCoinsListOutputPort {

  List<GetAllMagicCoinsListResponse> execute();
}
