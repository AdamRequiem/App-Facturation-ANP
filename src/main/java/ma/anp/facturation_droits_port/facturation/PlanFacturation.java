package ma.anp.facturation_droits_port.facturation;

import ma.anp.facturation_droits_port.entity.Escale;
import ma.anp.facturation_droits_port.entity.Port;

import java.util.List;

public interface PlanFacturation {

    boolean supporte(Port port);
    List<LigneFacturable> resoudreLignes(Escale escale);
}
