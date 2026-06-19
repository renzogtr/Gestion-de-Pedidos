package com.examen.pedidos.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;

    // El stock se muestra para que el frontend pueda indicar disponibilidad
    private Integer stock;

    // true = disponible, false = retirado del catálogo
    private Boolean estado;
}
