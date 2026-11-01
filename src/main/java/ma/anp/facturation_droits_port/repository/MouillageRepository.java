package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Mouillage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouillageRepository extends JpaRepository<Mouillage, Long> {
}
