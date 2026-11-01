package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.DetailFactureRequest;
import ma.anp.facturation_droits_port.dto.DetailFactureResponse;
import ma.anp.facturation_droits_port.service.DetailFactureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detail-factures")
public class DetailFactureController {

    private final DetailFactureService detailFactureService;

    public DetailFactureController(DetailFactureService detailFactureService) {
        this.detailFactureService = detailFactureService;
    }

    @GetMapping
    public List<DetailFactureResponse> getAllDetailFactures() {
        return detailFactureService.getAllDetailFactures();
    }

    @GetMapping("/{id}")
    public DetailFactureResponse getDetailFactureById(@PathVariable Long id) {
        return detailFactureService.getDetailFactureById(id);
    }

    @PostMapping
    public ResponseEntity<DetailFactureResponse> createDetailFacture(@RequestBody DetailFactureRequest request) {
        DetailFactureResponse response = detailFactureService.createDetailFacture(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public DetailFactureResponse updateDetailFacture(@PathVariable Long id, @RequestBody DetailFactureRequest request) {
        return detailFactureService.updateDetailFacture(id, request);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDetailFacture(@PathVariable Long id) {
        detailFactureService.deleteDetailFacture(id);

        return ResponseEntity.noContent().build();
    }
}
