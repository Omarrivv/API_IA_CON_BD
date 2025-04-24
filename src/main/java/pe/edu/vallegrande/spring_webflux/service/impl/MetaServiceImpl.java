package pe.edu.vallegrande.spring_webflux.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.spring_webflux.config.WebClientLlamaConfig;
import pe.edu.vallegrande.spring_webflux.model.AitoHuman;
import pe.edu.vallegrande.spring_webflux.model.MetaLlama;
import pe.edu.vallegrande.spring_webflux.repository.MetaLlamaRepository;
import pe.edu.vallegrande.spring_webflux.service.MetaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
public class MetaServiceImpl implements MetaService {

    private static final Logger log = LoggerFactory.getLogger(IAServiceImpl.class);
    private final MetaLlamaRepository repository;
    private final WebClient webClient;

    public MetaServiceImpl(MetaLlamaRepository repository,
                           @Qualifier(value = "metaLlamaClient") WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    @Override
    public Flux<MetaLlama> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<MetaLlama> findById(String id) {
        return repository.findById(Long.valueOf(String.valueOf(Long.parseLong(id))));
    }

    @Override
    public Mono<MetaLlama> save(MetaLlama llama) {
        log.info("Registrando pregunta: {}", llama.getContent());
        return consultarIA(llama);
    }

    @Override
    public Mono<MetaLlama> update(String id, MetaLlama metaLlama) {
        return repository.findById(Long.valueOf(String.valueOf(Long.parseLong(id))))
                .flatMap(existing -> {
                    existing.setContent(metaLlama.getContent());
                    return consultarIA(existing);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(Long.valueOf(String.valueOf(Long.parseLong(id))));
    }

    public record ChatRequest(List<Message> messages) {
        public record Message(String role, String content) {}
    }

    public Mono<MetaLlama> consultarIA(MetaLlama metaLlama) {

        // Crear el objeto Message y agregarlo a la lista
        List<ChatRequest.Message> messages = List.of(new ChatRequest.Message("user", metaLlama.getContent()));

        // Crear el objeto ChatRequest
        ChatRequest chatRequest = new ChatRequest(messages);


        return webClient.post()
                .uri("/chat")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(chatRequest)  // Enviar el objeto serializado
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(json -> log.info("Respuesta cruda: {}", json))
                .flatMap(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(json);

                        JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
                        String respuesta = contentNode.isTextual() ? contentNode.asText() : "Sin respuesta";
                        metaLlama.setAnswer(respuesta);

                    } catch (Exception e) {
                        log.error("Error al procesar JSON", e);
                        metaLlama.setAnswer("Error de formato en respuesta");
                    }

                    return repository.save(metaLlama);
                });
    }
}
