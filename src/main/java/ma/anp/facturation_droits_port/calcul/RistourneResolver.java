package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.enums.TypeRistourne;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RistourneResolver {

    private final List<CalculateurRistounre> calculateurs;


    public RistourneResolver(List<CalculateurRistounre> calculateurs) {
        this.calculateurs = calculateurs;
    }

    public double taux(TypeRistourne type, ContexteCalcul ctx) {
        if(type == null) return 0.0;
        return calculateurs.stream()
                .filter(c -> c.supporte(type))
                .findFirst()
                .map(c-> c.taux(ctx))
                .orElseThrow(()-> new IllegalStateException("Aucun calculateur de ristourne pour "+type));
    }
}
