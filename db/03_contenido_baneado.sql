-- Contenido que incumple las normas y quedó baneado.
-- Sus baneos y reportes están en ms-moderacion. No aparece en listados, búsquedas ni detalle.

SET DEFINE OFF

INSERT INTO temas (titulo, contenido, categoria_id, autor_id, autor_nombre, baneado, fecha_creacion)
VALUES ('Compra seguidores para tu canal de cartas', 'Vendo seguidores y visitas para tu canal. Escríbeme por interno y te hago precio.',
        (SELECT id FROM categorias WHERE nombre = 'Pokemon TCG'), 4, 'diego', 1, TIMESTAMP '2026-10-26 08:50:00');

INSERT INTO comentarios (tema_id, contenido, autor_id, autor_nombre, baneado, fecha_creacion)
VALUES ((SELECT id FROM temas WHERE titulo = 'Mazos económicos para aprender a jugar'), 'Si no tienes plata no juegues, deja de llorar en el foro.', 4, 'diego', 1, TIMESTAMP '2026-10-27 18:20:00');

COMMIT;
