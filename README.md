🎯 Sistema de Ventas de Lotería — Backend (Spring Boot)

Proyecto desarrollado como parte de la Prueba Técnica – Equipo de Desarrollo (Konex Innovation).

Implementa una API REST para gestionar sorteos, billetes, clientes y ventas de una lotería.
Desarrollado con Java 17 y Spring Boot 3.4, utilizando H2 Database en memoria para persistencia temporal.

🧩 Características principales

CRUD de Sorteos, Billetes y Clientes

Registro de ventas de billetes

Cambio automático de estado del billete (DISPONIBLE → VENDIDO)

Consulta de historial de billetes vendidos por cliente

Manejador global de excepciones (GlobalExceptionHandler) con respuestas JSON consistentes

Pruebas unitarias completas con JUnit 5 + Mockito

Uso de @MockitoBean (nuevo en Spring Boot 3.4)

Cobertura de pruebas superior al 95 %

🗂️ Estructura del proyecto

src/
├── main/
│ ├── java/org/konex/sistemaloteria/
│ │ ├── billete/
│ │ │ ├── controller/ → BilleteController.java
│ │ │ ├── dto/ → BilleteDto.java
│ │ │ ├── model/ → Billete.java
│ │ │ ├── repository/ → BilleteRepository.java
│ │ │ └── service/ → BilleteService.java, BilleteServiceImpl.java
│ │ │
│ │ ├── cliente/
│ │ │ ├── controller/ → ClienteController.java
│ │ │ ├── dto/ → ClienteDto.java, HistorialClienteDto.java
│ │ │ ├── model/ → Cliente.java
│ │ │ ├── repository/ → ClienteRepository.java
│ │ │ └── service/ → ClienteService.java, ClienteServiceImpl.java
│ │ │
│ │ ├── sorteo/
│ │ │ ├── controller/ → SorteoController.java
│ │ │ ├── dto/ → SorteoDto.java
│ │ │ ├── model/ → Sorteo.java
│ │ │ ├── repository/ → SorteoRepository.java
│ │ │ └── service/ → SorteoService.java, SorteoServiceImpl.java
│ │ │
│ │ ├── venta/
│ │ │ ├── controller/ → VentaController.java
│ │ │ ├── dto/ → VentaRequestDto.java, VentaResponseDto.java
│ │ │ ├── model/ → Venta.java
│ │ │ ├── repository/ → VentaRepository.java
│ │ │ └── service/ → VentaService.java, VentaServiceImpl.java
│ │ │
│ │ ├── excepciones/ → GlobalExceptionHandler.java
│ │ ├── compartido/ → EstadoBillete.java
│ │ ├── configuracion/ → SecurityConfig.java, MapperConfig.java, StatusController.java
│ │ └── SistemaLoteriaApplication.java
│ │
│ └── resources/
│ ├── application.properties
│ ├── data.sql
│ ├── static/
│ └── templates/
│
└── test/java/org/konex/sistemaloteria/
├── billete/
│ ├── controller/ → BilleteControllerTest.java
│ └── service/ → BilleteServiceImplTest.java
│
├── cliente/
│ ├── controller/ → ClienteControllerTest.java
│ └── service/ → ClienteServiceTest.java
│
├── sorteo/
│ ├── controller/ → SorteoControllerTest.java
│ └── service/ → SorteoServiceImplTest.java
│
└── venta/
├── controller/ → VentaControllerTest.java
└── service/ → VentaServiceImplTest.java


⚙️ Tecnologías utilizadas
| Categoría           | Tecnología / Framework                          |
| ------------------- | ----------------------------------------------- |
| Lenguaje            | Java 17                                         |
| Framework principal | Spring Boot 3.4                                 |
| Dependencias        | Spring Web, Spring Data JPA, Validation, Lombok |
| Base de datos       | H2 (en memoria)                                 |
| Testing             | JUnit 5, Mockito, Spring Boot Test              |
| Build Tool          | Gradle                                          |
| IDE recomendado     | IntelliJ IDEA / VS Code / Eclipse               |


🚀 Ejecución del proyecto
🔧 Requisitos previos

Java 17 o superior

Gradle 8+

Puerto 8080 disponible

▶️ Ejecutar desde consola
./gradlew clean bootRun

🌐 Acceso a la API

URL base: http://localhost:8080/api

Consola H2: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Usuario: sa

Contraseña: (vacía)

📡 Endpoints principales
| Recurso      | Método | Ruta                                      | Descripción                                |
| ------------ | ------ | ----------------------------------------- | ------------------------------------------ |
| **Sorteos**  | `POST` | `/api/sorteos`                            | Crear un nuevo sorteo                      |
|              | `GET`  | `/api/sorteos`                            | Listar todos los sorteos                   |
| **Billetes** | `POST` | `/api/billetes`                           | Crear billetes asociados a un sorteo       |
|              | `GET`  | `/api/billetes/sorteo/{id}`               | Listar billetes de un sorteo               |
| **Clientes** | `POST` | `/api/clientes`                           | Registrar nuevo cliente                    |
|              | `GET`  | `/api/clientes/historial?correo={correo}` | Consultar billetes vendidos por cliente    |
| **Ventas**   | `POST` | `/api/ventas`                             | Registrar venta de un billete a un cliente |


🧪 Pruebas unitarias
🔹 Ejecutar pruebas
./gradlew clean test
🔹 Reporte HTML
start .\build\reports\tests\test\index.html
🔹 Casos cubiertos

VentaServiceImplTest → Verifica estados, validaciones y excepciones (billete no encontrado, ya vendido, error de guardado).

VentaControllerTest → Asegura respuestas correctas (201, 400, 404, 409) y formato JSON coherente.

GlobalExceptionHandlerTest (implícito) → Gestiona todos los errores estándar de Spring.


⚠️ Formato de errores

Las respuestas de error siguen una estructura JSON consistente:
{
  "timestamp": "2025-11-09T10:00:00.123",
  "status": 400,
  "error": "Error de validación",
  "message": "Campos inválidos",
  "path": "/api/ventas",
  "method": "POST",
  "errors": {
    "billeteId": "El ID del billete es obligatorio"
  }
}

## 🧠 Buenas prácticas aplicadas

- Arquitectura por capas: **controller**, **service**, **repository**, **dto**, **model**  
- Validaciones con **@NotNull**, **@Positive**, **@Valid**  
- Manejo centralizado de excepciones (**@RestControllerAdvice**)  
- Lógica de negocio con transacciones seguras (**@Transactional**)  
- Pruebas unitarias (servicio y controlador) automatizadas  
- Uso de **DTOs** para desacoplar la API del modelo de datos  
- Alta cobertura de pruebas **(>95%)**

---

## 🏁 Estado del proyecto

✅ **Completado y funcional**

Incluye:
- Lógica de negocio completa  
- Validaciones robustas  
- Pruebas unitarias exitosas  
- Documentación técnica y de endpoints

- 👨‍💻 Autor

Johan Sebastian Grisales Montoya
Desarrollador

📅 Noviembre 2025
