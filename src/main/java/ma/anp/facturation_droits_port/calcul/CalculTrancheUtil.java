package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.TarifTranche;
import ma.anp.facturation_droits_port.exception.TrancheIntrouvableException;

import java.util.Comparator;
import java.util.List;

public final class CalculTrancheUtil {

    private CalculTrancheUtil() {
    }

    public static double cumulProgressif(List<TarifTranche> tranches, double valeur) {
        double montant = 0;
        List<TarifTranche> tranchesTriees = tranches.stream().sorted(Comparator.comparing(TarifTranche::getValeurMin))
                .toList();
        for (TarifTranche t : tranchesTriees) {
            double bornBasse = t.getValeurMin();
            double bornHaute = t.getValeurMax() != null ? t.getValeurMax() : valeur;
            if (valeur <= bornBasse)
                break;
            double volumeDansTranche = Math.min(valeur, bornHaute) - bornBasse;
            if (volumeDansTranche > 0)
                montant = montant + (volumeDansTranche * t.getMontant());
        }
        return montant;
    }

    public static double tauxUnitaire(List<TarifTranche> tranches, double valeur) {
        return tranches.stream()
                .filter(t -> valeur >= t.getValeurMin() && (t.getValeurMax() == null || valeur < t.getValeurMax()))
                .findFirst()
                .map(TarifTranche::getMontant)
                .orElseThrow(() -> new TrancheIntrouvableException(valeur));
    }
}
