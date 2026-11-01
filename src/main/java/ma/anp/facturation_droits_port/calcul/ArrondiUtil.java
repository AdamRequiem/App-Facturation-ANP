package ma.anp.facturation_droits_port.calcul;

public final class ArrondiUtil {
    private ArrondiUtil() {}

    public static double arrondirSuperieur(double montant) {
        return Math.ceil(montant);
    }
}
