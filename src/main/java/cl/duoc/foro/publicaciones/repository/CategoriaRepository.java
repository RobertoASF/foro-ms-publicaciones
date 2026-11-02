package cl.duoc.foro.publicaciones.repository;

import cl.duoc.foro.publicaciones.dto.CategoriaResumenResponse;
import cl.duoc.foro.publicaciones.model.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByOrderByNombreAsc();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    @Query("""
            SELECT new cl.duoc.foro.publicaciones.dto.CategoriaResumenResponse(
                       c.id, c.nombre, COUNT(DISTINCT t.id), COUNT(co.id), MAX(t.fechaCreacion))
            FROM Categoria c
            LEFT JOIN Tema t ON t.categoria = c
            LEFT JOIN Comentario co ON co.tema = t
            GROUP BY c.id, c.nombre
            ORDER BY COUNT(DISTINCT t.id) DESC, c.nombre
            """)
    List<CategoriaResumenResponse> resumir();
}
