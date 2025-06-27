package pe.edu.vallegrande.spring_webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.edu.vallegrande.spring_webflux.model.Gemini;
import reactor.core.publisher.Flux;

public interface GeminiRepository extends ReactiveCrudRepository<Gemini, Long> {
    Flux<Gemini> findByStatus(String status);
} 