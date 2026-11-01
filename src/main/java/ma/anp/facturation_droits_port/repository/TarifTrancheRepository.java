package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.TarifTranche;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifTrancheRepository extends JpaRepository<TarifTranche, Long> {
}
