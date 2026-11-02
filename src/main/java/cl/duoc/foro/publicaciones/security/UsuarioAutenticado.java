package cl.duoc.foro.publicaciones.security;

import org.springframework.security.oauth2.jwt.Jwt;

public record UsuarioAutenticado(Long id, String nombreUsuario, String rol) {

    public static UsuarioAutenticado desde(Jwt jwt) {
        Number id = jwt.getClaim("uid");
        return new UsuarioAutenticado(id.longValue(), jwt.getSubject(), jwt.getClaimAsString("rol"));
    }

    public boolean esAutor(Long autorId) {
        return id.equals(autorId);
    }

    public boolean puedeBorrarDe(Long autorId) {
        return esAutor(autorId) || "ADMIN".equals(rol) || "MODERADOR".equals(rol);
    }
}
