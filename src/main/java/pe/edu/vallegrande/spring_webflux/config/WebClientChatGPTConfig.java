package pe.edu.vallegrande.spring_webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientChatGPTConfig {
    
    @Value("${rapidapiChatGPT.url}")
    private String RAPIDAPI_URL;

    @Value("${rapidapiChatGPT.host}")
    private String RAPIDAPI_HOST;

    @Value("${rapidapiChatGPT.apikey}")
    private String RAPIDAPI_APIKEY;

    @Bean(name = "chatGPTClient")
    public WebClient chatGPTClient() {
        return WebClient.builder()
                .baseUrl(RAPIDAPI_URL)
                .defaultHeader("x-rapidapi-host", RAPIDAPI_HOST)
                .defaultHeader("x-rapidapi-key", RAPIDAPI_APIKEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
} 