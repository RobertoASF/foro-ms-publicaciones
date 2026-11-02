-- Tablas del microservicio de publicaciones.
-- Se ejecuta conectado como FORO_PUBLICACIONES.
-- autor_id apunta a un usuario de ms-usuarios. No lleva clave foranea
-- porque cada microservicio es dueño de sus propios datos.

CREATE TABLE categorias (
    id          NUMBER(19)         GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR2(60 CHAR)  NOT NULL UNIQUE,
    descripcion VARCHAR2(255 CHAR) NOT NULL
);

CREATE TABLE temas (
    id                  NUMBER(19)          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo              VARCHAR2(150 CHAR)  NOT NULL,
    contenido           VARCHAR2(2000 CHAR) NOT NULL,
    categoria_id        NUMBER(19)          NOT NULL REFERENCES categorias (id),
    autor_id            NUMBER(19)          NOT NULL,
    autor_nombre        VARCHAR2(30 CHAR)   NOT NULL,
    fecha_creacion      TIMESTAMP           DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP
);

CREATE TABLE comentarios (
    id                  NUMBER(19)          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tema_id             NUMBER(19)          NOT NULL REFERENCES temas (id) ON DELETE CASCADE,
    contenido           VARCHAR2(1000 CHAR) NOT NULL,
    autor_id            NUMBER(19)          NOT NULL,
    autor_nombre        VARCHAR2(30 CHAR)   NOT NULL,
    fecha_creacion      TIMESTAMP           DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP
);

CREATE INDEX idx_temas_categoria ON temas (categoria_id);
CREATE INDEX idx_temas_autor ON temas (autor_id);
CREATE INDEX idx_comentarios_tema ON comentarios (tema_id);
