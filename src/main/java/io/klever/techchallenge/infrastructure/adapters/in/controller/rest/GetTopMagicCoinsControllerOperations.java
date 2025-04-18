package io.klever.techchallenge.infrastructure.adapters.in.controller.rest;

import io.klever.techchallenge.domain.models.MagicCoin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping({"/rest/v1"})
public interface GetTopMagicCoinsControllerOperations {

  @Operation(
    description = "Retorna as criptomoedas com maior valor de mercado (limite máximo de 50)",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Lista de criptomoedas com maior valor de mercado"
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Requisição contém dados inválidos"
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor"
      )
    }
  )
  @GetMapping(value = "/magic-coins")
  List<MagicCoin> execute(
    @RequestParam(name = "currency", required = false, defaultValue = "USD") String currency,

    @RequestParam(name= "top", required = false, defaultValue = "10")
    @Min(value = 1, message = "O valor mínimo permitido é 1")
    @Max(value = 50, message = "O valor máximo permitido é 50")
    int top
  );
}