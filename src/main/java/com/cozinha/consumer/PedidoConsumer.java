package com.cozinha.consumer;

import com.cozinha.dto.RecoveryPedidoDTO;
import com.cozinha.exceptions.PedidoIncompletoException;
import com.cozinha.producer.PedidoProducer;
import com.cozinha.service.CozinhaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Component
@EnableRetry
public class PedidoConsumer {

    @Autowired
    private CozinhaService cozinhaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PedidoProducer pedidoProducer;

    @RabbitListener(queues = { "cozinha-queue" })
    public void receberPedido(@Payload String pedidoJson) {
        RecoveryPedidoDTO pedido = objectMapper.readValue(pedidoJson, RecoveryPedidoDTO.class);
        if(pedido.id() == 0 || pedido.itens() == null || pedido.dataDoPedido() == null) {
            pedidoProducer.dlqSender(pedidoJson);
            throw new PedidoIncompletoException("Dados incompletos");
        }
        if(pedido.dataDoPedido().equals(LocalDate.parse("0001-01-01"))) {
            pedidoProducer.dlqSender(pedidoJson);
            System.out.println("sending");
            return;
        }
        cozinhaService.realizarPedido(pedido);

    }
}
