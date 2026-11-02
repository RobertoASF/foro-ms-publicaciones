package cl.duoc.foro.publicaciones.dto;

import cl.duoc.foro.publicaciones.model.Categoria;

public record CategoriaResponse(Long id, String nombre, String descripcion) {

    public static CategoriaResponse desde(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }
}
