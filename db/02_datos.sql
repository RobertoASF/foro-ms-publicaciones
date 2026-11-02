-- Datos iniciales del foro.
-- Los autores coinciden con los usuarios cargados en ms-usuarios:
-- 1 admin, 2 moderador, 3 camila, 4 diego.

SET DEFINE OFF

INSERT INTO categorias (nombre, descripcion) VALUES ('Magic The Gathering', 'Mazos, formatos, reglas y novedades de Magic');
INSERT INTO categorias (nombre, descripcion) VALUES ('Mitos y Leyendas', 'El TCG chileno: ediciones, mazos y torneos');
INSERT INTO categorias (nombre, descripcion) VALUES ('Pokemon TCG', 'Expansiones, mazos y torneos del juego de cartas');
INSERT INTO categorias (nombre, descripcion) VALUES ('One Piece TCG', 'Líderes, mazos y expansiones');
INSERT INTO categorias (nombre, descripcion) VALUES ('Yu-Gi-Oh!', 'Arquetipos, listas de cartas prohibidas y duelos');

INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('¿Commander o Modern para partir?', 'Quiero entrar a Magic este año y no sé si armar un mazo de Commander para jugar con amigos o partir con uno de Modern.',
        (SELECT id FROM categorias WHERE nombre = 'Magic The Gathering'), 3, 'camila', TIMESTAMP '2026-10-20 10:15:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Torneos de Mitos y Leyendas en Santiago', 'Busco tiendas que organicen torneos los fines de semana. ¿Dónde están jugando?',
        (SELECT id FROM categorias WHERE nombre = 'Mitos y Leyendas'), 4, 'diego', TIMESTAMP '2026-10-21 18:40:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Mazos económicos para aprender a jugar', 'Quiero partir sin gastar tanto. ¿Qué mazo recomiendan para aprender las reglas?',
        (SELECT id FROM categorias WHERE nombre = 'Pokemon TCG'), 3, 'camila', TIMESTAMP '2026-10-22 07:30:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('¿Qué líder recomiendan para mi primer mazo?', 'Estoy armando mi primer mazo y no sé qué líder elegir. ¿Cuál es más fácil de jugar?',
        (SELECT id FROM categorias WHERE nombre = 'One Piece TCG'), 4, 'diego', TIMESTAMP '2026-10-23 21:05:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Dónde revisar la lista de cartas prohibidas', 'Cada actualización cambia harto el formato. ¿Dónde revisan la lista vigente?',
        (SELECT id FROM categorias WHERE nombre = 'Yu-Gi-Oh!'), 2, 'moderador', TIMESTAMP '2026-10-24 22:10:00');
INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, fecha_creacion)
VALUES ('Cómo guardar las cartas más caras', 'Se me están doblando algunas cartas. ¿Usan doble funda o carpetas?',
        (SELECT id FROM categorias WHERE nombre = 'Magic The Gathering'), 3, 'camila', TIMESTAMP '2026-10-25 12:20:00');

INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = '¿Commander o Modern para partir?'), 'Commander es más entretenido en grupo, y puedes partir con un mazo preconstruido.', 4, 'diego', TIMESTAMP '2026-10-20 11:00:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = '¿Commander o Modern para partir?'), 'Modern pide más inversión, así que yo partiría por el otro formato.', 2, 'moderador', TIMESTAMP '2026-10-20 12:30:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Torneos de Mitos y Leyendas en Santiago'), 'Revisa las tiendas de cartas del centro, varias publican sus torneos en redes sociales.', 3, 'camila', TIMESTAMP '2026-10-21 19:15:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Mazos económicos para aprender a jugar'), 'Los mazos preconstruidos sirven harto para aprender a jugar.', 4, 'diego', TIMESTAMP '2026-10-22 08:45:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = '¿Qué líder recomiendan para mi primer mazo?'), 'Los mazos de inicio traen un líder listo para jugar, es buena forma de partir.', 3, 'camila', TIMESTAMP '2026-10-23 21:40:00');
INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Cómo guardar las cartas más caras'), 'Doble funda para las más caras y carpeta de nueve bolsillos para el resto.', 2, 'moderador', TIMESTAMP '2026-10-25 13:00:00');

COMMIT;
