package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.ToucheeRequest;
import ma.anp.facturation_droits_port.dto.ToucheeResponse;
import ma.anp.facturation_droits_port.entity.Navire;
import ma.anp.facturation_droits_port.entity.Port;
import ma.anp.facturation_droits_port.entity.Touchee;
import org.springframework.stereotype.Component;

@Component
public class ToucheeMapper {

    public Touchee toEntity(ToucheeRequest request) {
        Touchee touchee = new Touchee();
        touchee.setAnnee(request.getAnnee());
        touchee.setNombreTouchees(request.getNombreTouchees());

        if (request.getIdNavire() != null) {
            Navire navire = new Navire();
            navire.setIdNavire(request.getIdNavire());
            touchee.setNavire(navire);
        }

        if (request.getIdPort() != null) {
            Port port = new Port();
            port.setIdPort(request.getIdPort());
            touchee.setPort(port);
        }

        return touchee;
    }

    public ToucheeResponse toResponse(Touchee touchee) {
        ToucheeResponse response = new ToucheeResponse();
        response.setIdTouchees(touchee.getIdTouchees());
        response.setAnnee(touchee.getAnnee());
        response.setNombreTouchees(touchee.getNombreTouchees());

        if (touchee.getNavire() != null) {
            response.setIdNavire(touchee.getNavire().getIdNavire());
            response.setNomNavire(touchee.getNavire().getNom());
        }

        if (touchee.getPort() != null) {
            response.setIdPort(touchee.getPort().getIdPort());
            response.setNomPort(touchee.getPort().getNom());
        }

        return response;
    }
}
