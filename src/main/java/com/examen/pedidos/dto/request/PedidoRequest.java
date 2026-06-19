package com.examen.pedidos.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {

    // ID del cliente que realiza el pedido.
    // El servicio verificará que este cliente exista en la BD.
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    // Lista de productos con sus cantidades.
    // @NotEmpty = la lista no puede estar vacía (un pedido sin productos no existe)
    // @Valid    = le dice a Spring que también valide las reglas dentro de cada ItemPedidoRequest
    //             Sin @Valid, las anotaciones en ItemPedidoRequest serían ignoradas.
    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<ItemPedidoRequest> items;
}
