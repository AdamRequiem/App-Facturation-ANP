package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.DetailFacture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailFactureRepository extends JpaRepository<DetailFacture, Long> {
}
