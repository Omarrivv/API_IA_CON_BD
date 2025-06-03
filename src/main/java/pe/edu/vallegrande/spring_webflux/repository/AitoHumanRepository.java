package pe.edu.vallegrande.spring_webflux.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.spring_webflux.model.AitoHuman;
import reactor.core.publisher.Mono;

@Repository
public interface AitoHumanRepository extends ReactiveCrudRepository<AitoHuman, Long> {
}

