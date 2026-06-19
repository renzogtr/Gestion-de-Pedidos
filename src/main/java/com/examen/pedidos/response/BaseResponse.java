package com.examen.pedidos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    // Código HTTP: 200 = éxito, 404 = no encontrado, 400 = dato malo del usuario
    private Integer codigo;

    // Mensaje legible para el desarrollador que consume la API
    private String mensaje;

    // El dato real de la respuesta. <T> permite que sea cualquier tipo:
    // PedidoResponse, ClienteResponse, List<ProductoResponse>, null, etc.
    private T objeto;
}
