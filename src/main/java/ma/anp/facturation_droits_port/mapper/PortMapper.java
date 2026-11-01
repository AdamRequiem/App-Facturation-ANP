package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.PortRequest;
import ma.anp.facturation_droits_port.dto.PortResponse;
import ma.anp.facturation_droits_port.entity.Port;
import org.springframework.stereotype.Component;

@Component
public class PortMapper {

    public Port toEntity(PortRequest request) {
        Port port = new Port();
        port.setNom(request.getNom());
        port.setTypeTerminal(request.getTypeTerminal());
        return port;
    }

    public PortResponse toResponse(Port port) {
        PortResponse response = new PortResponse();
        response.setIdPort(port.getIdPort());
        response.setNom(port.getNom());
        response.setTypeTerminal(port.getTypeTerminal());
        return response;
    }
}
