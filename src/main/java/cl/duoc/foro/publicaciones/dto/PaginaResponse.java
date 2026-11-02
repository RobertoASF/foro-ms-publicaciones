package cl.duoc.foro.publicaciones.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginaResponse<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas) {

    public static <T> PaginaResponse<T> de(Page<?> pagina, List<T> contenido) {
        return new PaginaResponse<>(contenido, pagina.getNumber(), pagina.getSize(),
                pagina.getTotalElements(), pagina.getTotalPages());
    }
}
