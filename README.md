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

Configura las credenciales de MySQL en el entorno antes de iniciar la aplicación:
```text
DB_URL=jdbc:mysql://127.0.0.1:3306/laboratorio_api?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=tu_contraseña_de_mysql
```
En IntelliJ, añade `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` en **Run | Edit Configurations | Environment variables**. Spring generará las tablas de las entidades al iniciar.

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos los productos |
| GET | `/api/productos/{id}` | Obtener producto por ID |
| GET | `/api/productos/buscar?nombre=xxx` | Buscar productos por nombre (parcial, case-insensitive) |
| POST | `/api/productos` | Crear nuevo producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |

Todas las solicitudes a `/api/productos` requieren los encabezados `Usuario` y `Rol`.

| Usuario | Rol | Acceso |
|---------|-----|--------|
| Leo     | ADMIN | Listar, crear, consultar, buscar, actualizar y eliminar |
| Ana     | USER | Listar y crear |
| Luis    | USER | Listar y crear |

La API responde `401` si falta alguno de los encabezados y `403` si el usuario y rol no coinciden o el rol no tiene acceso a la operación.

Ejemplo de encabezados para Postman:
```text
Usuario: Ana
Rol: USER
```

La auditoría registra acción, método, fecha, detalle y usuario. Al iniciar con `spring.jpa.hibernate.ddl-auto=update`, JPA incorpora la columna `usuario` en `auditoria_log`; si se prefiere actualizarla manualmente, ejecutar una sola vez:
```sql
ALTER TABLE auditoria_log ADD usuario VARCHAR(100);
```

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

