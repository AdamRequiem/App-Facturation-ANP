package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.EnteteFactureResponse;
import ma.anp.facturation_droits_port.dto.EscaleRequest;
import ma.anp.facturation_droits_port.dto.EscaleResponse;
import ma.anp.facturation_droits_port.entity.EnteteFacture;
import ma.anp.facturation_droits_port.facturation.FactureOrchestrationService;
import ma.anp.facturation_droits_port.mapper.EnteteFactureMapper;
import ma.anp.facturation_droits_port.service.EscaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escales")
public class EscaleController {

    private final EscaleService escaleService;
    private final FactureOrchestrationService factureOrchestrationService;
    private final EnteteFactureMapper enteteFactureMapper;

    public EscaleController(EscaleService escaleService, FactureOrchestrationService factureOrchestrationService, EnteteFactureMapper enteteFactureMapper) {
        this.escaleService = escaleService;
        this.factureOrchestrationService = factureOrchestrationService;
        this.enteteFactureMapper = enteteFactureMapper;
    }

    @PostMapping("/{id}/facture")
    public ResponseEntity<EnteteFactureResponse> genererFacture(@PathVariable Long id) {
        EnteteFacture facture = factureOrchestrationService.genererFacturePourEscale(id);
        return ResponseEntity.status(201).body(enteteFactureMapper.toResponse(facture));
    }

    @GetMapping
    public List<EscaleResponse> getAllEscales() {
        return escaleService.getAllEscales();
    }

    @GetMapping("/{id}")
    public EscaleResponse getEscaleById(@PathVariable Long id) {
        return escaleService.getEscaleById(id);
    }

    @PostMapping
    public ResponseEntity<EscaleResponse> createEscale(@RequestBody EscaleRequest request) {
        EscaleResponse response = escaleService.createEscale(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public EscaleResponse createEscale(@PathVariable Long id, @RequestBody EscaleRequest request) {
        return escaleService.updateEscale(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEscale(@PathVariable Long id) {
        escaleService.deleteEscale(id);

        return ResponseEntity.noContent().build();
    }
}
