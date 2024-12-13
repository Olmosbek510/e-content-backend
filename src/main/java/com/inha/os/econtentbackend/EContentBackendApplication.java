package com.inha.os.econtentbackend;

import com.google.gson.Gson;
import com.inha.os.econtentbackend.dto.request.RequestDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EContentBackendApplication {

    public static void main(String[] args) {
        Gson gson1 = new Gson();
        RequestDto requestDto = gson1.fromJson("""
                {
                                                  "entity": "STUDENT",
                                                  "action": "CREATE_STUDENT",
                                                  "data": "{
                                                               \\\\"firstName\\\\": \\\\"John\\\\",
                                                               \\\\"lastName\\\\": \\\\"Doe\\\\",
                                                               \\\\"email\\\\": \\\\"john.doe@example.com\\\\",
                                                               \\\\"password\\\\": \\\\"securePassword123\\\\",
                                                               \\\\"confirmPassword\\\\": \\\\"securePassword123\\\\",
                                                               \\\\"studentId\\\\": \\\\"S12345\\\\",
                                                               \\\\"university\\\\": \\\\"Inha University\\\\",
                                                               \\\\"birthDate\\\\": \\\\"2000-05-15\\\\",
                                                               \\\\"username\\\\": \\\\"john_doe\\\\",
                                                               \\\\"phoneNumber\\\\": \\\\"+1234567890\\\\",
                                                               \\\\"address\\\\": \\\\"123 Main Street, Example City\\\\"
                                                             }",
                                                  "token": null
                                                }
                }
                """, RequestDto.class);
        System.out.println("data parsed: " + requestDto);
        SpringApplication.run(EContentBackendApplication.class, args);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}