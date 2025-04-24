package pe.edu.vallegrande.spring_webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientLlamaConfig {
    //Client Llama
    @Value("${rapidapiLlama.url}")
    private String RAPIDAPILLAMA_URL;

    @Value("${rapidapiLlama.host}")
    private String RAPIDAPILLAMA_HOST;

    @Value("${rapidapiLlama.apikey}")
    private String RAPIDAPILLAMA_APIKEY;

    @Bean
    public WebClient LlamaClient() {
        return WebClient.builder()
                .baseUrl(RAPIDAPILLAMA_URL)
                .defaultHeader("x-rapidapi-host", RAPIDAPILLAMA_HOST)
                .defaultHeader("x-rapidapi-key", RAPIDAPILLAMA_APIKEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
