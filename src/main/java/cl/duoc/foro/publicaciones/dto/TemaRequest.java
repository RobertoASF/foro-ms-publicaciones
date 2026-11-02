package cl.duoc.foro.publicaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TemaRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String titulo,

        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 2000, message = "El contenido no puede superar los 2000 caracteres")
        String contenido,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId) {
}
