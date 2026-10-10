package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition //http://localhost:8080/swagger-ui/index.html
(
		info = @Info(
				title = "Тестирование API",
				version = "3000",
				description = "\"Тут могла быть ваша реклама\""
		)
)
public class SchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplication.class, args);
	}

}
