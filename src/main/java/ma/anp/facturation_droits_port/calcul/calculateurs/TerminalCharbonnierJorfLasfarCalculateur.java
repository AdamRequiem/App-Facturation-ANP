package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import ma.anp.facturation_droits_port.repository.TarifRepository;
import org.springframework.stereotype.Component;

@Component
public class TerminalCharbonnierJorfLasfarCalculateur extends TerminalCharbonnierCalculateurBase{

    public TerminalCharbonnierJorfLasfarCalculateur(TarifRepository tarifRepository) {
        super(tarifRepository,
                TypeTerminal.CHARBONNIER_JORF,
                TypeOperation.ENTREE_CHARBON_JORF,
                TypeOperation.SORTIE_CHARBON_JORF,
                TypeOperation.SEJOUR_CHARBON_JORF);
    }
}
