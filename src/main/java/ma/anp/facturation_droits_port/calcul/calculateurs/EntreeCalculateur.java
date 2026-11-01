package ma.anp.facturation_droits_port.calcul.calculateurs;

import ma.anp.facturation_droits_port.calcul.CalculTrancheUtil;
import ma.anp.facturation_droits_port.calcul.CalculateurDroit;
import ma.anp.facturation_droits_port.calcul.ContexteCalcul;
import ma.anp.facturation_droits_port.calcul.MontantDroit;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Tarif;
import ma.anp.facturation_droits_port.enums.TypeNavire;
import ma.anp.facturation_droits_port.enums.TypeOperation;
import ma.anp.facturation_droits_port.enums.TypeTerminal;
import ma.anp.facturation_droits_port.exception.TarifIntrouvableException;
import ma.anp.facturation_droits_port.repository.TarifRepository;
import org.springframework.stereotype.Component;

@Component
public class EntreeCalculateur implements CalculateurDroit {

    private final TarifRepository tarifRepository;

    public EntreeCalculateur(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    @Override
    public boolean supporte(TypeOperation operation, Port port) {
        return operation == TypeOperation.ENTREE_PORT && port.getTypeTerminal() == TypeTerminal.STANDARD;
    }

    @Override
    public MontantDroit calculer(ContexteCalcul ctx) {
        TypeNavire typeNavire = ctx.navire().isTransbordeur() ? TypeNavire.TRANSBORDEUR : TypeNavire.AUTRE;

        Tarif tarif = tarifRepository
                .findByTypeOperationAndTypeNavireAndPort_IdPort(TypeOperation.ENTREE_PORT, typeNavire, ctx.port().getIdPort())
                .or(()-> tarifRepository.findByTypeOperationAndTypeNavireAndPortIsNull(TypeOperation.ENTREE_PORT, typeNavire))
                .orElseThrow(()-> new TarifIntrouvableException(TypeOperation.ENTREE_PORT, typeNavire, ctx.port()));

        double montant = CalculTrancheUtil.cumulProgressif(tarif.getTarifTranches(), ctx.tjb());
        return new MontantDroit(montant, "EUR", tarif, 1.0, "forfait", montant);
    }
}
