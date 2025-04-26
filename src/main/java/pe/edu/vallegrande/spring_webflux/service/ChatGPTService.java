package pe.edu.vallegrande.spring_webflux.service;

import pe.edu.vallegrande.spring_webflux.model.ChatGPT;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatGPTService {
    Flux<ChatGPT> findAll();
    Mono<ChatGPT> findById(String id);
    Mono<ChatGPT> save(ChatGPT chatGPT);
    Mono<ChatGPT> update(String id, ChatGPT chatGPT);
    Mono<Void> delete(String id);
} 