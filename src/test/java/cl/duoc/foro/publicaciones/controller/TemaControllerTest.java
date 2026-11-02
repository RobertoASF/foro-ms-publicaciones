package cl.duoc.foro.publicaciones.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.foro.publicaciones.config.SeguridadConfig;
import cl.duoc.foro.publicaciones.dto.PaginaResponse;
import cl.duoc.foro.publicaciones.security.RespuestasSeguridad;
import cl.duoc.foro.publicaciones.service.ComentarioService;
import cl.duoc.foro.publicaciones.service.TemaService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TemaController.class,
        properties = "jwt.secreto=clave-solo-para-pruebas-de-mas-de-32-caracteres")
@Import({SeguridadConfig.class, RespuestasSeguridad.class})
class TemaControllerTest {

    private static final String TEMA_VALIDO = """
            {"titulo": "Titulo", "contenido": "Contenido", "categoriaId": 1}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TemaService temaService;

    @MockitoBean
    private ComentarioService comentarioService;

    @Test
    void buscarNoPideToken() throws Exception {
        when(temaService.buscar(eq("java"), any(), any(), eq(0), eq(10)))
                .thenReturn(new PaginaResponse<>(List.of(), 0, 10, 0, 0));

        mockMvc.perform(get("/api/temas/buscar").param("texto", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos").value(0));
    }

    @Test
    void buscarRechazaUnTamanoDePaginaFueraDeRango() throws Exception {
        mockMvc.perform(get("/api/temas/buscar").param("tamano", "500"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400));
    }

    @Test
    void publicarSinTokenResponde401() throws Exception {
        mockMvc.perform(post("/api/temas").contentType(MediaType.APPLICATION_JSON).content(TEMA_VALIDO))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void publicarConTituloVacioIndicaElCampo() throws Exception {
        mockMvc.perform(post("/api/temas")
                        .with(jwt().jwt(datos -> datos.subject("camila").claim("uid", 3L).claim("rol", "USUARIO")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": " ", "contenido": "Contenido", "categoriaId": 1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.titulo").exists());
    }
}
