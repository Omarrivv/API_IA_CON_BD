package pe.edu.vallegrande.spring_webflux.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.spring_webflux.model.Gemini;
import pe.edu.vallegrande.spring_webflux.service.GeminiService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "*")
public class GeminiRest {
    private static final Logger log = LoggerFactory.getLogger(GeminiRest.class);
    private final GeminiService service;

    public GeminiRest(@Qualifier("geminiServiceImpl") GeminiService service) {
        this.service = service;
    }

    @PostMapping("/chat")
    public Mono<ResponseEntity<Gemini>> chat(@RequestBody ChatRequest request) {
        log.debug("Recibida petición de chat con contenido: {}", request.getContent());
        return service.processPrompt(request.getContent())
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error procesando la petición: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
                });
    }

    @GetMapping("/list")
    public Flux<Gemini> getAllActive() {
        return service.getAllActive();
    }

    @PutMapping("/delete/{id}")
    public Mono<ResponseEntity<Gemini>> deactivate(@PathVariable Long id) {
        return service.deactivate(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public static class ChatRequest {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
} 