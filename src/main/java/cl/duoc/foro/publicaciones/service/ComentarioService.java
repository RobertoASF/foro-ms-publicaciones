package cl.duoc.foro.publicaciones.service;

import cl.duoc.foro.publicaciones.dto.ComentarioRequest;
import cl.duoc.foro.publicaciones.dto.ComentarioResponse;
import cl.duoc.foro.publicaciones.exception.AccionNoPermitidaException;
import cl.duoc.foro.publicaciones.exception.RecursoNoEncontradoException;
import cl.duoc.foro.publicaciones.model.Comentario;
import cl.duoc.foro.publicaciones.model.Tema;
import cl.duoc.foro.publicaciones.repository.ComentarioRepository;
import cl.duoc.foro.publicaciones.repository.TemaRepository;
import cl.duoc.foro.publicaciones.security.UsuarioAutenticado;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TemaRepository temaRepository;

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarPorTema(Long temaId) {
        buscarTema(temaId);
        return comentarioRepository.findByTemaIdOrderByFechaCreacionAsc(temaId).stream()
                .map(ComentarioResponse::desde)
                .toList();
    }

    @Transactional
    public ComentarioResponse crear(Long temaId, ComentarioRequest datos, UsuarioAutenticado usuario) {
        Comentario comentario = new Comentario();
        comentario.setTema(buscarTema(temaId));
        comentario.setContenido(datos.contenido());
        comentario.setAutorId(usuario.id());
        comentario.setAutorNombre(usuario.nombreUsuario());

        Comentario guardado = comentarioRepository.save(comentario);
        log.info("Comentario {} creado en el tema {}", guardado.getId(), temaId);
        return ComentarioResponse.desde(guardado);
    }

    @Transactional
    public ComentarioResponse actualizar(Long id, ComentarioRequest datos, UsuarioAutenticado usuario) {
        Comentario comentario = buscarComentario(id);
        if (!usuario.esAutor(comentario.getAutorId())) {
            throw new AccionNoPermitidaException("Solo el autor puede editar este comentario");
        }

        comentario.setContenido(datos.contenido());
        comentarioRepository.flush();
        log.info("Comentario {} actualizado", id);
        return ComentarioResponse.desde(comentario);
    }

    @Transactional
    public void eliminar(Long id, UsuarioAutenticado usuario) {
        Comentario comentario = buscarComentario(id);
        if (!usuario.puedeBorrarDe(comentario.getAutorId())) {
            throw new AccionNoPermitidaException("No puedes eliminar comentarios de otros usuarios");
        }
        comentarioRepository.delete(comentario);
        log.info("Comentario {} eliminado por el usuario {}", id, usuario.id());
    }

    private Comentario buscarComentario(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el comentario con id " + id));
    }

    private Tema buscarTema(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el tema con id " + id));
    }
}
