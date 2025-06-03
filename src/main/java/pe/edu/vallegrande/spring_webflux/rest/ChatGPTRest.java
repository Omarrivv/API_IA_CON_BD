package pe.edu.vallegrande.spring_webflux.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.spring_webflux.model.ChatGPT;
import pe.edu.vallegrande.spring_webflux.service.ChatGPTService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai/chatgpt")
public class ChatGPTRest {

    @Autowired
    private ChatGPTService chatGPTService;

    @GetMapping
    public Flux<ChatGPT> findAll() {
        return chatGPTService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ChatGPT>> findById(@PathVariable String id) {
        return chatGPTService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ChatGPT> create(@RequestBody ChatGPT chatGPT) {
        return chatGPTService.save(chatGPT);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ChatGPT>> update(@PathVariable String id, @RequestBody ChatGPT chatGPT) {
        return chatGPTService.update(id, chatGPT)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return chatGPTService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/inactive")
    public Flux<ChatGPT> findAllInactive() {
        return chatGPTService.findAllInactive();
    }

    @PutMapping("/restore/{id}")
    public Mono<ResponseEntity<ChatGPT>> restore(@PathVariable String id) {
        return chatGPTService.restore(id)
                .map(restored -> new ResponseEntity<>(restored, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
} 