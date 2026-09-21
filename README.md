# lab5-DAW

API REST de Productos con Spring Boot 4, MySQL y JPA

## Descripción
Este proyecto implementa una API REST para gestión de productos con las siguientes características:
- CRUD completo de productos
- Validación de datos con Bean Validation
- Búsqueda de productos por nombre
- Persistencia en MySQL con Spring Data JPA

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL 8.0
- Maven

## Configuración de Base de Datos
Crear la base de datos en MySQL:
```sql
CREATE DATABASE laboratorio_api;
```

Configuración en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/laboratorio_api?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1111
spring.jpa.hibernate.ddl-auto=update
```

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos los productos |
| GET | `/api/productos/{id}` | Obtener producto por ID |
| GET | `/api/productos/buscar?nombre=xxx` | Buscar productos por nombre (parcial, case-insensitive) |
| POST | `/api/productos` | Crear nuevo producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |

### Ejemplo POST /api/productos
```json
{
  "nombre": "Laptop",
  "precio": 2500.00,
  "stock": 10,
  "categoria": "Electrónica"
}
```

## Ejecución
```bash
./mvnw spring-boot:run
```
La aplicación inicia en `http://localhost:8080`

---

## Respuestas a las Preguntas

### ¿Por qué usamos un DTO en lugar de exponer directamente la entidad?
1. **Desacoplamiento**: La API no expone la estructura interna de la base de datos. Cambios en la entidad no rompen el contrato de la API.
2. **Seguridad**: Evita exponer campos sensibles (ej. passwords, IDs internos, fechas de auditoría).
3. **Validación independiente**: Las anotaciones de validación (`@NotBlank`, `@Positive`) están en el DTO, no en la entidad JPA.
4. **Formato específico**: Permite transformar datos (ej. fechas en formato específico, campos calculados) sin afectar la entidad.
5. **Versionado**: Facilita mantener múltiples versiones de la API con DTOs diferentes para la misma entidad.

### ¿Qué función cumple @Valid?
Valida automáticamente el objeto anotado (en este caso el `ProductoDTO` en el `@RequestBody`) usando las restricciones de Bean Validation (`@NotBlank`, `@Positive`, `@Min`, etc.) antes de ejecutar el método del controlador. Si la validación falla, Spring lanza `MethodArgumentNotValidException` y retorna **400 Bad Request** con los detalles de error.

### ¿Qué pasaría si eliminamos @RestControllerAdvice?
Perderíamos el **manejo global de excepciones**. Los errores como:
- `MethodArgumentNotValidException` (validación fallida)
- `EntityNotFoundException`
- `DataIntegrityViolationException`
- Excepciones genéricas

Retornarían **500 Internal Server Error** con stack trace HTML (página de error por defecto de Spring Boot) en lugar de respuestas JSON estructuradas y códigos HTTP apropiados (400, 404, 409).

### ¿Qué hace @Autowired?
Inyecta automáticamente las dependencias (beans) declaradas. En este proyecto:
- `@Autowired private ProductoService service;` → Spring busca un bean de tipo `ProductoService` y lo inyecta en el controlador.
- Funciona por **tipo** (clase/interfaz). Si hay múltiples implementaciones, requiere `@Qualifier` o `@Primary`.

### ¿Cuál es la diferencia entre save(), findById(), findAll()?

| Método | Descripción | Retorno |
|--------|-------------|---------|
| `save(T entity)` | **Inserta** si el ID es null/nuevo, **actualiza** si el ID existe (merge). Gestiona transacción. | Entidad guardada (con ID generado) |
| `findById(ID id)` | Busca por clave primaria. Retorna `Optional<T>` (puede estar vacío). | `Optional<T>` |
| `findAll()` | Retorna **todas** las entidades de la tabla. Sin paginación (cuidado con tablas grandes). | `List<T>` |