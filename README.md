# ms-publicaciones

Microservicio del foro en línea para buscar, ver, publicar y comentar temas. Valida el token JWT que entrega `ms-usuarios` y usa el rol que viene en él para decidir qué puede hacer cada persona.

Hecho con Java 21, Spring Boot 4.1, Spring Data JPA, Spring Security y Oracle.

## Endpoints

### Categorías

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/categorias` | Público | Lista las categorías |
| GET | `/api/categorias/resumen` | Público | Total de temas, comentarios y último tema por categoría |
| GET | `/api/categorias/{id}` | Público | Muestra una categoría |
| POST | `/api/categorias` | ADMIN | Crea una categoría |
| PUT | `/api/categorias/{id}` | ADMIN | Actualiza una categoría |
| DELETE | `/api/categorias/{id}` | ADMIN | Elimina una categoría sin temas |

### Temas

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/temas?pagina=&tamano=` | Público | Lista los temas más nuevos primero |
| GET | `/api/temas/buscar?texto=&categoriaId=&autorId=&pagina=&tamano=` | Público | Busca con filtros opcionales |
| GET | `/api/temas/{id}` | Público | Muestra un tema con sus comentarios |
| POST | `/api/temas` | Con sesión | Publica un tema |
| PUT | `/api/temas/{id}` | Autor | Edita un tema |
| DELETE | `/api/temas/{id}` | Autor, MODERADOR o ADMIN | Elimina un tema y sus comentarios |

### Comentarios

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/temas/{id}/comentarios` | Público | Lista los comentarios de un tema |
| POST | `/api/temas/{id}/comentarios` | Con sesión | Comenta un tema |
| PUT | `/api/comentarios/{id}` | Autor | Edita un comentario |
| DELETE | `/api/comentarios/{id}` | Autor, MODERADOR o ADMIN | Elimina un comentario |

Las rutas con sesión piden el encabezado `Authorization: Bearer <token>`. El autor nunca se envía en la petición, siempre se toma del token.

Ejemplo para publicar un tema:

```json
{
  "titulo": "Mejores juegos cooperativos",
  "contenido": "Busco juegos para jugar con amigos los fines de semana.",
  "categoriaId": 2
}
```

## Estructura

```
src/main/java/cl/duoc/foro/publicaciones
├── config       Seguridad y validación del token
├── controller   Endpoints REST
├── dto          Datos de entrada y salida, con sus validaciones
├── exception    Errores propios y manejo global
├── model        Entidades JPA
├── repository   Acceso a Oracle
├── security     Datos del usuario autenticado y respuestas 401 y 403
└── service      Reglas de negocio
```

## Base de datos

Los scripts están en la carpeta `db` y se ejecutan conectado como `FORO_PUBLICACIONES`:

1. `01_tablas.sql` crea las tablas.
2. `02_datos.sql` carga 6 categorías, 6 temas y 6 comentarios.

## Variables de entorno

| Variable | Descripción |
|---|---|
| `DB_URL` | Conexión JDBC a Oracle |
| `DB_USUARIO` | Usuario de la base de datos |
| `DB_PASSWORD` | Clave de la base de datos |
| `JWT_SECRETO` | El mismo secreto que usa `ms-usuarios` |
| `CORS_ORIGEN` | Origen web permitido |
| `LOG_NIVEL` | Nivel de log del proyecto, por defecto `INFO` |

## Cómo ejecutarlo

El servicio se levanta junto a Oracle desde el repositorio `foro-infra`.

Las pruebas no necesitan base de datos:

```bash
mvn test
```
