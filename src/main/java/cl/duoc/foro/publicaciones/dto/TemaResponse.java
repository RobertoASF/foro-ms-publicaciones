package cl.duoc.foro.publicaciones.dto;

import cl.duoc.foro.publicaciones.model.Tema;
import java.time.LocalDateTime;

public record TemaResponse(
        Long id,
        String titulo,
        String contenido,
        CategoriaResponse categoria,
        Long autorId,
        String autorNombre,
        long totalComentarios,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {

    public static TemaResponse desde(Tema tema, long totalComentarios) {
        return new TemaResponse(
                tema.getId(),
                tema.getTitulo(),
                tema.getContenido(),
                CategoriaResponse.desde(tema.getCategoria()),
                tema.getAutorId(),
                tema.getAutorNombre(),
                totalComentarios,
                tema.getFechaCreacion(),
                tema.getFechaActualizacion());
    }
}
