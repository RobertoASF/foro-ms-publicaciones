package cl.duoc.foro.publicaciones.service;

import cl.duoc.foro.publicaciones.dto.ComentarioResponse;
import cl.duoc.foro.publicaciones.dto.PaginaResponse;
import cl.duoc.foro.publicaciones.dto.TemaDetalleResponse;
import cl.duoc.foro.publicaciones.dto.TemaRequest;
import cl.duoc.foro.publicaciones.dto.TemaResponse;
import cl.duoc.foro.publicaciones.exception.AccionNoPermitidaException;
import cl.duoc.foro.publicaciones.exception.RecursoNoEncontradoException;
import cl.duoc.foro.publicaciones.model.Categoria;
import cl.duoc.foro.publicaciones.model.Tema;
import cl.duoc.foro.publicaciones.repository.CategoriaRepository;
import cl.duoc.foro.publicaciones.repository.ComentarioRepository;
import cl.duoc.foro.publicaciones.repository.ComentarioRepository.TotalComentarios;
import cl.duoc.foro.publicaciones.repository.TemaRepository;
import cl.duoc.foro.publicaciones.security.UsuarioAutenticado;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemaService {

    private final TemaRepository temaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ComentarioRepository comentarioRepository;

    @Transactional(readOnly = true)
    public PaginaResponse<TemaResponse> buscar(String texto, Long categoriaId, Long autorId, int pagina, int tamano) {
        PageRequest orden = PageRequest.of(pagina, tamano, Sort.by(Sort.Direction.DESC, "fechaCreacion", "id"));
        Page<Tema> resultado = temaRepository.findAll(filtro(texto, categoriaId, autorId), orden);
        Map<Long, Long> totales = contarComentarios(resultado.getContent());

        List<TemaResponse> temas = resultado.getContent().stream()
                .map(tema -> TemaResponse.desde(tema, totales.getOrDefault(tema.getId(), 0L)))
                .toList();
        return PaginaResponse.de(resultado, temas);
    }

    @Transactional(readOnly = true)
    public TemaDetalleResponse obtener(Long id) {
        Tema tema = buscarTema(id);
        List<ComentarioResponse> comentarios = comentarioRepository.findByTemaIdOrderByFechaCreacionAsc(id).stream()
                .map(ComentarioResponse::desde)
                .toList();
        return new TemaDetalleResponse(TemaResponse.desde(tema, comentarios.size()), comentarios);
    }

    @Transactional
    public TemaResponse crear(TemaRequest datos, UsuarioAutenticado usuario) {
        Tema tema = new Tema();
        tema.setTitulo(datos.titulo());
        tema.setContenido(datos.contenido());
        tema.setCategoria(buscarCategoria(datos.categoriaId()));
        tema.setAutorId(usuario.id());
        tema.setAutorNombre(usuario.nombreUsuario());

        Tema guardado = temaRepository.save(tema);
        log.info("Tema {} creado por el usuario {}", guardado.getId(), usuario.id());
        return TemaResponse.desde(guardado, 0);
    }

    @Transactional
    public TemaResponse actualizar(Long id, TemaRequest datos, UsuarioAutenticado usuario) {
        Tema tema = buscarTema(id);
        if (!usuario.esAutor(tema.getAutorId())) {
            throw new AccionNoPermitidaException("Solo el autor puede editar este tema");
        }

        tema.setTitulo(datos.titulo());
        tema.setContenido(datos.contenido());
        tema.setCategoria(buscarCategoria(datos.categoriaId()));
        temaRepository.flush();
        log.info("Tema {} actualizado", id);
        return TemaResponse.desde(tema, comentarioRepository.countByTemaId(id));
    }

    @Transactional
    public void eliminar(Long id, UsuarioAutenticado usuario) {
        Tema tema = buscarTema(id);
        if (!usuario.puedeBorrarDe(tema.getAutorId())) {
            throw new AccionNoPermitidaException("No puedes eliminar temas de otros usuarios");
        }
        temaRepository.delete(tema);
        log.info("Tema {} eliminado por el usuario {}", id, usuario.id());
    }

    // Los filtros son opcionales, asi que solo se agregan a la consulta los que vienen
    private Specification<Tema> filtro(String texto, Long categoriaId, Long autorId) {
        return (raiz, consulta, criterio) -> {
            List<Predicate> condiciones = new ArrayList<>();
            if (texto != null && !texto.isBlank()) {
                String patron = "%" + texto.trim().toLowerCase(Locale.ROOT) + "%";
                condiciones.add(criterio.or(
                        criterio.like(criterio.lower(raiz.get("titulo")), patron),
                        criterio.like(criterio.lower(raiz.get("contenido")), patron)));
            }
            if (categoriaId != null) {
                condiciones.add(criterio.equal(raiz.get("categoria").get("id"), categoriaId));
            }
            if (autorId != null) {
                condiciones.add(criterio.equal(raiz.get("autorId"), autorId));
            }
            return criterio.and(condiciones.toArray(new Predicate[0]));
        };
    }

    private Map<Long, Long> contarComentarios(List<Tema> temas) {
        if (temas.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = temas.stream().map(Tema::getId).toList();
        return comentarioRepository.contarPorTemas(ids).stream()
                .collect(Collectors.toMap(TotalComentarios::getTemaId, TotalComentarios::getTotal));
    }

    private Tema buscarTema(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el tema con id " + id));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la categoría con id " + id));
    }
}
