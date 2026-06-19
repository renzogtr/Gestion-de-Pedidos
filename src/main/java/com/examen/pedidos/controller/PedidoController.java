package com.examen.pedidos.controller;

import com.examen.pedidos.dto.request.PedidoRequest;
import com.examen.pedidos.dto.response.PedidoResponse;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<BaseResponse<PedidoResponse>> crearPedido(
            @RequestBody @Valid PedidoRequest request) {

        BaseResponse<PedidoResponse> response = pedidoService.crearPedido(request);
        return ResponseEntity.status(response.getCodigo()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PedidoResponse>> buscarPorId(@PathVariable Long id) {
        BaseResponse<PedidoResponse> response = pedidoService.buscarPorId(id);
        return ResponseEntity.status(response.getCodigo()).body(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<BaseResponse<List<PedidoResponse>>> listarPorCliente(
            @PathVariable Long clienteId) {

        BaseResponse<List<PedidoResponse>> response = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.status(response.getCodigo()).body(response);
    }
}
