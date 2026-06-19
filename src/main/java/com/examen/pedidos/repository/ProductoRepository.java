package com.examen.pedidos.repository;

import com.examen.pedidos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // JpaRepository<Producto, Long>:
    //   Producto = entidad que maneja
    //   Long     = tipo del @Id

    // "findBy" + "Estado" → SELECT * FROM productos WHERE estado = ?
    // Devuelve List porque puede haber muchos productos activos o inactivos.
    // Usar findAll() traería también los inactivos, esto filtra solo los activos (true).
    List<Producto> findByEstado(Boolean estado);
}
