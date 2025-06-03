package pe.edu.vallegrande.spring_webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.spring_webflux.model.ChatGPT;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatGPTRepository extends ReactiveCrudRepository<ChatGPT, Long> {
    Flux<ChatGPT> findAllByStatus(String status);
    Mono<ChatGPT> findByIdAndStatus(Long id, String status);
} 