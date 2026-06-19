package com.examen.pedidos.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String correo;

    // Fecha que el sistema asignó automáticamente al crear el cliente
    private LocalDateTime fechaRegistro;
}
