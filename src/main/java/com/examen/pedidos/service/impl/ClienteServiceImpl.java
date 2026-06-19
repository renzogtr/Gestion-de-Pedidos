package com.examen.pedidos.service.impl;

import com.examen.pedidos.dto.request.ClienteRequest;
import com.examen.pedidos.dto.response.ClienteResponse;
import com.examen.pedidos.entity.Cliente;
import com.examen.pedidos.mapper.ClienteMapper;
import com.examen.pedidos.repository.ClienteRepository;
import com.examen.pedidos.response.BaseResponse;
import com.examen.pedidos.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public BaseResponse<ClienteResponse> crearCliente(ClienteRequest request) {
        if (clienteRepository.findByDni(request.getDni()).isPresent()) {
            return BaseResponse.<ClienteResponse>builder()
                    .codigo(HttpStatus.BAD_REQUEST.value())
                    .mensaje("Ya existe un cliente con el DNI: " + request.getDni())
                    .objeto(null)
                    .build();
        }

        if (clienteRepository.findByCorreo(request.getCorreo()).isPresent()) {
            return BaseResponse.<ClienteResponse>builder()
                    .codigo(HttpStatus.BAD_REQUEST.value())
                    .mensaje("Ya existe un cliente con el correo: " + request.getCorreo())
                    .objeto(null)
                    .build();
        }

        Cliente cliente = clienteMapper.toEntity(request);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        ClienteResponse response = clienteMapper.toResponse(clienteGuardado);

        return BaseResponse.<ClienteResponse>builder()
                .codigo(HttpStatus.CREATED.value())
                .mensaje("Cliente registrado correctamente")
                .objeto(response)
                .build();
    }

    @Override
    public BaseResponse<ClienteResponse> buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el cliente con id: " + id));

        return BaseResponse.<ClienteResponse>builder()
                .codigo(HttpStatus.OK.value())
                .mensaje("Cliente encontrado")
                .objeto(clienteMapper.toResponse(cliente))
                .build();
    }
}
