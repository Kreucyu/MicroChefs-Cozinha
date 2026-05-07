package com.cozinha.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record RecoveryPedidoDTO(
        @NotNull long id,
        @NotNull LocalDate dataDoPedido,
        @NotNull List<RecoveryItemPedidoDTO> itens
) {
}
