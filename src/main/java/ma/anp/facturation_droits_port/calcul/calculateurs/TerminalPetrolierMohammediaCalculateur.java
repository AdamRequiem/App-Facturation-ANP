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
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TerminalPetrolierMohammediaCalculateur implements CalculateurDroit {

    private static final long SEUIL_SEJOUR_HEURES = 72;

    private static final Set<TypeOperation> OPERATIONS_SUPPORTEES = Set.of(
            TypeOperation.ENTREE_PETROLIER,
            TypeOperation.SORTIE_PETROLIER,
            TypeOperation.SEJOUR_PETROLIER,
            TypeOperation.STATIONNEMENT_EMBARQUEMENT_PETROLIER);

    private final TarifRepository tarifRepository;

    public TerminalPetrolierMohammediaCalculateur(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    @Override
    public boolean supporte(TypeOperation operation, Port port) {
        return port.getTypeTerminal() == TypeTerminal.PETROLIER_MOHAMMEDIA && OPERATIONS_SUPPORTEES.contains(operation);
    }

    @Override
    public MontantDroit calculer(ContexteCalcul ctx) {
        return switch (ctx.typeOperation()) {
            case ENTREE_PETROLIER, SORTIE_PETROLIER -> calculerDebarquement(ctx);
            case SEJOUR_PETROLIER -> calculerSejourSupplementaire(ctx);
            case STATIONNEMENT_EMBARQUEMENT_PETROLIER -> calculerEmbarquement(ctx);
            default ->
                throw new ResourceNotFoundException("Opération non gérée par ce calculateur : " + ctx.typeOperation());
        };
    }

    private Tarif resoudreTarif(TypeOperation operation, Port port) {
        return tarifRepository
                .findByTypeOperationAndTypeNavireAndPort_IdPort(operation, TypeNavire.TOUS, port.getIdPort())
                .orElseThrow(() -> new TarifIntrouvableException(operation, TypeNavire.TOUS, port));
    }

    private MontantDroit calculerDebarquement(ContexteCalcul ctx) {
        double vg = VolumeGeometriqueUtil.calculer(ctx.navire());
        Tarif tarif = resoudreTarif(ctx.typeOperation(), ctx.port());
        double taux = CalculTrancheUtil.tauxUnitaire(tarif.getTarifTranches(), vg);
        return new MontantDroit(vg * taux, "EUR", tarif, vg, "m³", taux);
    }

    private MontantDroit calculerSejourSupplementaire(ContexteCalcul ctx) {
        if (ctx.dureeHeures() <= SEUIL_SEJOUR_HEURES) {
            return new MontantDroit(0, "EUR", null, null, null, null);
        }
        Tarif tarif = resoudreTarif(TypeOperation.SEJOUR_PETROLIER, ctx.port());
        double tauxHoraire = CalculTrancheUtil.tauxUnitaire(tarif.getTarifTranches(), 0);
        long heuresSupplementaires = ctx.dureeHeures() - SEUIL_SEJOUR_HEURES;
        return new MontantDroit(heuresSupplementaires * tauxHoraire, "EUR", tarif, (double) heuresSupplementaires, "heures", tauxHoraire);
    }

    private MontantDroit calculerEmbarquement(ContexteCalcul ctx) {
        Tarif tarif = resoudreTarif(TypeOperation.STATIONNEMENT_EMBARQUEMENT_PETROLIER, ctx.port());
        double montantParJour = CalculTrancheUtil.cumulProgressif(tarif.getTarifTranches(), ctx.tjb());
        long jours = TempsUtil.joursEntames(ctx.dureeHeures());
        return new MontantDroit(montantParJour * jours, "EUR", tarif, (double) jours, "jours", montantParJour);
    }
}
