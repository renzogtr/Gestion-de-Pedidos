package com.examen.pedidos.exception;

// Se lanza cuando el cliente pide más unidades de las que hay en stock.
// Regla de negocio del examen: si cantidad solicitada > producto.getStock()
// → lanzar esta excepción y NO guardar el pedido.
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
