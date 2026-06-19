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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Cliente clientePrueba;
    private Producto productoPrueba;
    private PedidoRequest requestValido;

    @BeforeEach
    void setUp() {
        clientePrueba = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .build();

        productoPrueba = Producto.builder()
                .id(1L)
                .nombre("Teclado mecánico")
                .precio(150.0)
                .stock(10)
                .estado(true)
                .build();

        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProductoId(1L);
        item.setCantidad(2);

        requestValido = new PedidoRequest();
        requestValido.setClienteId(1L);
        requestValido.setItems(List.of(item));
    }

    @Test
    @DisplayName("Crear pedido con datos válidos → debe retornar pedido creado con código 201")
    void crearPedido_cuandoDatosSonValidos_retornaPedidoCreado() {

        DetallePedido detallePrueba = DetallePedido.builder()
                .id(1L)
                .productoId(1L)
                .nombreProducto("Teclado mecánico")
                .cantidad(2)
                .precioUnitario(150.0)
                .subTotal(300.0)
                .build();

        Pedido pedidoGuardado = Pedido.builder()
                .id(1L)
                .cliente(clientePrueba)
                .estado("CREADO")
                .total(300.0)
                .detalles(List.of(detallePrueba))
                .build();

        PedidoResponse pedidoResponseEsperado = PedidoResponse.builder()
                .id(1L)
                .cliente("Juan Pérez")
                .total(300.0)
                .estado("CREADO")
                .detalles(List.of())
                .build();

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clientePrueba));

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(productoPrueba));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(productoPrueba);

        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedidoGuardado);

        when(pedidoMapper.toResponse(pedidoGuardado))
                .thenReturn(pedidoResponseEsperado);

        BaseResponse<PedidoResponse> resultado = pedidoService.crearPedido(requestValido);

        assertNotNull(resultado, "La respuesta no debe ser nula");
        assertEquals(201, resultado.getCodigo(), "El código debe ser 201 (Created)");
        assertEquals("Pedido creado correctamente", resultado.getMensaje());
        assertNotNull(resultado.getObjeto(), "El objeto de respuesta no debe ser nulo");
        assertEquals(300.0, resultado.getObjeto().getTotal(), "El total debe ser 300.0 (2 unidades x S/.150)");
        assertEquals("CREADO", resultado.getObjeto().getEstado(), "El estado inicial del pedido debe ser CREADO");

        verify(clienteRepository).findById(1L);
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(any(Producto.class));
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Crear pedido con stock insuficiente → debe lanzar StockInsuficienteException")
    void crearPedido_cuandoStockEsInsuficiente_lanzaStockInsuficienteException() {

        Producto productoSinStock = Producto.builder()
                .id(1L)
                .nombre("Teclado mecánico")
                .precio(150.0)
                .stock(5)
                .estado(true)
                .build();

        ItemPedidoRequest itemExcesivo = new ItemPedidoRequest();
        itemExcesivo.setProductoId(1L);
        itemExcesivo.setCantidad(20);

        PedidoRequest requestConExceso = new PedidoRequest();
        requestConExceso.setClienteId(1L);
        requestConExceso.setItems(List.of(itemExcesivo));

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(clientePrueba));

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(productoSinStock));

        StockInsuficienteException excepcion = assertThrows(
                StockInsuficienteException.class,
                () -> pedidoService.crearPedido(requestConExceso),
                "Debe lanzar StockInsuficienteException cuando la cantidad supera el stock"
        );

        assertTrue(excepcion.getMessage().contains("Teclado mecánico"),
                "El mensaje debe mencionar el nombre del producto");

        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Buscar pedido que no existe → debe lanzar PedidoNotFoundException")
    void buscarPedido_cuandoNoExiste_lanzaPedidoNotFoundException() {

        when(pedidoRepository.findById(99L))
                .thenReturn(Optional.empty());

        PedidoNotFoundException excepcion = assertThrows(
                PedidoNotFoundException.class,
                () -> pedidoService.buscarPorId(99L),
                "Debe lanzar PedidoNotFoundException cuando el pedido no existe"
        );

        assertTrue(excepcion.getMessage().contains("99"),
                "El mensaje de error debe indicar el ID que no se encontró");

        verify(pedidoRepository).findById(99L);
    }
}
