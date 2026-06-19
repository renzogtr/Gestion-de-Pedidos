package com.examen.pedidos.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PedidoResponse {

    private Long id;

    // El nombre completo del cliente (nombre + apellido)
    // No devolvemos el objeto Cliente completo para evitar datos innecesarios
    private String cliente;

    private Double total;

    // "CREADO" al momento de crear el pedido (regla de negocio del examen)
    private String estado;

    private LocalDateTime fechaPedido;

    // Lista con cada producto comprado y su subtotal
    private List<DetallePedidoResponse> detalles;
}
