-- Datos iniciales del foro.
-- Los autores coinciden con los usuarios cargados en ms-usuarios:
-- 1 admin, 2 moderador, 3 camila, 4 diego.

SET DEFINE OFF

INSERT INTO categorias (nombre, descripcion) VALUES ('Tecnología', 'Programación, hardware y novedades del mundo digital');
INSERT INTO categorias (nombre, descripcion) VALUES ('Videojuegos', 'Lanzamientos, recomendaciones y partidas');
INSERT INTO categorias (nombre, descripcion) VALUES ('Deportes', 'Resultados, equipos y actividad física');
INSERT INTO categorias (nombre, descripcion) VALUES ('Música', 'Artistas, discos y conciertos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Cine y series', 'Estrenos, críticas y recomendaciones');
INSERT INTO categorias (nombre, descripcion) VALUES ('Cocina', 'Recetas, técnicas y datos para cocinar en casa');

INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('¿Qué lenguaje recomiendan para partir?', 'Quiero aprender a programar este año y no sé si partir con Java o con Python.',
        (SELECT id FROM categorias WHERE nombre = 'Tecnología'), 3, 'camila', TIMESTAMP '2026-10-20 10:15:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Mejores juegos cooperativos', 'Busco juegos para jugar con amigos los fines de semana, ojalá en PC.',
        (SELECT id FROM categorias WHERE nombre = 'Videojuegos'), 4, 'diego', TIMESTAMP '2026-10-21 18:40:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Rutas para trotar en Santiago', 'Compartan sus rutas favoritas para trotar temprano y que tengan sombra.',
        (SELECT id FROM categorias WHERE nombre = 'Deportes'), 3, 'camila', TIMESTAMP '2026-10-22 07:30:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Discos para estudiar', 'Necesito música tranquila para estudiar sin distraerme. ¿Qué están escuchando?',
        (SELECT id FROM categorias WHERE nombre = 'Música'), 4, 'diego', TIMESTAMP '2026-10-23 21:05:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Series cortas para ver en una semana', 'Recomienden series de una temporada que valgan la pena.',
        (SELECT id FROM categorias WHERE nombre = 'Cine y series'), 2, 'moderador', TIMESTAMP '2026-10-24 22:10:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Pan amasado en casa', 'Mi masa queda muy dura. ¿Cuánto rato la dejan reposar?',
        (SELECT id FROM categorias WHERE nombre = 'Cocina'), 3, 'camila', TIMESTAMP '2026-10-25 12:20:00');

INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = '¿Qué lenguaje recomiendan para partir?'), 'Python es más simple para empezar, y después puedes pasar a Java.', 4, 'diego', TIMESTAMP '2026-10-20 11:00:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = '¿Qué lenguaje recomiendan para partir?'), 'Java te obliga a ordenar mejor el código desde el principio.', 2, 'moderador', TIMESTAMP '2026-10-20 12:30:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Mejores juegos cooperativos'), 'Prueba con alguno de supervivencia, se hacen muy entretenidos en grupo.', 3, 'camila', TIMESTAMP '2026-10-21 19:15:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Rutas para trotar en Santiago'), 'El parque Bicentenario es buena opción en la mañana.', 4, 'diego', TIMESTAMP '2026-10-22 08:45:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Discos para estudiar'), 'A mí me funciona la música instrumental, sin letra.', 3, 'camila', TIMESTAMP '2026-10-23 21:40:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Pan amasado en casa'), 'Déjala reposar unos 30 minutos tapada con un paño.', 2, 'moderador', TIMESTAMP '2026-10-25 13:00:00');

COMMIT;
