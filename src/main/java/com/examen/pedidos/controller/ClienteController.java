package com.examen.pedidos.controller;

import com.examen.pedidos.dto.request.ClienteRequest;
import com.examen.pedidos.dto.response.ClienteResponse;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<BaseResponse<ClienteResponse>> crearCliente(
            @RequestBody @Valid ClienteRequest request) {

        BaseResponse<ClienteResponse> response = clienteService.crearCliente(request);
        return ResponseEntity.status(response.getCodigo()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponse>> buscarPorId(@PathVariable Long id) {
        BaseResponse<ClienteResponse> response = clienteService.buscarPorId(id);
        return ResponseEntity.status(response.getCodigo()).body(response);
    }
}
