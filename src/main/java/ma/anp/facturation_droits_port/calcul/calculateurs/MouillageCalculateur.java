package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.*;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import org.springframework.stereotype.Component;

@Component
public class MouillageCalculateur implements CalculateurDroit {

    private static final double FRACTION_STATIONNEMENT = 0.25;

    private final TarifStationnementResolver tarifResolver;

    public MouillageCalculateur(TarifStationnementResolver tarifResolver) {
        this.tarifResolver = tarifResolver;
    }

    @Override
    public boolean supporte(TypeOperation operation, Port port) {
        return operation == TypeOperation.MOUILLAGE && port.getTypeTerminal() == TypeTerminal.STANDARD;
    }

    @Override
    public MontantDroit calculer(ContexteCalcul ctx) {
        TypeNavire typeNavire = ctx.navire().isTransbordeur() ? TypeNavire.TRANSBORDEUR : TypeNavire.AUTRE;

        Tarif tarif = tarifResolver.resoudreTarif(TypeOperation.STATIONNEMENT, typeNavire, ctx.port());
        double tauxJournalierStationnement = tarifResolver.tauxJournalier(tarif, ctx.tjb());

        long jours = TempsUtil.joursEntames(ctx.dureeHeures());
        double tauxMouillage = tauxJournalierStationnement * FRACTION_STATIONNEMENT;
        return new MontantDroit(tauxMouillage * jours, "EUR", tarif, (double) jours, "jours", tauxMouillage);
    }
}
