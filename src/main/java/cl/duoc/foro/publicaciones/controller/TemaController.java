package cl.duoc.foro.publicaciones.controller;

import cl.duoc.foro.publicaciones.dto.ComentarioRequest;
import cl.duoc.foro.publicaciones.dto.ComentarioResponse;
import cl.duoc.foro.publicaciones.dto.MensajeResponse;
import cl.duoc.foro.publicaciones.dto.PaginaResponse;
import cl.duoc.foro.publicaciones.dto.TemaDetalleResponse;
import cl.duoc.foro.publicaciones.dto.TemaRequest;
import cl.duoc.foro.publicaciones.dto.TemaResponse;
import cl.duoc.foro.publicaciones.security.UsuarioAutenticado;
import cl.duoc.foro.publicaciones.service.ComentarioService;
import cl.duoc.foro.publicaciones.service.TemaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/temas")
@CrossOrigin(origins = "${cors.origen}")
@RequiredArgsConstructor
public class TemaController {

    private final TemaService temaService;
    private final ComentarioService comentarioService;

    @GetMapping
    public PaginaResponse<TemaResponse> listar(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int pagina,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño mínimo es 1")
            @Max(value = 50, message = "El tamaño máximo es 50") int tamano) {
        return temaService.buscar(null, null, null, pagina, tamano);
    }

    @GetMapping("/buscar")
    public PaginaResponse<TemaResponse> buscar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long autorId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int pagina,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño mínimo es 1")
            @Max(value = 50, message = "El tamaño máximo es 50") int tamano) {
        return temaService.buscar(texto, categoriaId, autorId, pagina, tamano);
    }

    @GetMapping("/{id}")
    public TemaDetalleResponse obtener(@PathVariable Long id) {
        return temaService.obtener(id);
    }

    @PostMapping
    public ResponseEntity<TemaResponse> crear(@Valid @RequestBody TemaRequest datos,
            @AuthenticationPrincipal Jwt jwt) {
        TemaResponse creado = temaService.crear(datos, UsuarioAutenticado.desde(jwt));
        return ResponseEntity.created(URI.create("/api/temas/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public TemaResponse actualizar(@PathVariable Long id, @Valid @RequestBody TemaRequest datos,
            @AuthenticationPrincipal Jwt jwt) {
        return temaService.actualizar(id, datos, UsuarioAutenticado.desde(jwt));
    }

    @DeleteMapping("/{id}")
    public MensajeResponse eliminar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        temaService.eliminar(id, UsuarioAutenticado.desde(jwt));
        return new MensajeResponse("Tema eliminado");
    }

    @GetMapping("/{id}/comentarios")
    public List<ComentarioResponse> listarComentarios(@PathVariable Long id) {
        return comentarioService.listarPorTema(id);
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioResponse> comentar(@PathVariable Long id,
            @Valid @RequestBody ComentarioRequest datos, @AuthenticationPrincipal Jwt jwt) {
        ComentarioResponse creado = comentarioService.crear(id, datos, UsuarioAutenticado.desde(jwt));
        return ResponseEntity.created(URI.create("/api/comentarios/" + creado.id())).body(creado);
    }
}
