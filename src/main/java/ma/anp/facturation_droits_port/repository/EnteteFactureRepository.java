package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.EnteteFacture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnteteFactureRepository extends JpaRepository<EnteteFacture, Long> {
}
