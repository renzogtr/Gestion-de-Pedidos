package com.examen.pedidos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    // La descripción es opcional: no tiene @NotBlank
    private String descripcion;

    // @NotNull porque Double puede ser null si no se envía
    // @Positive porque el precio debe ser mayor a 0 (no existen precios negativos)
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    // @Min(0) porque el stock puede empezar en 0 (sin inventario inicial)
    // pero no puede ser negativo (-5 unidades no existe en el mundo real)
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
