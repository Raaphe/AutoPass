package backend.autopass.config;

import backend.autopass.payload.viewmodels.ErrorResponse;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPISecurityConfig - 2024-03-30
 * Raph
 * The OpenAPI 3.0 configurations.
 * AutoPass
 */
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Raphael .Paquin",
                        email = "raphaelpaquin19@gmail.com",
                        url = "https://www.linkedin.com/in/raphee/"
                ),
                title = "OpenApi specification - AutoPass",
                version = "1.0"
        ),
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "Bearer"
                )
        }
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "Bearer",
        description = "JWT auth description",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPISecurityConfig {

    @Value("${application.ip}")
    private String serverIp;

    @Value("${server.port}")
    private String backendPort;

    @Bean
    public OpenApiCustomizer schemaCustomizer() {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class));
        return openApi -> openApi
                .schema(resolvedSchema.schema.getName(), resolvedSchema.schema);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch(SecurityConfig.WHITE_LIST_URL)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .addServersItem(new Server().url("http://" + serverIp + ":" + backendPort))
                .components(new Components()
                        .addSecuritySchemes("Bearer", createAPIKeyScheme()))
                .info(new Info()
                        .title("AutoPass Restful API")
                        .description("This is the documentation of the AutoPass application's Restful API. It is important to note that our system implements JWTs for session management.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .email("raphaelpaquin19@gmail.com")
                                .name("Raphael .Paquin")
                        )
                );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                ;
    }
}
