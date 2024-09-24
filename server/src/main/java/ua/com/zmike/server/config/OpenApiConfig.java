package ua.com.zmike.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI (Swagger) documentation in the project.
 * This class defines the necessary beans to customize the OpenAPI documentation and
 * group API endpoints.
 */
@EnableCaching
@Configuration
public class OpenApiConfig {

	/**
	 * Creates a custom OpenAPI bean to configure the API metadata like title, description, version, and license.
	 *
	 * @return an instance of {@link OpenAPI} with the customized information for the API.
	 */
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("news-service-api")
						.description("Simple API to manage news")
						.version("0.0.1-SNAPSHOT")
						.license(new License().name("Zaiets Mike")));
	}

	/**
	 * Defines a group of API endpoints that match the given paths for news-related endpoints.
	 * This grouping will be reflected in the Swagger UI, under the "news" group.
	 *
	 * @return an instance of {@link GroupedOpenApi} that matches the "/news/**" paths.
	 */
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("news")
				.pathsToMatch("/news/**")
				.build();
	}
}

