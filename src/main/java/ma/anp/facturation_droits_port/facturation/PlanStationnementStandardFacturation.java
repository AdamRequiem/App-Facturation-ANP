package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.entity.*;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanStationnementStandardFacturation implements PlanFacturation {

    @Override
    public boolean supporte(Port port) {
        return port.getTypeTerminal() == TypeTerminal.STANDARD;
    }

    @Override
    public List<LigneFacturable> resoudreLignes(Escale escale) {
        List<LigneFacturable> lignes = new ArrayList<>();

        for (Entree entree : escale.getEntrees()) {
            if (entree.isExoneration() || !entree.isFacturationNext())
                continue;
            lignes.add(LigneFacturable.pourEntree(TypeOperation.ENTREE_PORT, contexteEntree(escale, entree), entree));
        }

        escale.getMouillages().stream().findFirst().ifPresent(mouillage -> {
            if (!mouillage.isExoneration()) {
                lignes.add(LigneFacturable.pourMouillage(TypeOperation.MOUILLAGE, contexteMouillage(escale, mouillage),
                        mouillage));
            }
        });

        escale.getStationnements().stream().findFirst().ifPresent(stationnement -> {
            if (!stationnement.isExoneration()) {
                lignes.add(LigneFacturable.pourStationnement(TypeOperation.STATIONNEMENT,
                        contexteStationnement(escale, stationnement), stationnement));
            }
        });

        return lignes;
    }

    private LocalDateTime debutStationnement(Escale escale) {
        return escale.getMouillages().stream().findFirst().map(Mouillage::getDateAccostage)
                .orElse(escale.getDateEntree());
    }

    private ContexteCalcul contexteEntree(Escale escale, Entree entree) {
        return new ContexteCalcul(
                TypeOperation.ENTREE_PORT, escale, escale.getNavire(), escale.getPort(), null,
                escale.getNavire().getTjb(), 0, escale.getDateEntree(), escale.getDateSortie(), false,
                entree.getTypeRistourne());
    }

    private ContexteCalcul contexteMouillage(Escale escale, Mouillage mouillage) {
        long heures = Duration.between(escale.getDateEntree(), mouillage.getDateAccostage()).toHours();
        return new ContexteCalcul(
                TypeOperation.MOUILLAGE, escale, escale.getNavire(), escale.getPort(), mouillage.getMarchandise(),
                escale.getNavire().getTjb(), heures, escale.getDateEntree(), mouillage.getDateAccostage(),
                mouillage.isAbonnement(), mouillage.getTypeRistourne());
    }

    private ContexteCalcul contexteStationnement(Escale escale, Stationnement stationnement) {
        LocalDateTime debut = debutStationnement(escale);
        LocalDateTime fin = escale.getDateSortie();
        long heures = Duration.between(debut, fin).toHours();
        return new ContexteCalcul(
                TypeOperation.STATIONNEMENT, escale, escale.getNavire(), escale.getPort(),
                stationnement.getMarchandise(), escale.getNavire().getTjb(), heures, debut, fin,
                stationnement.isAbonnement(), stationnement.getTypeRistourne());
    }
}
