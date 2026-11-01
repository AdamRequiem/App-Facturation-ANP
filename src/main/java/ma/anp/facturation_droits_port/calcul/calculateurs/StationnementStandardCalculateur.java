package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.*;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import ma.anp.facturation_droits_port.service.AbonnementService;
import org.springframework.stereotype.Component;

@Component
public class StationnementStandardCalculateur implements CalculateurDroit {

    private final TarifStationnementResolver tarifResolver;
    private final AbonnementService abonnementService;

    public StationnementStandardCalculateur(TarifStationnementResolver tarifResolver, AbonnementService abonnementService) {
        this.tarifResolver = tarifResolver;
        this.abonnementService = abonnementService;
    }

    @Override
    public boolean supporte(TypeOperation operation, Port port) {
        return operation == TypeOperation.STATIONNEMENT && port.getTypeTerminal() == TypeTerminal.STANDARD;
    }

    @Override
    public MontantDroit calculer(ContexteCalcul ctx) {
        TypeNavire typeNavire = ctx.navire().isTransbordeur() ? TypeNavire.TRANSBORDEUR : TypeNavire.AUTRE;
        Tarif tarif = tarifResolver.resoudreTarif(TypeOperation.STATIONNEMENT, typeNavire, ctx.port());

        if(ctx.abonnement()) {
            double montant = abonnementService.calculerMontant(tarif, ctx.tjb(), ctx.dateDebut(), ctx.dateFin());
            // Pas de quantité/taux uniques : l'abonnement applique un forfait 30j/-50% ou le
            // plein tarif selon chaque mois calendaire couvert, donc aucune paire (quantité, taux)
            // ne reconstruit fidèlement ce montant composite.
            return new MontantDroit(montant, "EUR", tarif, null, "forfait mensuel (abonnement)", null);
        }

        double tauxJournalier = tarifResolver.tauxJournalier(tarif, ctx.tjb());
        long jours = TempsUtil.joursEntames(ctx.dureeHeures());
        return new MontantDroit(tauxJournalier*jours, "EUR", tarif, (double) jours, "jours", tauxJournalier);
    }


}
