package com.examen.pedidos.service;

import com.examen.pedidos.dto.request.ClienteRequest;
import com.examen.pedidos.dto.response.ClienteResponse;
import com.examen.pedidos.response.BaseResponse;

// Interfaz: define QUÉ puede hacer el servicio, sin decir CÓMO lo hace.
// Principio O de SOLID: abierto para extensión, cerrado para modificación.
// Principio D de SOLID: el Controlador depende de esta abstracción, no de ClienteServiceImpl.
public interface ClienteService {

    // Registra un nuevo cliente en el sistema.
    // Recibe el formulario del usuario y devuelve el cliente creado envuelto en BaseResponse.
    BaseResponse<ClienteResponse> crearCliente(ClienteRequest request);

    // Busca un cliente por su ID.
    // Si no existe, la implementación lanzará una excepción apropiada.
    BaseResponse<ClienteResponse> buscarPorId(Long id);
}
