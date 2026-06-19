package com.examen.pedidos.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetallePedidoResponse {

    private Long id;

    // El nombre del producto en el momento de la compra (snapshot histórico)
    private String nombreProducto;

    private Integer cantidad;
    private Double precioUnitario;

    // subtotal = cantidad * precioUnitario (calculado por el servicio)
    private Double subtotal;
}
