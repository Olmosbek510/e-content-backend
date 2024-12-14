package com.inha.os.econtentbackend.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class Beans {
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls() // Serialize null values (optional, if you want to include null values)
                .create();
    }

    @Bean
    public Base64.Encoder encoder() {
        return Base64.getEncoder();
    }

    @Bean
    public Base64.Decoder decoder() {
        return Base64.getDecoder();
    }
}
