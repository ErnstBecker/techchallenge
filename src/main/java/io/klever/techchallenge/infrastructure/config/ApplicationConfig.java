package io.klever.techchallenge.infrastructure.config;

import io.klever.techchallenge.application.ports.in.GetTopMagicCoinsUseCase;
import io.klever.techchallenge.application.ports.out.HandleCacheOutputPort;
import io.klever.techchallenge.application.ports.out.ConvertMagicPriceOutputPort;
import io.klever.techchallenge.application.ports.out.GetAllMagicCoinsListOutputPort;
import io.klever.techchallenge.application.services.GetTopMagicCoinsService;
import io.klever.techchallenge.application.services.UpdateTopMagicCoinsService;
import io.klever.techchallenge.infrastructure.adapters.out.feign.GetAllMagicCoinsListAdapter;
import io.klever.techchallenge.infrastructure.adapters.out.feign.GetAllMagicCoinsListClient;
import io.klever.techchallenge.infrastructure.adapters.out.feign.conversion.ConvertMagicCoinPriceAdapter;
import io.klever.techchallenge.infrastructure.adapters.out.feign.conversion.ConvertMagicCoinPriceClient;
import io.klever.techchallenge.infrastructure.adapters.out.redis.HandleRedisCacheAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ApplicationConfig {

  @Bean
  public GetTopMagicCoinsUseCase getTopMagicCoinsUseCase(
    GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort,
    ConvertMagicPriceOutputPort convertMagicPriceOutputPort,
    HandleCacheOutputPort handleCacheOutputPort
  ) {

    return new GetTopMagicCoinsService(
      getAllMagicCoinsListOutputPort,
      convertMagicPriceOutputPort,
      handleCacheOutputPort
    );
  }

  @Bean
  public GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort(
    GetAllMagicCoinsListClient getAllMagicCoinsListClient) {
    return new GetAllMagicCoinsListAdapter(getAllMagicCoinsListClient);
  }

  @Bean
  public ConvertMagicPriceOutputPort convertMagicPriceOutputPort(
    ConvertMagicCoinPriceClient convertMagicCoinPriceClient) {
    return new ConvertMagicCoinPriceAdapter(convertMagicCoinPriceClient);
  }

  @Bean
  public HandleCacheOutputPort handleCacheOutputPort(RedisTemplate<String, Object> redisTemplate) {
    return new HandleRedisCacheAdapter(redisTemplate);
  }

  @Bean
  public UpdateTopMagicCoinsService updateTopMagicCoinsService(
    GetAllMagicCoinsListOutputPort getAllMagicCoinsListOutputPort,
    HandleCacheOutputPort handleCacheOutputPort) {
    return new UpdateTopMagicCoinsService(getAllMagicCoinsListOutputPort, handleCacheOutputPort);
  }
}