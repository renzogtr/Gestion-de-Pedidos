package com.examen.pedidos.exception;

// Hereda de RuntimeException para ser una excepción NO verificada (unchecked).
// Ventaja: no obliga a usar try-catch donde se lanza. Se propaga sola
// hasta que el GlobalExceptionHandler la atrapa.
public class PedidoNotFoundException extends RuntimeException {

    // Constructor que recibe el mensaje de error.
    // super(mensaje) pasa el texto al constructor de RuntimeException,
    // que lo guarda internamente. Se recupera con getMessage().
    public PedidoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
