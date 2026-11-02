package cl.duoc.foro.publicaciones.dto;

import cl.duoc.foro.publicaciones.model.Comentario;
import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        Long temaId,
        String contenido,
        Long autorId,
        String autorNombre,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {

    public static ComentarioResponse desde(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getTema().getId(),
                comentario.getContenido(),
                comentario.getAutorId(),
                comentario.getAutorNombre(),
                comentario.getFechaCreacion(),
                comentario.getFechaActualizacion());
    }
}
