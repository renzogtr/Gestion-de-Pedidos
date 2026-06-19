package com.examen.pedidos.service;

import com.examen.pedidos.dto.request.PedidoRequest;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.response.BaseResponse;

import java.util.List;

public interface PedidoService {

    // Crea un pedido aplicando todas las reglas de negocio del examen:
    // cliente existe, productos existen, stock suficiente, calcula total,
    // descuenta stock, estado = "CREADO".
    BaseResponse<PedidoResponse> crearPedido(PedidoRequest request);

    // Busca un pedido por ID. Lanza PedidoNotFoundException si no existe.
    BaseResponse<PedidoResponse> buscarPorId(Long id);

    // Lista todos los pedidos de un cliente específico.
    BaseResponse<List<PedidoResponse>> listarPorCliente(Long clienteId);
}
