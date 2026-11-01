package ma.anp.facturation_droits_port.controller;

import ma.anp.facturation_droits_port.dto.StationnementRequest;
import ma.anp.facturation_droits_port.dto.StationnementResponse;
import ma.anp.facturation_droits_port.service.StationnementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stationnements")
public class StationnementController {

    private final StationnementService stationnementService;

    public StationnementController(StationnementService stationnementService) {
        this.stationnementService = stationnementService;
    }

    @GetMapping
    public List<StationnementResponse> getAllStationnements() {
        return stationnementService.getAllStationnements();
    }

    @GetMapping("/{id}")
    public StationnementResponse getStationnementById(@PathVariable Long id) {
        return stationnementService.getStationnementById(id);
    }

    @PostMapping
    public ResponseEntity<StationnementResponse> createStationnement(@RequestBody StationnementRequest request) {
        StationnementResponse response = stationnementService.createStationnement(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{id}")
    public StationnementResponse updateStationnement(@PathVariable Long id, @RequestBody StationnementRequest request) {
        return stationnementService.updateStationnement(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStationnement(@PathVariable Long id) {
        stationnementService.deleteStationnement(id);
        return ResponseEntity.noContent().build();
    }
}
