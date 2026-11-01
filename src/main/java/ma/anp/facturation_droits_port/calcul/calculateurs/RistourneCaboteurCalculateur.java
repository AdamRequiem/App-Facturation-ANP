package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.CalculateurRistounre;
import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.enums.TypeRistourne;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RistourneCaboteurCalculateur implements CalculateurRistounre {

    private static final Set<String> PORTS_HYDROCARBURES_74 = Set.of("Tan-Tan", "Dakhla", "Laâyoune");
    final String MARCHANDISE_HYDROCARBURES  = "HYDROCARBURES";

    @Override
    public boolean supporte(TypeRistourne type) {
        return type == TypeRistourne.A_CABOTEURS;
    }

    @Override
    public double taux(ContexteCalcul ctx) {
        boolean estHydrocarbures = ctx.marchandise() !=null
                && MARCHANDISE_HYDROCARBURES.equalsIgnoreCase(ctx.marchandise().getNom());
        boolean portEligible74 = PORTS_HYDROCARBURES_74.contains(ctx.port().getNom());

        return (estHydrocarbures && portEligible74) ? 0.74 : 0.60;
    }
}
