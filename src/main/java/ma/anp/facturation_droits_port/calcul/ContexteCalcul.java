package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Marchandise;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeRistourne;

import java.time.LocalDateTime;

public record ContexteCalcul(
        TypeOperation typeOperation,
        Escale escale,
        Navire navire,
        Port port,
        Marchandise marchandise,
        double tjb,
        long dureeHeures,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        boolean abonnement,
        TypeRistourne typeRistourneDemandee
) {
}
