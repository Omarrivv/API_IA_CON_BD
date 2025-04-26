package pe.edu.vallegrande.spring_webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${rapidapiChatGPT.url}")
    private String RAPIDAPI_URL;

    @Value("${rapidapiChatGPT.host}")
    private String RAPIDAPI_HOST;

    @Value("${rapidapiChatGPT.apikey}")
    private String RAPIDAPI_APIKEY;

    @Bean
    @Primary
    public WebClient aitoHumanWebClient() {
        return WebClient.builder()
                .baseUrl(RAPIDAPI_URL)
                .defaultHeader("x-rapidapi-host", RAPIDAPI_HOST)
                .defaultHeader("x-rapidapi-key", RAPIDAPI_APIKEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    //Client Llama
    @Value("${rapidapiLlama.url}")
    private String RAPIDAPILLAMA_URL;

    @Value("${rapidapiLlama.host}")
    private String RAPIDAPILLAMA_HOST;

    @Value("${rapidapiLlama.apikey}")
    private String RAPIDAPILLAMA_APIKEY;
    
    @Bean
    public WebClient metaLlamaClient() {
        return WebClient.builder()
                .baseUrl(RAPIDAPILLAMA_URL)
                .defaultHeader("x-rapidapi-host", RAPIDAPILLAMA_HOST)
                .defaultHeader("x-rapidapi-key", RAPIDAPILLAMA_APIKEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}