-- Actualiza una base creada con la versión 1 (semana 2).
-- Agrega la columna que usa ms-moderacion para ocultar contenido.
-- Se ejecuta conectado como FORO_PUBLICACIONES y antes de 03_contenido_baneado.sql.

ALTER TABLE temas ADD (baneado NUMBER(1) DEFAULT 0 NOT NULL CHECK (baneado IN (0, 1)));
ALTER TABLE comentarios ADD (baneado NUMBER(1) DEFAULT 0 NOT NULL CHECK (baneado IN (0, 1)));
