package pe.edu.vallegrande.spring_webflux.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.spring_webflux.model.AitoHuman;
import pe.edu.vallegrande.spring_webflux.service.IAService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class ApiAIRest {
    /*hola*/
    private final IAService iaService;

    public ApiAIRest(IAService iaService) {
        this.iaService = iaService;
    }

    @GetMapping
    public Flux<AitoHuman> findAll() {
        return iaService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AitoHuman>> findById(@PathVariable String id) {
        return iaService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<AitoHuman> createAitoHuman(@RequestBody AitoHuman aitoHuman) {
        return iaService.save(aitoHuman);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AitoHuman>> update(@PathVariable String id, @RequestBody AitoHuman human) {
        return iaService.update(id, human)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return iaService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
