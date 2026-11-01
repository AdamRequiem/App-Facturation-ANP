package ma.anp.facturation_droits_port.mapper;

import ma.anp.facturation_droits_port.dto.MarchandiseRequest;
import ma.anp.facturation_droits_port.dto.MarchandiseResponse;
import ma.anp.facturation_droits_port.entity.Marchandise;
import org.springframework.stereotype.Component;

@Component
public class MarchandiseMapper {

    public Marchandise toEntity(MarchandiseRequest request) {
        Marchandise marchandise = new Marchandise();
        marchandise.setNom(request.getNom());
        return marchandise;
    }

    public MarchandiseResponse toResponse(Marchandise marchandise) {
        MarchandiseResponse response = new MarchandiseResponse();
        response.setIdMarchandise(marchandise.getIdMarchandise());
        response.setNom(marchandise.getNom());
        return response;
    }
}
