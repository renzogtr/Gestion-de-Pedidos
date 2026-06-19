# API REST de Gestión de Pedidos

API REST para una tienda que permite registrar clientes, productos y crear pedidos con múltiples productos, validando stock y calculando totales automáticamente.

## Tecnologías usadas

- Java 21
- Spring Boot 4.1.0
- Maven
- PostgreSQL
- Spring Data JPA
- Spring Validation
- Lombok
- JUnit 5
- Mockito

## Instrucciones para ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/renzogtr/Gestion-de-Pedidos.git
   cd pedidos
   ```

2. Configurar la base de datos (ver sección siguiente).

3. Configurar las credenciales locales en `src/main/resources/application.properties` (este archivo no se sube al repositorio; usar `application.properties.example` como plantilla).

4. Ejecutar:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. La API quedará disponible en `http://localhost:8080`.

## Configuración de base de datos

Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE db_pedidos;
```

Variables necesarias en `application.properties` (ver `application.properties.example`):

```properties
spring.application.name=pedidos

spring.datasource.url=jdbc:postgresql://localhost:5432/db_pedidos
spring.datasource.username=postgres
spring.datasource.password=tu contraseña postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Con `ddl-auto=update`, Hibernate crea/actualiza automáticamente las tablas (`cliente`, `producto`, `pedido`, `detalle_pedido`) al levantar la aplicación. No es necesario ejecutar un script SQL manual.

## Modelo de datos

- **Cliente**: id, nombre, apellido, dni, correo, fechaRegistro → tiene muchos Pedidos.
- **Producto**: id, nombre, descripcion, precio, stock, estado.
- **Pedido**: id, fechaPedido, estado, total, cliente → pertenece a un Cliente, tiene muchos DetallePedido.
- **DetallePedido**: id, productoId, nombreProducto, cantidad, precioUnitario, subtotal → pertenece a un Pedido.

## Reglas de negocio (creación de pedido)

1. El cliente debe existir.
2. Cada producto del pedido debe existir.
3. La cantidad solicitada debe ser mayor a cero.
4. El producto debe tener stock suficiente.
5. Se calcula el subtotal de cada detalle y el total del pedido.
6. Se descuenta el stock de cada producto.
7. El pedido queda con estado `CREADO`.

## Manejo de errores

Todas las respuestas usan la estructura `BaseResponse<T>`:

```json
{
  "codigo": 200,
  "mensaje": "Pedido creado correctamente",
  "objeto": { }
}
```

| Excepción                     | Código HTTP | Cuándo ocurre                                  |
|--------------------------------|:-----------:|-------------------------------------------------|
| `PedidoNotFoundException`      | 404         | Se busca un pedido que no existe                |
| `StockInsuficienteException`   | 400         | La cantidad pedida supera el stock disponible   |
| `MethodArgumentNotValidException` (validaciones `@Valid`) | 400 | Datos de entrada inválidos (campos vacíos, formato incorrecto, etc.) |
| Excepción no controlada        | 500         | Error inesperado del servidor                   |

## Endpoints disponibles

### Clientes

| Método | Endpoint              | Descripción            |
|--------|------------------------|-------------------------|
| POST   | `/api/clientes`         | Crear cliente            |
| GET    | `/api/clientes/{id}`    | Buscar cliente por ID    |

### Productos

| Método | Endpoint           | Descripción         |
|--------|---------------------|----------------------|
| POST   | `/api/productos`     | Crear producto        |
| GET    | `/api/productos`     | Listar productos      |

### Pedidos

| Método | Endpoint                          | Descripción                  |
|--------|------------------------------------|--------------------------------|
| POST   | `/api/pedidos`                     | Crear pedido                   |
| GET    | `/api/pedidos/{id}`                | Buscar pedido por ID           |
| GET    | `/api/pedidos/cliente/{clienteId}` | Listar pedidos de un cliente   |

## Ejemplos de request JSON

**Crear cliente** — `POST /api/clientes`
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "dni": "12345678",
  "correo": "juan.perez@gmail.com"
}
```

**Crear producto** — `POST /api/productos`
```json
{
  "nombre": "Teclado mecánico",
  "descripcion": "Teclado RGB",
  "precio": 150.00,
  "stock": 20
}
```

**Crear pedido** — `POST /api/pedidos`
```json
{
  "clienteId": 1,
  "items": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 2, "cantidad": 1 }
  ]
}
```

## Pruebas unitarias

El proyecto incluye pruebas con JUnit 5 y Mockito en `PedidoServiceImplTest`:

- `crearPedido_cuandoDatosSonValidos_retornaPedidoCreado()` — happy path
- `crearPedido_cuandoStockEsInsuficiente_lanzaStockInsuficienteException()` — error de stock
- `buscarPedido_cuandoNoExiste_lanzaPedidoNotFoundException()` — pedido no encontrado

Para ejecutarlas:

```bash
mvn test
```

**Evidencia de ejecución (última corrida local):**

```
Test set: com.examen.pedidos.service.impl.PedidoServiceImplTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0

Test set: com.examen.pedidos.PedidosApplicationTests
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## Pruebas en Postman

En la carpeta `postman/` se incluye la colección `Pedidos_API.postman_collection.json`, lista para importar en Postman. Cubre el flujo completo: crear cliente → crear productos → crear pedido → consultar pedido → listar pedidos por cliente → caso de error de stock insuficiente.
