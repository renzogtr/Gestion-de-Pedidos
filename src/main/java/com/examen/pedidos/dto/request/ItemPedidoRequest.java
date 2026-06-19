package com.examen.pedidos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoRequest {

    // Qué producto se quiere comprar
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    // Cuántas unidades se quieren comprar.
    // @Min(1) porque la regla de negocio dice: cantidad debe ser mayor a 0.
    // Pedir 0 unidades no tiene sentido.
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
