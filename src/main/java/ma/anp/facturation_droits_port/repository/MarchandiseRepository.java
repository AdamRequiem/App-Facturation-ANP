package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Marchandise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarchandiseRepository extends JpaRepository<Marchandise, Long> {
}
