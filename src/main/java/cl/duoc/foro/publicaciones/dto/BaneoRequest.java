package cl.duoc.foro.publicaciones.dto;

import jakarta.validation.constraints.NotNull;

public record BaneoRequest(
        @NotNull(message = "Indica si el contenido queda baneado: true o false")
        Boolean baneado) {
}
