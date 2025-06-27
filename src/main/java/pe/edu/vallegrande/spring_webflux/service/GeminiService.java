package pe.edu.vallegrande.spring_webflux.service;

import pe.edu.vallegrande.spring_webflux.model.Gemini;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GeminiService {
    Mono<Gemini> processPrompt(String content);
    Flux<Gemini> getAllActive();
    Mono<Gemini> deactivate(Long id);
} 