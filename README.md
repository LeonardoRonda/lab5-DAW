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

