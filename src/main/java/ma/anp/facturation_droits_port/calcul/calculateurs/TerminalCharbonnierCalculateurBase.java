package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.*;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.exception.TarifIntrouvableException;
import ma.anp.facturation_droits_port.repository.TarifRepository;

public abstract class TerminalCharbonnierCalculateurBase implements CalculateurDroit {

    private final TarifRepository tarifRepository;
    private final TypeTerminal typeTerminal;
    private final TypeOperation operationEntree;
    private final TypeOperation operationSortie;
    private final TypeOperation operationSejour;

    protected TerminalCharbonnierCalculateurBase(TarifRepository tarifRepository, TypeTerminal typeTerminal, TypeOperation operationEntree, TypeOperation operationSortie, TypeOperation operationSejour) {
        this.tarifRepository = tarifRepository;
        this.typeTerminal = typeTerminal;
        this.operationEntree = operationEntree;
        this.operationSortie = operationSortie;
        this.operationSejour = operationSejour;
    }

    @Override
    public boolean supporte(TypeOperation operation, Port port) {
        return port.getTypeTerminal() == typeTerminal && (operation == operationEntree || operation == operationSortie || operation == operationSejour);
    }

    @Override
    public MontantDroit calculer(ContexteCalcul ctx) {
        if(ctx.typeOperation() == operationEntree || ctx.typeOperation() == operationSortie) {
            return calculerDebarquement(ctx);
        }
        if(ctx.typeOperation() == operationSejour) {
            return calculerSejour(ctx);
        }
        throw new ResourceNotFoundException("Opération non gérée par ce calculateur : "+ctx.typeOperation());
    }

    public MontantDroit calculerDebarquement(ContexteCalcul ctx) {
        double vg = VolumeGeometriqueUtil.calculer(ctx.navire());
        Tarif tarif = resoudreTarif(ctx.typeOperation(), ctx.port());
        double taux = CalculTrancheUtil.tauxUnitaire(tarif.getTarifTranches(), vg);
        return new MontantDroit(vg*taux, "EUR", tarif, vg, "m³", taux);
    }

    private MontantDroit calculerSejour(ContexteCalcul ctx) {
        double vg = VolumeGeometriqueUtil.calculer(ctx.navire());
        Tarif tarif = resoudreTarif(operationSejour, ctx.port());
        double tauxJournalier = CalculTrancheUtil.tauxUnitaire(tarif.getTarifTranches(), vg);
        long jours = TempsUtil.joursEntames(ctx.dureeHeures());
        // Taux composé (VG × taux/m³/jour) : le tarif publié est au m³, mais la quantité
        // facturée est en jours ; ce taux journalier "effectif" reconstruit le montant total.
        double tauxJournalierEffectif = vg * tauxJournalier;
        return new MontantDroit(tauxJournalierEffectif * jours, "EUR", tarif, (double) jours, "jours", tauxJournalierEffectif);
    }

    private Tarif resoudreTarif(TypeOperation operation, Port port) {
        return tarifRepository
                .findByTypeOperationAndTypeNavireAndPort_IdPort(operation, TypeNavire.TOUS, port.getIdPort())
                .orElseThrow(()-> new TarifIntrouvableException(operation, TypeNavire.TOUS, port));
    }
}
