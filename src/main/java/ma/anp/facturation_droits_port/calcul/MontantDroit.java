package ma.anp.facturation_droits_port.calcul;

import ma.anp.facturation_droits_port.entity.Tarif;

/**
 * Résultat d'un calcul de droit. quantite/uniteQuantite/taux forment la décomposition
 * affichée sur la facture (ex: "3 jours × 82,50 €" ou "4 250,00 m³ × 0,19 €"), calculée
 * AVANT application d'une éventuelle ristourne — le montant final (après ristourne et
 * arrondi Art 8) peut donc différer de quantite × taux. C'est volontaire : ça permet de
 * vérifier le calcul tarifaire de base indépendamment de la ristourne.
 */
public record MontantDroit(double montant, String devise, Tarif tarif, Double quantite, String uniteQuantite, Double taux) {
}
