package cl.duoc.foro.publicaciones.repository;

import cl.duoc.foro.publicaciones.model.Tema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemaRepository extends JpaRepository<Tema, Long>, JpaSpecificationExecutor<Tema> {

    @Override
    @EntityGraph(attributePaths = "categoria")
    Page<Tema> findAll(Specification<Tema> filtro, Pageable pagina);

    boolean existsByCategoriaId(Long categoriaId);
}
