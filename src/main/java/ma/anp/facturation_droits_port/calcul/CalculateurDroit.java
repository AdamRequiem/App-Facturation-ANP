package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.enums.TypeOperation;

public interface CalculateurDroit {

    boolean supporte(TypeOperation operation, Port port);
    MontantDroit calculer (ContexteCalcul contexte);
}
