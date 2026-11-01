package ma.anp.facturation_droits_port.exception;

public class TrancheIntrouvableException extends RuntimeException {
    public TrancheIntrouvableException(double valeur) {
        super("Aucune tranche ne couvre la valeur "+valeur);
    }
}
