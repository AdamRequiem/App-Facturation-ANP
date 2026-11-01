package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.CalculateurRistounre;
import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.enums.TypeRistourne;
import ma.anp.facturation_droits_port.service.ToucheeService;
import org.springframework.stereotype.Component;

@Component
public class RistourneToucheesCalculateur implements CalculateurRistounre {

    private static final int SEUIL_TOUCHEES = 50;

    private final ToucheeService toucheeService;

    public RistourneToucheesCalculateur(ToucheeService toucheeService) {
        this.toucheeService = toucheeService;
    }

    @Override
    public boolean supporte(TypeRistourne type) {
        return type == TypeRistourne.B_NOMBRE_TOUCHEES;
    }

    @Override
    public double taux(ContexteCalcul ctx) {
        int annee = ctx.escale().getDateEntree().getYear();
        int touchees = toucheeService.nombreTouchees(ctx.navire(), ctx.port(), annee);
        if (touchees <= SEUIL_TOUCHEES)
            return 0.0;

        double tjb = ctx.tjb();
        if (tjb <= 5000)
            return 0.35;
        if (tjb <= 10000)
            return 0.45;
        return 0.55;
    }
}
