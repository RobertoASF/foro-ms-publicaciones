package cl.duoc.foro.publicaciones.controller;

import cl.duoc.foro.publicaciones.dto.ComentarioRequest;
import cl.duoc.foro.publicaciones.dto.ComentarioResponse;
import cl.duoc.foro.publicaciones.dto.MensajeResponse;
import cl.duoc.foro.publicaciones.security.UsuarioAutenticado;
import cl.duoc.foro.publicaciones.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "${cors.origen}")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PutMapping("/{id}")
    public ComentarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest datos,
            @AuthenticationPrincipal Jwt jwt) {
        return comentarioService.actualizar(id, datos, UsuarioAutenticado.desde(jwt));
    }

    @DeleteMapping("/{id}")
    public MensajeResponse eliminar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        comentarioService.eliminar(id, UsuarioAutenticado.desde(jwt));
        return new MensajeResponse("Comentario eliminado");
    }
}
