package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Stationnement;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class PlanTerminalCharbonnierFacturationBase implements PlanFacturation{

    private final TypeTerminal typeTerminal;
    private final TypeOperation operationEntree;
    private final TypeOperation operationSortie;
    private final TypeOperation operationSejour;

    protected PlanTerminalCharbonnierFacturationBase(TypeTerminal typeTerminal, TypeOperation operationEntree, TypeOperation operationSortie, TypeOperation operationSejour) {
        this.typeTerminal = typeTerminal;
        this.operationEntree = operationEntree;
        this.operationSortie = operationSortie;
        this.operationSejour = operationSejour;
    }

    @Override
    public boolean supporte(Port port) {
        return port.getTypeTerminal() == typeTerminal;
    }

    @Override
    public List<LigneFacturable> resoudreLignes(Escale escale) {
        List<LigneFacturable> lignes = new ArrayList<>();

        for (Stationnement stationnement : escale.getStationnements()) {
            if(stationnement.isExoneration()) continue;

            lignes.add(LigneFacturable.pourStationnement(operationEntree, contexte(operationEntree, escale, stationnement), stationnement));
            lignes.add(LigneFacturable.pourStationnement(operationSortie, contexte(operationSortie, escale, stationnement), stationnement));
            lignes.add(LigneFacturable.pourStationnement(operationSejour, contexte(operationSejour, escale, stationnement), stationnement));
        }
        return lignes;
    }

    private ContexteCalcul contexte(TypeOperation operation, Escale escale, Stationnement stationnement) {
        long dureeHeures = Duration.between(escale.getDateEntree(), escale.getDateSortie()).toHours();
        return new ContexteCalcul(operation, escale, escale.getNavire(),
                escale.getPort(), stationnement.getMarchandise(), escale.getNavire().getTjb(),
                dureeHeures, escale.getDateEntree(), escale.getDateSortie(),
                stationnement.isAbonnement(), null);
    }
}
