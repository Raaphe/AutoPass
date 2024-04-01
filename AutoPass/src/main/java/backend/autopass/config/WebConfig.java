package backend.autopass.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig - 2024-03-30
 * Raph
 * Another web configurer class.
 * AutoPass
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${application.ip}")
    private String localHostIP;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins()
                .allowedOrigins(
                        "http://" + localHostIP + ":3005",
                        "http://" + localHostIP + ":3000",
                        "http://" + localHostIP + ":9090",
                        "http://localhost:3005",
                        "http://localhost:3000",
                        "http://localhost:9090"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}


