package pe.edu.vallegrande.spring_webflux.service;


import pe.edu.vallegrande.spring_webflux.model.MetaLlama;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MetaService {
    Flux<MetaLlama> findAll();
    Mono<MetaLlama> findById(String id);
    Mono<MetaLlama> save(MetaLlama human);
    Mono<MetaLlama> update(String id, MetaLlama metaLlama);
    Mono<Void> delete(String id);
}
