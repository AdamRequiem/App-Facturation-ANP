package ma.anp.facturation_droits_port.service;

import ma.anp.facturation_droits_port.calcul.CalculTrancheUtil;
import ma.anp.facturation_droits_port.entity.Tarif;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AbonnementService {

    private static final double REDUCTION_ABONNEMENT = 0.50;
    private static final long JOURS_FORFAIT = 30;

    private record PeriodeMois(Month mois, long joursCouverts) {}

    public double calculerMontant(Tarif tarif, double tjb, LocalDateTime debut, LocalDateTime fin) {
        double tauxJournalier = CalculTrancheUtil.cumulProgressif(tarif.getTarifTranches(), tjb);

        double total = 0;
        for (PeriodeMois periode : decouperParMoisCalendaire(debut, fin)) {
            int seuilMoisComplet = periode.mois() == Month.FEBRUARY ? 28 : 30;
            boolean moisComplet = periode.joursCouverts() >= seuilMoisComplet;

            total += moisComplet
                    ? tauxJournalier * JOURS_FORFAIT * REDUCTION_ABONNEMENT
                    : tauxJournalier * periode.joursCouverts();
        }
        return total;
    }

    private List<PeriodeMois> decouperParMoisCalendaire(LocalDateTime debut, LocalDateTime fin) {
        List<PeriodeMois> periodes = new ArrayList<>();
        LocalDate curseur = debut.toLocalDate();
        LocalDate finDate = fin.toLocalDate();

        while (!curseur.isAfter(finDate)) {
            LocalDate finMoisCalendaire = curseur.withDayOfMonth(curseur.lengthOfMonth());
            LocalDate finPeriode = finDate.isBefore(finMoisCalendaire) ? finDate : finMoisCalendaire;
            long joursCouverts = ChronoUnit.DAYS.between(curseur, finPeriode) +1;

            periodes.add(new PeriodeMois(curseur.getMonth(), joursCouverts));
            curseur = finPeriode.plusDays(1);
        }
        return periodes;
    }
}
