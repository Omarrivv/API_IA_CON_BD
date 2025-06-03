package pe.edu.vallegrande.spring_webflux.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.spring_webflux.model.AitoHuman;
import pe.edu.vallegrande.spring_webflux.repository.AitoHumanRepository;
import pe.edu.vallegrande.spring_webflux.service.IAService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IAServiceImpl implements IAService {

    private static final Logger log = LoggerFactory.getLogger(IAServiceImpl.class);
    private final AitoHumanRepository repository;
    private final WebClient webClient;

    public IAServiceImpl(AitoHumanRepository repository,WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    @Override
    public Flux<AitoHuman> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<AitoHuman> findById(String id) {
        return repository.findById(Long.parseLong(id));
    }

    @Override
    public Mono<AitoHuman> save(AitoHuman human) {
        log.info("Registrando pregunta: {}", human.getText());
        return consultarIA(human);
    }

    @Override
    public Mono<AitoHuman> update(String id, AitoHuman human) {
        return repository.findById(Long.parseLong(id))
                .flatMap(existing -> {
                    existing.setText(human.getText());
                    return consultarIA(existing);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(Long.parseLong(id));
    }

    public record ChatRequest(String text) {}

    public Mono<AitoHuman> consultarIA(AitoHuman human) {
        ChatRequest text = new ChatRequest(human.getText());
        log.info("JSON enviado: {}", text);  // Log del JSON antes de enviarlo

        return webClient.post()
                .uri("/aitohuman")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(text)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> log.info("Respuesta cruda: {}", json))
                .flatMap(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(json);

                        JsonNode resultNode = root.path("result");
                        String respuesta = resultNode.isArray() && resultNode.size() > 0
                                ? resultNode.get(0).asText()
                                : "Sin respuesta";

                        human.setResult(respuesta);
                    } catch (Exception e) {
                        log.error("Error al procesar JSON", e);
                        human.setResult("Error de formato en respuesta");
                    }

                    return repository.save(human);
                });
    }

}
