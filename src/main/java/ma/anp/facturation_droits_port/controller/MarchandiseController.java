package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.MarchandiseRequest;
import ma.anp.facturation_droits_port.dto.MarchandiseResponse;
import ma.anp.facturation_droits_port.service.MarchandiseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marchandises")
public class MarchandiseController {

    private final MarchandiseService marchandiseService;

    public MarchandiseController(MarchandiseService marchandiseService) {
        this.marchandiseService = marchandiseService;
    }

    @GetMapping
    public List<MarchandiseResponse> getAllMarchandise() {
        return marchandiseService.getAllMarchandises();
    }

    @GetMapping("/{id}")
    public MarchandiseResponse getMarchandiseById(@PathVariable Long id) {
        return marchandiseService.getMarchandiseById(id);
    }

    @PostMapping
    public ResponseEntity<MarchandiseResponse> createMarchandise(@RequestBody MarchandiseRequest request) {
        MarchandiseResponse response = marchandiseService.createMarchandise(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public MarchandiseResponse updateMarchandise(@PathVariable Long id, @RequestBody MarchandiseRequest request) {
        return marchandiseService.updateMarchandise(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarchandise(@PathVariable Long id) {
        marchandiseService.deleteMarchandise(id);
        return ResponseEntity.noContent().build();
    }
}
