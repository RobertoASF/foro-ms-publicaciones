package cl.duoc.foro.publicaciones.dto;

import java.util.List;

public record TemaDetalleResponse(TemaResponse tema, List<ComentarioResponse> comentarios) {
}
