package com.cozinha.dto;

import java.time.LocalDate;
import java.util.List;

public record RecoveryPedidoDTO(
        long id,
        LocalDate dataDoPedido,
        List<RecoveryItemPedidoDTO> itens
) {
}
