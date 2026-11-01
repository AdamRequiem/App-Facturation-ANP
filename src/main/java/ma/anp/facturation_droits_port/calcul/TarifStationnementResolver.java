package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.exception.TarifIntrouvableException;
import ma.anp.facturation_droits_port.repository.TarifRepository;
import org.springframework.stereotype.Component;

@Component
public class TarifStationnementResolver {

    private final TarifRepository tarifRepository;

    public TarifStationnementResolver(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    public Tarif resoudreTarif(TypeOperation operation, TypeNavire typeNavire, Port port) {
        return tarifRepository
                .findByTypeOperationAndTypeNavireAndPort_IdPort(operation, typeNavire, port.getIdPort())
                .or(() -> tarifRepository.findByTypeOperationAndTypeNavireAndPortIsNull(operation, typeNavire))
                .orElseThrow(() -> new TarifIntrouvableException(operation, typeNavire, port));
    }

    public double tauxJournalier(Tarif tarif, double tjb) {
        return CalculTrancheUtil.cumulProgressif(tarif.getTarifTranches(), tjb);
    }
}
