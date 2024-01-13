package backend.autopass.security.config;

import backend.autopass.security.handlers.ErrorResponse;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
        },
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenAPISecurityConfig {

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
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", createAPIKeyScheme()))
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
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                ;
    }

    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> {
                    if (operation.getParameters() != null) {
                        operation.getParameters().add(new HeaderParameter().name("X-Custom-Header").description("Custom header").required(false).schema(new StringSchema()));
                    }
                });
    }
}
