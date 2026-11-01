package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DroitCalculateurFactory {

    private final RistourneResolver ristourneResolver;
    private final List<CalculateurDroit> calculateurs;

    public DroitCalculateurFactory(RistourneResolver ristourneResolver, List<CalculateurDroit> calculateurs) {
        this.ristourneResolver = ristourneResolver;
        this.calculateurs = calculateurs;
    }

    public CalculateurDroit resoudre(TypeOperation operation, Port port) {
        return calculateurs.stream()
                .filter(c->c.supporte(operation, port))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Aucun calculateur pour "+ operation + " / port" + port.getNom()));
    }

    public MontantDroit calculer(TypeOperation operation, Port port, ContexteCalcul ctx) {
        MontantDroit brut = resoudre(operation, port).calculer(ctx);
        double montant = brut.montant();
        if(port.getTypeTerminal()== TypeTerminal.STANDARD && ctx.typeRistourneDemandee() !=null) {
            double taux = ristourneResolver.taux(ctx.typeRistourneDemandee(), ctx);
            montant = montant * (1-taux);
        }

        return new MontantDroit(ArrondiUtil.arrondirSuperieur(montant), brut.devise(), brut.tarif(), brut.quantite(), brut.uniteQuantite(), brut.taux());
    }
}
