package ua.com.zmike.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	/**
	 * Creates a {@link RestTemplate} bean that allows for executing HTTP
	 * requests and interacting with RESTful web services.
	 *
	 * @return a new instance of {@link RestTemplate}.
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
