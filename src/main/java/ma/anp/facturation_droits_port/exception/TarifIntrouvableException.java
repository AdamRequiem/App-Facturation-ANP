package ma.anp.facturation_droits_port.exception;

import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;

public class TarifIntrouvableException extends RuntimeException{

    public TarifIntrouvableException (TypeOperation operation, TypeNavire typeNavire, Port port) {
        super("Aucun tarif trouvé pour opération=" + operation + ", typeNavire=" + typeNavire + ", port=" + (port !=null ? port.getNom() : null));
    }
}
