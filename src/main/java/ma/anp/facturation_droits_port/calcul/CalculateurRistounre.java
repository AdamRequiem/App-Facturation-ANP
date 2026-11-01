package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.enums.TypeRistourne;

public interface CalculateurRistounre {
    boolean supporte(TypeRistourne type);
    double taux(ContexteCalcul ctx);
}
