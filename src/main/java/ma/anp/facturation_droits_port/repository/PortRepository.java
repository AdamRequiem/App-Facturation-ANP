package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortRepository extends JpaRepository<Port, Long> {
}
