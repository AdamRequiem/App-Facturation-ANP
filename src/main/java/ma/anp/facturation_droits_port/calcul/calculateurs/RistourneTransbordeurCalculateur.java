package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.CalculateurRistounre;
import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.enums.TypeRistourne;
import org.springframework.stereotype.Component;

@Component
public class RistourneTransbordeurCalculateur implements CalculateurRistounre {

    private static final double TAUX_UNIQUE = 0.67;

    @Override
    public boolean supporte(TypeRistourne type) {
        return type == TypeRistourne.D_TRANSBORDEURS;
    }

    @Override
    public double taux(ContexteCalcul ctx) {
        return ctx.navire().isTransbordeur() ? TAUX_UNIQUE : 0.0;
    }
}
