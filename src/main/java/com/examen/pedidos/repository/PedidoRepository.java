package com.examen.pedidos.repository;

import com.examen.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // JpaRepository<Pedido, Long>:
    //   Pedido = entidad que maneja
    //   Long   = tipo del @Id

    // Endpoint del examen: GET /api/pedidos/cliente/{clienteId}
    // Spring lee el nombre: "findBy" + "Cliente" + "Id"
    // Genera: SELECT * FROM pedidos WHERE cliente_id = ?
    // Devuelve List porque un cliente puede tener MUCHOS pedidos.
    List<Pedido> findByClienteId(Long clienteId);
}
