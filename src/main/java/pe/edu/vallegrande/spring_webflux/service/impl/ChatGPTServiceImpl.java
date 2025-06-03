package pe.edu.vallegrande.spring_webflux.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.spring_webflux.model.ChatGPT;
import pe.edu.vallegrande.spring_webflux.repository.ChatGPTRepository;
import pe.edu.vallegrande.spring_webflux.service.ChatGPTService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTServiceImpl implements ChatGPTService {

    private static final Logger log = LoggerFactory.getLogger(ChatGPTServiceImpl.class);
    private final ChatGPTRepository repository;
    private final WebClient webClient;

    public ChatGPTServiceImpl(ChatGPTRepository repository,
                          @Qualifier(value = "chatGPTClient") WebClient webClient) {
        this.repository = repository;
        this.webClient = webClient;
    }

    @Override
    public Flux<ChatGPT> findAll() {
        return repository.findAllByStatus("A");
    }

    @Override
    public Mono<ChatGPT> findById(String id) {
        return repository.findByIdAndStatus(Long.valueOf(String.valueOf(Long.parseLong(id))), "A");
    }

    @Override
    public Mono<ChatGPT> save(ChatGPT chatGPT) {
        chatGPT.setStatus("A");
        log.info("Registrando pregunta: {}", chatGPT.getContent());
        return consultarIA(chatGPT);
    }

    @Override
    public Mono<ChatGPT> update(String id, ChatGPT chatGPT) {
        return repository.findByIdAndStatus(Long.valueOf(String.valueOf(Long.parseLong(id))), "A")
                .flatMap(existing -> {
                    existing.setContent(chatGPT.getContent());
                    existing.setStatus("A");
                    return consultarIA(existing);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(Long.valueOf(String.valueOf(Long.parseLong(id))))
                .flatMap(chatGPT -> {
                    chatGPT.setStatus("I");
                    return repository.save(chatGPT);
                })
                .then();
    }

    @Override
    public Mono<ChatGPT> restore(String id) {
        return repository.findById(Long.valueOf(String.valueOf(Long.parseLong(id))))
                .filter(chatGPT -> "I".equals(chatGPT.getStatus())) // Solo restaura si está inactivo
                .flatMap(chatGPT -> {
                    chatGPT.setStatus("A");
                    return repository.save(chatGPT);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Registro no encontrado o ya está activo")));
    }

    @Override
    public Flux<ChatGPT> findAllInactive() {
        return repository.findAllByStatus("I");
    }

    public Mono<ChatGPT> consultarIA(ChatGPT chatGPT) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", List.of(
            Map.of("role", "user", "content", chatGPT.getContent())
        ));
        requestBody.put("model", "gpt-4o-mini");

        return webClient.post()
                .uri("/chat")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(json);
                        
                        String respuesta;
                        if (root.has("Error")) {
                            respuesta = root.get("Error").asText();
                        } else {
                            JsonNode choices = root.path("choices");
                            if (!choices.isArray() || choices.size() == 0) {
                                respuesta = "No se encontraron opciones en la respuesta";
                            } else {
                                JsonNode messageNode = choices.get(0).path("message");
                                if (!messageNode.has("content")) {
                                    respuesta = "No se encontró contenido en la respuesta";
                                } else {
                                    respuesta = messageNode.get("content").asText();
                                }
                            }
                        }
                        
                        // Mantener el mismo ID si existe
                        if (chatGPT.getId() != null) {
                            chatGPT.setAnswer(respuesta);
                            return chatGPT;
                        } else {
                            ChatGPT response = new ChatGPT();
                            response.setContent(chatGPT.getContent());
                            response.setAnswer(respuesta);
                            response.setStatus(chatGPT.getStatus());
                            return response;
                        }
                    } catch (Exception e) {
                        log.error("Error al procesar JSON: {}", e.getMessage());
                        if (chatGPT.getId() != null) {
                            chatGPT.setAnswer("Error al procesar la respuesta: " + e.getMessage());
                            return chatGPT;
                        } else {
                            ChatGPT errorResponse = new ChatGPT();
                            errorResponse.setContent(chatGPT.getContent());
                            errorResponse.setAnswer("Error al procesar la respuesta: " + e.getMessage());
                            errorResponse.setStatus(chatGPT.getStatus());
                            return errorResponse;
                        }
                    }
                })
                .flatMap(repository::save)
                .doOnError(e -> log.error("Error al guardar en la base de datos: {}", e.getMessage()))
                .onErrorResume(e -> {
                    ChatGPT errorResponse = new ChatGPT();
                    errorResponse.setContent(chatGPT.getContent());
                    errorResponse.setAnswer("Error al guardar en la base de datos");
                    errorResponse.setStatus(chatGPT.getStatus());
                    return Mono.just(errorResponse);
                });
    }
} 