package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ItemPedidoRequest;
import com.examen.pedidos.dto.request.PedidoRequest;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.entity.Cliente;
import com.examen.pedidos.entity.DetallePedido;
import com.examen.pedidos.entity.Pedido;
import com.examen.pedidos.entity.Producto;
import com.examen.pedidos.exception.PedidoNotFoundException;
import com.examen.pedidos.exception.StockInsuficienteException;
import com.examen.pedidos.mapper.PedidoMapper;
import com.examen.pedidos.repository.ClienteRepository;
import com.examen.pedidos.repository.PedidoRepository;
import com.examen.pedidos.repository.ProductoRepository;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    @Transactional
    public BaseResponse<PedidoResponse> crearPedido(PedidoRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró el cliente con id: " + request.getClienteId()));

        List<DetallePedido> detalles = new ArrayList<>();

        double total = 0.0;

        for (ItemPedidoRequest item : request.getItems()) {

            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "No se encontró el producto con id: " + item.getProductoId()));

            if (item.getCantidad() > producto.getStock()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + producto.getNombre() +
                        "'. Disponible: " + producto.getStock() +
                        ", Solicitado: " + item.getCantidad());
            }

            double subtotal = producto.getPrecio() * item.getCantidad();

            total += subtotal;

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetallePedido detalle = DetallePedido.builder()
                    .productoId(producto.getId())
                    .nombreProducto(producto.getNombre())
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subTotal(subtotal)
                    .build();

            detalles.add(detalle);
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estado("CREADO")
                .total(total)
                .detalles(detalles)
                .build();

        detalles.forEach(detalle -> detalle.setPedido(pedido));

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        return BaseResponse.<PedidoResponse>builder()
                .codigo(HttpStatus.CREATED.value())
                .mensaje("Pedido creado correctamente")
                .objeto(pedidoMapper.toResponse(pedidoGuardado))
                .build();
    }

    @Override
    public BaseResponse<PedidoResponse> buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(
                        "No se encontró el pedido con id: " + id));

        return BaseResponse.<PedidoResponse>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Pedido encontrado")
                .objeto(pedidoMapper.toResponse(pedido))
                .build();
    }

    @Override
    public BaseResponse<List<PedidoResponse>> listarPorCliente(Long clienteId) {
        List<PedidoResponse> pedidos = pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(pedidoMapper::toResponse)
                .collect(Collectors.toList());

        return BaseResponse.<List<PedidoResponse>>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Pedidos del cliente " + clienteId)
                .objeto(pedidos)
                .build();
    }
}
