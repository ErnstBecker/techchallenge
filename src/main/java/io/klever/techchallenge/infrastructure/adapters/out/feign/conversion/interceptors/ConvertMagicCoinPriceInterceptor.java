package io.klever.techchallenge.infrastructure.adapters.out.feign.conversion.interceptors;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertMagicCoinPriceInterceptor {

  @Value("${api.convertMagicCoinsPrice.accessKey}")
  private String accessKey;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> requestTemplate.query("access_key", accessKey);
  }
}
