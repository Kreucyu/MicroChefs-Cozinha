package com.cozinha.service;

import com.cozinha.dto.DLQSupportDTO;
import com.cozinha.dto.RecoveryPedidoDTO;
import com.cozinha.dto.UpdatePedidoDTO;
import com.cozinha.entities.StatusPedido;
import com.cozinha.exceptions.InfraException;
import com.cozinha.producer.PedidoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CozinhaService {

    @Autowired
    private PedidoProducer pedidoProducer;

    public void realizarPedido(RecoveryPedidoDTO pedido) {

        System.out.println(pedido);

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pedidoProducer.enviarAtualizacao(new UpdatePedidoDTO(pedido.id(), StatusPedido.EM_PREPARO));
        System.out.println("Atualizado (EM_PREPARO)");

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pedidoProducer.enviarAtualizacao(new UpdatePedidoDTO(pedido.id(), StatusPedido.PRONTO));
        System.out.println("Atualizado (PRONTO)");
    }

    public void processarErro(Exception e, String JSON) {
        String tipo = "DATA_ERROR";
        if (e instanceof InfraException) {
            tipo = "INFRA_ERROR";
        }
        DLQSupportDTO dlqSupportDTO = new DLQSupportDTO(
                "PEDIDO_STATUS_PAGO",
                "cozinha-queue",
                tipo,
                e.getMessage(),
                JSON,
                LocalDateTime.now()
        );
        pedidoProducer.dlqSender(dlqSupportDTO);
        System.out.println(dlqSupportDTO);
    }
}
