package cl.duoc.foro.publicaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.foro.publicaciones.dto.TemaRequest;
import cl.duoc.foro.publicaciones.dto.TemaResponse;
import cl.duoc.foro.publicaciones.exception.AccionNoPermitidaException;
import cl.duoc.foro.publicaciones.exception.RecursoNoEncontradoException;
import cl.duoc.foro.publicaciones.model.Categoria;
import cl.duoc.foro.publicaciones.model.Tema;
import cl.duoc.foro.publicaciones.repository.CategoriaRepository;
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
class TemaServiceTest {

    private static final UsuarioAutenticado CAMILA = new UsuarioAutenticado(3L, "camila", "USUARIO");
    private static final UsuarioAutenticado DIEGO = new UsuarioAutenticado(4L, "diego", "USUARIO");
    private static final UsuarioAutenticado MODERADOR = new UsuarioAutenticado(2L, "moderador", "MODERADOR");

    @Mock
    private TemaRepository temaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private TemaService temaService;

    @Test
    void crearTomaElAutorDesdeElToken() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria()));
        when(temaRepository.save(any(Tema.class))).thenAnswer(llamada -> llamada.getArgument(0));

        TemaResponse creado = temaService.crear(new TemaRequest("Titulo", "Contenido", 1L), CAMILA);

        assertThat(creado.autorId()).isEqualTo(3L);
        assertThat(creado.autorNombre()).isEqualTo("camila");
    }

    @Test
    void crearFallaSiLaCategoriaNoExiste() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temaService.crear(new TemaRequest("Titulo", "Contenido", 99L), CAMILA))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void actualizarSoloLoPuedeHacerElAutor() {
        when(temaRepository.findById(1L)).thenReturn(Optional.of(temaDe(3L)));

        assertThatThrownBy(() -> temaService.actualizar(1L, new TemaRequest("Otro", "Otro", 1L), DIEGO))
                .isInstanceOf(AccionNoPermitidaException.class);
        assertThatThrownBy(() -> temaService.actualizar(1L, new TemaRequest("Otro", "Otro", 1L), MODERADOR))
                .isInstanceOf(AccionNoPermitidaException.class);
    }

    @Test
    void eliminarRechazaAUnUsuarioQueNoEsElAutor() {
        when(temaRepository.findById(1L)).thenReturn(Optional.of(temaDe(3L)));

        assertThatThrownBy(() -> temaService.eliminar(1L, DIEGO)).isInstanceOf(AccionNoPermitidaException.class);
        verify(temaRepository, never()).delete(any(Tema.class));
    }

    @Test
    void eliminarPermiteAlModeradorBorrarTemasDeOtros() {
        Tema tema = temaDe(3L);
        when(temaRepository.findById(1L)).thenReturn(Optional.of(tema));

        assertThatCode(() -> temaService.eliminar(1L, MODERADOR)).doesNotThrowAnyException();
        verify(temaRepository).delete(tema);
    }

    private Categoria categoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Tecnología");
        return categoria;
    }

    private Tema temaDe(Long autorId) {
        Tema tema = new Tema();
        tema.setAutorId(autorId);
        tema.setCategoria(categoria());
        return tema;
    }
}
