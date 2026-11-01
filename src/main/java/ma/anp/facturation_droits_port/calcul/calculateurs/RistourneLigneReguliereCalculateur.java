package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.CalculateurRistounre;
import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.enums.TypeRistourne;
import ma.anp.facturation_droits_port.service.ToucheeService;
import org.springframework.stereotype.Component;

@Component
public class RistourneLigneReguliereCalculateur implements CalculateurRistounre {

    private static final int MIN_TOUCHEES_ELIGIBLE = 12;

    private final ToucheeService toucheeService;

    public RistourneLigneReguliereCalculateur(ToucheeService toucheeService) {
        this.toucheeService = toucheeService;
    }

    @Override
    public boolean supporte(TypeRistourne type) {
        return type == TypeRistourne.C_LIGNES_REGULIERES;
    }

    @Override
    public double taux(ContexteCalcul ctx) {
        int annee = ctx.escale().getDateEntree().getYear();
        int touchee = toucheeService.nombreTouchees(ctx.navire(), ctx.port(), annee);
        if (touchee < MIN_TOUCHEES_ELIGIBLE) return 0.0;
        if (touchee <= 35) return 0.30;
        if (touchee <= 59) return 0.45;
        return 0.60;
    }
}
