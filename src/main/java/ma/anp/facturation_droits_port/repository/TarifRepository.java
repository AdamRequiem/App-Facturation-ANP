package ma.anp.facturation_droits_port.repository;

import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarifRepository extends JpaRepository<Tarif, Long> {

    Optional<Tarif> findByTypeOperationAndTypeNavireAndPort_IdPort(
            TypeOperation typeOperation, TypeNavire typeNavire, Long idPort);

    Optional<Tarif> findByTypeOperationAndTypeNavireAndPortIsNull(
            TypeOperation typeOperation, TypeNavire typeNavire);
}
