package cl.duoc.foro.publicaciones.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.foro.publicaciones.dto.CategoriaRequest;
import cl.duoc.foro.publicaciones.exception.ConflictoException;
import cl.duoc.foro.publicaciones.model.Categoria;
import cl.duoc.foro.publicaciones.repository.CategoriaRepository;
import cl.duoc.foro.publicaciones.repository.TemaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private TemaRepository temaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void crearRechazaNombreRepetido() {
        when(categoriaRepository.existsByNombreIgnoreCase("Yu-Gi-Oh!")).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.crear(new CategoriaRequest("Yu-Gi-Oh!", "Descripción")))
                .isInstanceOf(ConflictoException.class);
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void eliminarRechazaCategoriaConTemas() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(new Categoria()));
        when(temaRepository.existsByCategoriaId(1L)).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.eliminar(1L)).isInstanceOf(ConflictoException.class);
        verify(categoriaRepository, never()).delete(any());
    }
}
