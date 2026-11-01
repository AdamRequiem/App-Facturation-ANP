package ma.anp.facturation_droits_port.calcul;

public final class TempsUtil {

    private TempsUtil() {}

    public static long joursEntames(long heures) {
        return(long) Math.ceil(heures/24.0);
    }
}
