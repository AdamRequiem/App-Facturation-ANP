package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Stationnement;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanTerminalPetrolierMohammediaFacturation implements PlanFacturation {

    private static final long SEUIL_SEJOUR_HEURES = 72;

    @Override
    public boolean supporte(Port port) {
        return port.getTypeTerminal() == TypeTerminal.PETROLIER_MOHAMMEDIA;
    }

    @Override
    public List<LigneFacturable> resoudreLignes(Escale escale) {
        List<LigneFacturable> lignes = new ArrayList<>();

        for (Stationnement stationnement : escale.getStationnements()) {
            if (stationnement.isExoneration())
                continue;

            if (stationnement.isEmbarquement()) {
                lignes.add(LigneFacturable.pourStationnement(
                        TypeOperation.STATIONNEMENT_EMBARQUEMENT_PETROLIER,
                        contexte(TypeOperation.STATIONNEMENT_EMBARQUEMENT_PETROLIER, escale, stationnement),
                        stationnement));
                continue;
            }

            lignes.add(LigneFacturable.pourStationnement(
                    TypeOperation.ENTREE_PETROLIER, contexte(TypeOperation.ENTREE_PETROLIER, escale, stationnement),
                    stationnement));
            lignes.add(LigneFacturable.pourStationnement(
                    TypeOperation.SORTIE_PETROLIER, contexte(TypeOperation.SORTIE_PETROLIER, escale, stationnement),
                    stationnement));

            long dureeHeures = Duration.between(escale.getDateEntree(), escale.getDateSortie()).toHours();
            if (dureeHeures > SEUIL_SEJOUR_HEURES) {
                lignes.add(LigneFacturable.pourStationnement(
                        TypeOperation.SEJOUR_PETROLIER, contexte(TypeOperation.SEJOUR_PETROLIER, escale, stationnement),
                        stationnement));
            }
        }
        return lignes;
    }

    public ContexteCalcul contexte(TypeOperation operation, Escale escale, Stationnement stationnement) {
        long dureeHeures = Duration.between(escale.getDateEntree(), escale.getDateSortie()).toHours();
        return new ContexteCalcul(
                operation, escale, escale.getNavire(), escale.getPort(),
                stationnement.getMarchandise(), escale.getNavire().getTjb(),
                dureeHeures, escale.getDateEntree(), escale.getDateSortie(),
                stationnement.isAbonnement(), null);
    }
}
