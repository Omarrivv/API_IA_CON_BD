package pe.edu.vallegrande.spring_webflux.service;

import pe.edu.vallegrande.spring_webflux.model.AitoHuman;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAService {
    Flux<AitoHuman> findAll();
    Mono<AitoHuman> findById(String id);
    Mono<AitoHuman> save(AitoHuman human);
    Mono<AitoHuman> update(String id, AitoHuman human);
    Mono<Void> delete(String id);
}
