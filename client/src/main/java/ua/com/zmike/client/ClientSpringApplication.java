package ua.com.zmike.client;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClientSpringApplication {

	public static void main(String[] args) {
		Application.launch(JavaFxApplication.class, args);
	}
}


