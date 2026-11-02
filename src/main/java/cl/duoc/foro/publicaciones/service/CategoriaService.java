package cl.duoc.foro.publicaciones.service;

import cl.duoc.foro.publicaciones.dto.CategoriaRequest;
import cl.duoc.foro.publicaciones.dto.CategoriaResponse;
import cl.duoc.foro.publicaciones.dto.CategoriaResumenResponse;
import cl.duoc.foro.publicaciones.exception.ConflictoException;
import cl.duoc.foro.publicaciones.exception.RecursoNoEncontradoException;
import cl.duoc.foro.publicaciones.model.Categoria;
import cl.duoc.foro.publicaciones.repository.CategoriaRepository;
import cl.duoc.foro.publicaciones.repository.TemaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TemaRepository temaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAllByOrderByNombreAsc().stream().map(CategoriaResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResumenResponse> resumir() {
        return categoriaRepository.resumir();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtener(Long id) {
        return CategoriaResponse.desde(buscar(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest datos) {
        if (categoriaRepository.existsByNombreIgnoreCase(datos.nombre())) {
            throw new ConflictoException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(datos.nombre());
        categoria.setDescripcion(datos.descripcion());

        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoria creada con id {}", guardada.getId());
        return CategoriaResponse.desde(guardada);
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest datos) {
        Categoria categoria = buscar(id);
        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(datos.nombre(), id)) {
            throw new ConflictoException("Ya existe una categoría con ese nombre");
        }

        categoria.setNombre(datos.nombre());
        categoria.setDescripcion(datos.descripcion());
        log.info("Categoria {} actualizada", id);
        return CategoriaResponse.desde(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscar(id);
        if (temaRepository.existsByCategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar una categoría que tiene temas");
        }
        categoriaRepository.delete(categoria);
        log.info("Categoria {} eliminada", id);
    }

    private Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la categoría con id " + id));
    }
}
