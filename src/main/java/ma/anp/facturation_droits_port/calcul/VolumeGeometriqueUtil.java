package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.Navire;

public final class VolumeGeometriqueUtil {

    private static final double COEFFICIENT_TIRANT_EAU_MIN = 0.14;

    private VolumeGeometriqueUtil() {}

    public static double calculer(Navire navire) {
        double l = navire.getLongueur();
        double b = navire.getLargeur();
        double teMinimum = COEFFICIENT_TIRANT_EAU_MIN * Math.sqrt(l*b);
        double te = Math.max(navire.getTirantEau(), teMinimum);
        return l * b* te;
    }
}
