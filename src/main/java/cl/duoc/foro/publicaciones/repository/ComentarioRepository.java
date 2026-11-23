package cl.duoc.foro.publicaciones.repository;

import cl.duoc.foro.publicaciones.model.Comentario;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByTemaIdAndBaneadoOrderByFechaCreacionAsc(Long temaId, boolean baneado);

    long countByTemaIdAndBaneado(Long temaId, boolean baneado);

    @Query("""
            SELECT c.tema.id AS temaId, COUNT(c) AS total
            FROM Comentario c
            WHERE c.tema.id IN :temaIds AND c.baneado = false
            GROUP BY c.tema.id
            """)
    List<TotalComentarios> contarPorTemas(@Param("temaIds") Collection<Long> temaIds);

    @Modifying
    @Query("UPDATE Comentario c SET c.baneado = :baneado WHERE c.id = :id")
    int cambiarBaneo(@Param("id") Long id, @Param("baneado") boolean baneado);

    interface TotalComentarios {
        Long getTemaId();

        long getTotal();
    }
}
