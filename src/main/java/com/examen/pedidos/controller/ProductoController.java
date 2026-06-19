package com.examen.pedidos.controller;

import com.examen.pedidos.dto.request.ProductoRequest;
import com.examen.pedidos.dto.response.ProductoResponse;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<BaseResponse<ProductoResponse>> crearProducto(
            @RequestBody @Valid ProductoRequest request) {

        BaseResponse<ProductoResponse> response = productoService.crearProducto(request);
        return ResponseEntity.status(response.getCodigo()).body(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductoResponse>>> listarProductos() {
        BaseResponse<List<ProductoResponse>> response = productoService.listarProductos();
        return ResponseEntity.status(response.getCodigo()).body(response);
    }
}
