package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.enums.RaisonEntree;

import java.util.Set;

public class RaisonEntreeUtil {

    private RaisonEntreeUtil() {}

    private static final Set<RaisonEntree> RAISON_SANS_REFACTURATION = Set.of(
            RaisonEntree.DEBARQUEMENT_CLANDESTINS_MALADES_CADAVRES,
            RaisonEntree.ALLEGEMENT_OU_PRELEVEMENT_ECHANTILLON,
            RaisonEntree.DEPART_CONDITIONS_CLIMATIQUES_OU_CONTRAINTE_PORTUAIRE,
            RaisonEntree.RELACHE
    );


    public static boolean doitEtreFacturee(RaisonEntree raison) {
        return !RAISON_SANS_REFACTURATION.contains(raison);
    }
}
