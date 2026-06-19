package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ProductoRequest;
import com.examen.pedidos.dto.response.ProductoResponse;
import com.examen.pedidos.entity.Producto;
import com.examen.pedidos.mapper.ProductoMapper;
import com.examen.pedidos.repository.ProductoRepository;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public BaseResponse<ProductoResponse> crearProducto(ProductoRequest request) {
        Producto producto = productoMapper.toEntity(request);
        Producto productoGuardado = productoRepository.save(producto);

        return BaseResponse.<ProductoResponse>builder()
                .codigo(HttpStatus.CREATED.value())
                .mensaje("Producto registrado correctamente")
                .objeto(productoMapper.toResponse(productoGuardado))
                .build();
    }

    @Override
    public BaseResponse<List<ProductoResponse>> listarProductos() {
        List<ProductoResponse> productos = productoRepository.findByEstado(true)
                .stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());

        return BaseResponse.<List<ProductoResponse>>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Lista de productos activos")
                .objeto(productos)
                .build();
    }
}
