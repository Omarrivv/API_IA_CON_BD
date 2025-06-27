package pe.edu.vallegrande.spring_webflux.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.spring_webflux.model.Gemini;
import pe.edu.vallegrande.spring_webflux.repository.GeminiRepository;
import pe.edu.vallegrande.spring_webflux.service.GeminiService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GeminiServiceImpl implements GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiServiceImpl.class);
    private final GeminiRepository repository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public GeminiServiceImpl(
            GeminiRepository repository,
            @Qualifier("geminiWebClient") WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
        log.info("GeminiServiceImpl initialized");
    }

    @Override
    public Mono<Gemini> processPrompt(String content) {
        log.debug("Processing prompt: {}", content);

        ObjectNode requestBody = objectMapper.createObjectNode();
        ObjectNode contentObj = objectMapper.createObjectNode();
        ObjectNode partObj = objectMapper.createObjectNode();
        
        partObj.put("text", content);
        
        contentObj.putArray("parts").add(partObj);
        contentObj.put("role", "user");
        
        requestBody.putArray("contents").add(contentObj);
        requestBody.put("model", "gemini-2.0-flash");

        log.debug("Request body: {}", requestBody);

        return webClient.post()
                .uri("/models/gemini-2.0-flash:generateContent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(response -> log.debug("Received response: {}", response))
                .map(response -> {
                    try {
                        String answer = response
                                .path("candidates")
                                .path(0)
                                .path("content")
                                .path("parts")
                                .path(0)
                                .path("text")
                                .asText();

                        if (answer == null || answer.isEmpty()) {
                            log.error("Empty response from Gemini API");
                            throw new RuntimeException("Respuesta vacía de la API de Gemini");
                        }

                        log.debug("Extracted answer: {}", answer);
                        return new Gemini(null, content, answer, "A");
                    } catch (Exception e) {
                        log.error("Error processing Gemini response: {}", e.getMessage());
                        throw new RuntimeException("Error procesando la respuesta de Gemini: " + e.getMessage());
                    }
                })
                .flatMap(repository::save)
                .doOnError(error -> log.error("Error in processPrompt: {}", error.getMessage()));
    }

    @Override
    public Flux<Gemini> getAllActive() {
        return repository.findByStatus("A");
    }

    @Override
    public Mono<Gemini> deactivate(Long id) {
        return repository.findById(id)
                .flatMap(gemini -> {
                    gemini.setStatus("I");
                    return repository.save(gemini);
                });
    }
} 