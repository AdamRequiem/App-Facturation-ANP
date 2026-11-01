package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Stationnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationnementRepository extends JpaRepository<Stationnement, Long> {
}
