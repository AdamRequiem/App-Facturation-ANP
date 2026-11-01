package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import ma.anp.facturation_droits_port.repository.TarifRepository;
import org.springframework.stereotype.Component;

@Component
public class TerminalCharbonnierSafiCalculateur extends TerminalCharbonnierCalculateurBase{

    public TerminalCharbonnierSafiCalculateur(TarifRepository tarifRepository) {
        super(tarifRepository,
                TypeTerminal.CHARBONNIER_SAFI,
                TypeOperation.ENTREE_CHARBON_SAFI,
                TypeOperation.SORTIE_CHARBON_SAFI,
                TypeOperation.SEJOUR_CHARBON_SAFI);
    }
}
