package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import org.springframework.stereotype.Component;

@Component
public class PlanTerminalCharbonnierSafiFacturation extends PlanTerminalCharbonnierFacturationBase {

    public PlanTerminalCharbonnierSafiFacturation() {
        super(TypeTerminal.CHARBONNIER_SAFI, TypeOperation.ENTREE_CHARBON_SAFI, TypeOperation.SORTIE_CHARBON_SAFI, TypeOperation.SEJOUR_CHARBON_SAFI);
    }
}
