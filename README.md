# Ruta Segura - Backend API

API REST construida con **Spring Boot 3.4.3**, **Java 21**, **Spring Security 6**, **JJWT** y persistencia en **PostgreSQL (Supabase)** para la aplicación colaborativa orientada a la seguridad ciudadana y reporte de riesgos nocturnos para mujeres en el espacio público.

---

## 🏛️ Arquitectura del Sistema

El proyecto sigue una arquitectura limpia en capas:

```
src/main/java/com/rutasegura/
├── controller/     # Controladores REST (AuthController, ReporteController)
├── dto/            # Objetos de Transferencia de Datos y validaciones con Jakarta
├── exception/      # Manejo global y uniforme de excepciones (GlobalExceptionHandler)
├── model/          # Entidades JPA (Usuario, Reporte, CategoriaRiesgo, Rol)
├── repository/     # Repositorios Spring Data JPA (UsuarioRepository, ReporteRepository)
├── security/       # Spring Security, filtros JWT y proveedores de tokens
└── service/        # Lógica de negocio y cálculo de cercanía geográfica (Haversine)
```

---

## ⚙️ Configuración de Entorno (Supabase / PostgreSQL)

El proyecto puede configurarse mediante variables de entorno o modificando [`application.properties`](file:///c:/Users/jtjef/OneDrive/Escritorio/maraton/src/main/resources/application.properties):

| Variable | Descripción | Valor por Defecto |
| :--- | :--- | :--- |
| `DB_URL` | URL de conexión JDBC a Supabase | `jdbc:postgresql://localhost:5432/rutasegura` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de la base de datos | `postgres` |
| `JWT_SECRET` | Clave secreta para firma HMAC-SHA256 (256 bits) | Clave base segura |
| `JWT_EXPIRATION_MS`| Vigencia del token en milisegundos | `86400000` (24 horas) |

---

## 📡 Endpoints de la API REST

### 1. Autenticación (`/api/auth`)

#### Registro de Usuaria
- **Método:** `POST`
- **Ruta:** `/api/auth/registro`
- **Acceso:** Público
- **Cuerpo de Solicitud:**
```json
{
  "nombre": "Ana Pérez",
  "email": "ana.perez@ejemplo.com",
  "password": "miClaveSegura123"
}
```
- **Respuesta Exitosa (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "email": "ana.perez@ejemplo.com",
  "nombre": "Ana Pérez",
  "rol": "ROLE_USER"
}
```

#### Inicio de Sesión
- **Método:** `POST`
- **Ruta:** `/api/auth/login`
- **Acceso:** Público
- **Cuerpo de Solicitud:**
```json
{
  "email": "ana.perez@ejemplo.com",
  "password": "miClaveSegura123"
}
```
- **Respuesta Exitosa (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "email": "ana.perez@ejemplo.com",
  "nombre": "Ana Pérez",
  "rol": "ROLE_USER"
}
```

#### Consulta de Perfil de Usuaria
- **Método:** `GET`
- **Ruta:** `/api/auth/me`
- **Acceso:** Protegido (Requiere encabezado `Authorization: Bearer <token>`)
- **Respuesta Exitosa (200 OK):**
```json
{
  "id": 1,
  "nombre": "Ana Pérez",
  "email": "ana.perez@ejemplo.com",
  "rol": "ROLE_USER",
  "fechaCreacion": "2026-11-04T10:15:00"
}
```

---

### 2. Reportes Rápidos de Riesgo (`/api/reportes`)

#### Registro Rápido (One-Tap)
- **Método:** `POST`
- **Ruta:** `/api/reportes`
- **Acceso:** Protegido (Requiere encabezado `Authorization: Bearer <token>`)
- **Cuerpo de Solicitud:**
```json
{
  "categoria": "ILUMINACION_DEFICIENTE",
  "municipio": "Soacha",
  "referencia": "Esquina transversal 15 con calle 8, junto a estación",
  "latitud": 4.5821,
  "longitud": -74.2185
}
```
- **Categorías admitidas:** `ACOSO`, `ILUMINACION_DEFICIENTE`, `ZONA_DESOLADA`, `SOSPECHOSO`, `OTRO`.
- **Respuesta Exitosa (201 Created):**
```json
{
  "id": 1,
  "categoria": "ILUMINACION_DEFICIENTE",
  "municipio": "Soacha",
  "referencia": "Esquina transversal 15 con calle 8, junto a estación",
  "latitud": 4.5821,
  "longitud": -74.2185,
  "fechaReporte": "2026-11-04T10:15:00",
  "nombreUsuaria": "Ana Pérez",
  "distanciaKm": null
}
```

#### Consulta y Filtro de Reportes (Lista y Mapa)
- **Método:** `GET`
- **Ruta:** `/api/reportes`
- **Acceso:** Público
- **Parámetros opcionales de consulta (*Query Params*):**
  - `categoria`: Filtrar por categoría (`ACOSO`, `ILUMINACION_DEFICIENTE`, etc.).
  - `municipio`: Filtrar por municipio.
  - `latitud` y `longitud`: Coordenadas de la usuaria para ordenar automáticamente la lista de menor a mayor distancia geográfica (fórmula de Haversine).
- **Ejemplo de consulta:** `/api/reportes?categoria=ACOSO&latitud=4.6582&longitud=-74.0573`
- **Respuesta Exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "categoria": "ACOSO",
    "municipio": "Bogotá",
    "referencia": "Calle 72 con Carrera 11",
    "latitud": 4.6582,
    "longitud": -74.0573,
    "fechaReporte": "2026-11-04T09:30:00",
    "nombreUsuaria": "Usuaria Verificada",
    "distanciaKm": 0.0
  }
]
```

#### Resumen Estadístico de Reportes
- **Método:** `GET`
- **Ruta:** `/api/reportes/estadisticas`
- **Acceso:** Público
- **Respuesta Exitosa (200 OK):**
```json
{
  "TOTAL_REPORTES": 4,
  "ACOSO": 1,
  "ILUMINACION_DEFICIENTE": 1,
  "ZONA_DESOLADA": 1,
  "SOSPECHOSO": 1,
  "OTRO": 0
}
```

---

## 🧪 Pruebas Unitarias y de Integración

Para ejecutar la suite completa de pruebas:
```bash
mvn test
```
Las pruebas se ejecutan de manera aislada utilizando una base de datos en memoria **H2** en modo de compatibilidad PostgreSQL.
