package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Entree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntreeRepository extends JpaRepository<Entree, Long> {
}
