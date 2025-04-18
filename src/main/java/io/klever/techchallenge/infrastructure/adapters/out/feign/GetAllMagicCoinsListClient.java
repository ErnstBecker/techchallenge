package io.klever.techchallenge.infrastructure.adapters.out.feign;

import io.klever.techchallenge.domain.models.GetAllMagicCoinsListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "getAllMagicCoinsListClient", url = "${api.getAllMagicCoins.baseUrl}")
public interface GetAllMagicCoinsListClient {

  @GetMapping("${api.getAllMagicCoins.endpoint}")
  List<GetAllMagicCoinsListResponse> execute();
}
