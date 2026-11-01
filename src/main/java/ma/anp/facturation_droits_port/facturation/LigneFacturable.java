package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.entity.Entree;
import ma.anp.facturation_droits_port.entity.Mouillage;
import ma.anp.facturation_droits_port.entity.Stationnement;
import ma.anp.facturation_droits_port.enums.TypeOperation;

public record LigneFacturable(
        TypeOperation operation,
        ContexteCalcul contexte,
        Entree entreeSource,
        Mouillage mouillageSource,
        Stationnement stationnementSource) {

    public static LigneFacturable pourEntree(TypeOperation op, ContexteCalcul ctx, Entree entree) {
        return new LigneFacturable(op, ctx, entree, null, null);
    }

    public static LigneFacturable pourMouillage(TypeOperation op, ContexteCalcul ctx, Mouillage mouillage) {
        return new LigneFacturable(op, ctx, null, mouillage, null);
    }

    public static LigneFacturable pourStationnement(TypeOperation op, ContexteCalcul ctx, Stationnement stationnement) {
        return new LigneFacturable(op, ctx, null, null, stationnement);
    }
}


