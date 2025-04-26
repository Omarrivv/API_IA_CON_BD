package pe.edu.vallegrande.spring_webflux.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.spring_webflux.model.ChatGPT;

@Repository
public interface ChatGPTRepository extends ReactiveCrudRepository<ChatGPT, Long> {
} 