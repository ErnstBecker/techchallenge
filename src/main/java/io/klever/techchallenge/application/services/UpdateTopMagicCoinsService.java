package io.klever.techchallenge.application.services;

import io.klever.techchallenge.application.ports.out.GetAllMagicCoinsListOutputPort;
import io.klever.techchallenge.application.ports.out.HandleCacheOutputPort;
import org.springframework.scheduling.annotation.Scheduled;

public class UpdateTopMagicCoinsService {

  private final GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort;
  private final HandleCacheOutputPort handleCacheOutputPort;

  public UpdateTopMagicCoinsService(GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort, HandleCacheOutputPort handleCacheOutputPort) {
    this.getAllMagicCoinsListOutputPort = getAllMagicCoinsListOutputPort;
    this.handleCacheOutputPort = handleCacheOutputPort;
  }

  @Scheduled(fixedRate = 60000)
  public void updateMagicCoinsList() {

    var allMagicCoinsList = getAllMagicCoinsListOutputPort.execute();
    handleCacheOutputPort.set("TOP_MAGIC_COINS", allMagicCoinsList);
  }
}

