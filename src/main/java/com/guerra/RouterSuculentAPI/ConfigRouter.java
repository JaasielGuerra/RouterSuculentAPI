package com.guerra.RouterSuculentAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigRouter {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
