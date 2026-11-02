package cl.duoc.foro.publicaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.foro.publicaciones.dto.ComentarioRequest;
import cl.duoc.foro.publicaciones.dto.ComentarioResponse;
import cl.duoc.foro.publicaciones.exception.AccionNoPermitidaException;
import cl.duoc.foro.publicaciones.exception.RecursoNoEncontradoException;
import cl.duoc.foro.publicaciones.model.Comentario;
import cl.duoc.foro.publicaciones.model.Tema;
import cl.duoc.foro.publicaciones.repository.ComentarioRepository;
import cl.duoc.foro.publicaciones.repository.TemaRepository;
import cl.duoc.foro.publicaciones.security.UsuarioAutenticado;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    private static final UsuarioAutenticado CAMILA = new UsuarioAutenticado(3L, "camila", "USUARIO");
    private static final UsuarioAutenticado DIEGO = new UsuarioAutenticado(4L, "diego", "USUARIO");

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private TemaRepository temaRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    @Test
    void comentarGuardaElAutorDelToken() {
        when(temaRepository.findById(1L)).thenReturn(Optional.of(new Tema()));
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(llamada -> llamada.getArgument(0));

        ComentarioResponse creado = comentarioService.crear(1L, new ComentarioRequest("Hola"), CAMILA);

        assertThat(creado.autorNombre()).isEqualTo("camila");
    }

    @Test
    void comentarFallaSiElTemaNoExiste() {
        when(temaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> comentarioService.crear(99L, new ComentarioRequest("Hola"), CAMILA))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void eliminarRechazaComentariosDeOtroUsuario() {
        Comentario comentario = new Comentario();
        comentario.setAutorId(3L);
        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario));

        assertThatThrownBy(() -> comentarioService.eliminar(1L, DIEGO))
                .isInstanceOf(AccionNoPermitidaException.class);
        verify(comentarioRepository, never()).delete(any());
    }
}
