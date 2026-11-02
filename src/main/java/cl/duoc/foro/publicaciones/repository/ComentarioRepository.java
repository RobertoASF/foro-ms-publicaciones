package cl.duoc.foro.publicaciones.repository;

import cl.duoc.foro.publicaciones.model.Comentario;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByTemaIdOrderByFechaCreacionAsc(Long temaId);

    long countByTemaId(Long temaId);

    @Query("""
            SELECT c.tema.id AS temaId, COUNT(c) AS total
            FROM Comentario c
            WHERE c.tema.id IN :temaIds
            GROUP BY c.tema.id
            """)
    List<TotalComentarios> contarPorTemas(@Param("temaIds") Collection<Long> temaIds);

    interface TotalComentarios {
        Long getTemaId();

        long getTotal();
    }
}
