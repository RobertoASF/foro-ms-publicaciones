package cl.duoc.foro.publicaciones.controller;

import cl.duoc.foro.publicaciones.dto.CategoriaRequest;
import cl.duoc.foro.publicaciones.dto.CategoriaResponse;
import cl.duoc.foro.publicaciones.dto.CategoriaResumenResponse;
import cl.duoc.foro.publicaciones.dto.MensajeResponse;
import cl.duoc.foro.publicaciones.service.CategoriaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "${cors.origen}")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/resumen")
    public List<CategoriaResumenResponse> resumir() {
        return categoriaService.resumir();
    }

    @GetMapping("/{id}")
    public CategoriaResponse obtener(@PathVariable Long id) {
        return categoriaService.obtener(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest datos) {
        CategoriaResponse creada = categoriaService.crear(datos);
        return ResponseEntity.created(URI.create("/api/categorias/" + creada.id())).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoriaResponse actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest datos) {
        return categoriaService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MensajeResponse eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return new MensajeResponse("Categoría eliminada");
    }
}
