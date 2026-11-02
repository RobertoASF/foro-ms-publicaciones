package cl.duoc.foro.publicaciones.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.foro.publicaciones.config.SeguridadConfig;
import cl.duoc.foro.publicaciones.dto.CategoriaResponse;
import cl.duoc.foro.publicaciones.security.RespuestasSeguridad;
import cl.duoc.foro.publicaciones.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CategoriaController.class,
        properties = "jwt.secreto=clave-solo-para-pruebas-de-mas-de-32-caracteres")
@Import({SeguridadConfig.class, RespuestasSeguridad.class})
class CategoriaControllerTest {

    private static final String CATEGORIA = """
            {"nombre": "Digimon TCG", "descripcion": "Mazos y torneos"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Test
    void crearConRolModeradorResponde403() throws Exception {
        mockMvc.perform(post("/api/categorias")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MODERADOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CATEGORIA))
                .andExpect(status().isForbidden());
    }

    @Test
    void crearConRolAdminResponde201() throws Exception {
        when(categoriaService.crear(any())).thenReturn(new CategoriaResponse(6L, "Digimon TCG", "Mazos y torneos"));

        mockMvc.perform(post("/api/categorias")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CATEGORIA))
                .andExpect(status().isCreated());
    }
}
