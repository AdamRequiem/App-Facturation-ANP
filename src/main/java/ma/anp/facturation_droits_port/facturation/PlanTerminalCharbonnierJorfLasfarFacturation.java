package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import org.springframework.stereotype.Component;

@Component
public class PlanTerminalCharbonnierJorfLasfarFacturation extends PlanTerminalCharbonnierFacturationBase {

    public PlanTerminalCharbonnierJorfLasfarFacturation() {
        super(TypeTerminal.CHARBONNIER_JORF, TypeOperation.ENTREE_CHARBON_JORF, TypeOperation.SORTIE_CHARBON_JORF, TypeOperation.SEJOUR_CHARBON_JORF);
    }
}
