package io.klever.techchallenge.infrastructure.adapters.out.feign.conversion;

import io.klever.techchallenge.infrastructure.adapters.out.feign.conversion.interceptors.ConvertMagicCoinPriceInterceptor;
import io.klever.techchallenge.domain.models.ConvertMagicCoinPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
  name = "convertMagicCoinsPriceClient",
  url = "${api.convertMagicCoinsPrice.baseUrl}",
  configuration = ConvertMagicCoinPriceInterceptor.class
)
public interface ConvertMagicCoinPriceClient {

  @GetMapping("${api.convertMagicCoinsPrice.endpoint}")
  ConvertMagicCoinPriceResponse execute(
    @RequestParam("from") String from,
    @RequestParam("to") String to,
    @RequestParam("amount") double amount
  );
}


