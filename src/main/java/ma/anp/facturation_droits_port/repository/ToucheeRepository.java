package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Touchee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToucheeRepository extends JpaRepository<Touchee, Long> {

    Optional<Touchee> findByNavire_IdNavireAndPort_IdPortAndAnnee(Long idNavire, Long idPort, int annee);
}
