package com.examen.pedidos.service;

import com.examen.pedidos.dto.request.ProductoRequest;
import com.examen.pedidos.dto.response.ProductoResponse;
import com.examen.pedidos.response.BaseResponse;

import java.util.List;

public interface ProductoService {

    // Registra un nuevo producto en el catálogo.
    BaseResponse<ProductoResponse> crearProducto(ProductoRequest request);

    // Lista todos los productos activos del catálogo.
    // List<ProductoResponse> dentro de BaseResponse porque son varios objetos.
    BaseResponse<List<ProductoResponse>> listarProductos();
}
