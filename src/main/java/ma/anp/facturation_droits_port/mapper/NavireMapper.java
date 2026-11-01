package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.NavireRequest;
import ma.anp.facturation_droits_port.dto.NavireResponse;
import ma.anp.facturation_droits_port.entity.Navire;
import org.springframework.stereotype.Component;

@Component
public class NavireMapper {

    public Navire toEntity(NavireRequest request) {
        Navire navire = new Navire();
        navire.setNom(request.getNom());
        navire.setTjb(request.getTjb());
        navire.setTypeNavire(request.getTypeNavire());
        navire.setLongueur(request.getLongueur());
        navire.setLargeur(request.getLargeur());
        navire.setTirantEau(request.getTirantEau());
        navire.setArmateur(request.getArmateur());
        return navire;
    }

    public NavireResponse toResponse(Navire navire) {
        NavireResponse response = new NavireResponse();
        response.setIdNavire(navire.getIdNavire());
        response.setNom(navire.getNom());
        response.setTjb(navire.getTjb());
        response.setTypeNavire(navire.getTypeNavire());
        response.setLongueur(navire.getLongueur());
        response.setLargeur(navire.getLargeur());
        response.setTirantEau(navire.getTirantEau());
        response.setArmateur(navire.getArmateur());
        return response;
    }
}
