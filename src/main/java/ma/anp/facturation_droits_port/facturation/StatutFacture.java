package ma.anp.facturation_droits_port.facturation;

public final class StatutFacture {

    private StatutFacture() {}

    /** Facture générée par le moteur, montants calculés mais pas encore validée par un agent ANP. */
    public static final String BROUILLON = "BROUILLON";

    /** Facture validée : visible dans l'historique et comptabilisée au tableau de bord. */
    public static final String VALIDEE = "VALIDEE";
}
