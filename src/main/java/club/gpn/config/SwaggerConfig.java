package club.gpn.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "auth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(title = "GPN Intelligence Cup",
                version = "v0.1.0",
                contact = @Contact(
                        name = "Chumakov Alexei",
                        email = "chumalesha@mail.ru",
                        url = "https://github.com/777DreamMaster"
                )
        )
)
public class SwaggerConfig {
}
