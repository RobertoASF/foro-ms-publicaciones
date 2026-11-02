package cl.duoc.foro.publicaciones.dto;

import java.time.LocalDateTime;

public record CategoriaResumenResponse(
        Long id,
        String nombre,
        long totalTemas,
        long totalComentarios,
        LocalDateTime ultimoTema) {
}
