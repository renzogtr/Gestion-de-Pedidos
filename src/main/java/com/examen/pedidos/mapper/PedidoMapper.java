package com.examen.pedidos.mapper;

import com.examen.pedidos.dto.response.DetallePedidoResponse;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.entity.DetallePedido;
import com.examen.pedidos.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {

        List<DetallePedidoResponse> detalles = pedido.getDetalles()
                .stream()
                .map(this::toDetalleResponse)
                .collect(Collectors.toList());

        return PedidoResponse.builder()
                .id(pedido.getId())
                .cliente(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .fechaPedido(pedido.getFechaPedido())
                .detalles(detalles)
                .build();
    }

    private DetallePedidoResponse toDetalleResponse(DetallePedido detalle) {
        return DetallePedidoResponse.builder()
                .id(detalle.getId())
                .nombreProducto(detalle.getNombreProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getSubTotal())
                .build();
    }
}
