package pe.edu.vallegrande.spring_webflux.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.spring_webflux.model.MetaLlama;
import pe.edu.vallegrande.spring_webflux.service.MetaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai/metallama")
public class LlamaAIRest {

    @Autowired
    private MetaService iaService;

    @GetMapping
    public Flux<MetaLlama> findAll() {
        return iaService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MetaLlama>> findById(@PathVariable String id) {
        return iaService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<MetaLlama> createAitoHuman(@RequestBody MetaLlama metaLlama) {
        return iaService.save(metaLlama);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MetaLlama>> update(@PathVariable String id, @RequestBody MetaLlama metaLlama) {
        return iaService.update(id, metaLlama)
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
