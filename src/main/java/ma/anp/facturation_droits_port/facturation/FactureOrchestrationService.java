package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.calcul.DroitCalculateurFactory;
import ma.anp.facturation_droits_port.calcul.MontantDroit;
import ma.anp.facturation_droits_port.entity.DetailFacture;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.exception.ResourceNotFoundException;
import ma.anp.facturation_droits_port.repository.DetailFactureRepository;
import ma.anp.facturation_droits_port.repository.EnteteFactureRepository;
import ma.anp.facturation_droits_port.repository.EscaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FactureOrchestrationService {

    private static final double TAUX_TVA = 0.20;

    private final List<PlanFacturation> plans;
    private final DroitCalculateurFactory droitCalculateurFactory;
    private final EnteteFactureRepository enteteFactureRepository;
    private final DetailFactureRepository detailFactureRepository;
    private final EscaleRepository escaleRepository;

    public FactureOrchestrationService(List<PlanFacturation> plans, DroitCalculateurFactory droitCalculateurFactory, EnteteFactureRepository enteteFactureRepository, DetailFactureRepository detailFactureRepository, EscaleRepository escaleRepository) {
        this.plans = plans;
        this.droitCalculateurFactory = droitCalculateurFactory;
        this.enteteFactureRepository = enteteFactureRepository;
        this.detailFactureRepository = detailFactureRepository;
        this.escaleRepository = escaleRepository;
    }

    @Transactional
    public EnteteFacture genererFacture(Escale escale) {
        PlanFacturation plan = plans.stream().filter(p -> p.supporte(escale.getPort())).findFirst().orElseThrow(()->new IllegalStateException("Aucun plan de facturation pour le port "+escale.getPort().getNom()));

        List<LigneFacturable> lignes = plan.resoudreLignes(escale);

        EnteteFacture entete = new EnteteFacture();
        entete.setDate(LocalDateTime.now());
        entete.setEscale(escale);
        entete.setStatut(StatutFacture.BROUILLON);

        List<DetailFacture> details = new ArrayList<>();
        double totalHT = 0;

        for (LigneFacturable ligne : lignes) {
            MontantDroit montant = droitCalculateurFactory.calculer(ligne.operation(), escale.getPort(), ligne.contexte());

            DetailFacture detail = new DetailFacture();
            detail.setTypeDroit(ligne.operation().name());
            detail.setDescription(ligne.operation().name().replace('_', ' ') + " — " + escale.getPort().getNom());
            detail.setMontantHT(montant.montant());
            detail.setUnite(montant.uniteQuantite());
            detail.setTarif(montant.tarif());
            detail.setQuantiteHeure(montant.quantite());
            detail.setTauxHoraire(montant.taux());
            detail.setEnteteFacture(entete);
            detail.setEntree(ligne.entreeSource());
            detail.setMouillage(ligne.mouillageSource());
            detail.setStationnement(ligne.stationnementSource());
            details.add(detail);

            totalHT += montant.montant();
        }

        entete.setTotalHT(totalHT);
        entete.setTaux_TVA(TAUX_TVA);
        double montantTVA = totalHT* TAUX_TVA;
        entete.setMontant_TVA(montantTVA);
        entete.setTotalTTC(totalHT+montantTVA);
        entete.setDetailFactures(details);

        EnteteFacture enregistree = enteteFactureRepository.save(entete);
        detailFactureRepository.saveAll(details);
        return enregistree;
    }

    @Transactional
    public EnteteFacture genererFacturePourEscale(Long idEscale) {
        Escale escale = escaleRepository.findById(idEscale)
                .orElseThrow(() -> new ResourceNotFoundException("Escale introuvable avec l'id : " + idEscale));
        return genererFacture(escale);
    }
}
