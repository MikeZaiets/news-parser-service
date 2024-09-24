package ua.com.zmike.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class JavaFxApplication extends Application {

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void init() {
		var args = getParameters().getRaw().toArray(new String[0]);
		var builder = new SpringApplicationBuilder();
		builder.headless(false);
		this.applicationContext = builder.sources(ClientSpringApplication.class).run(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		var loader = new FXMLLoader();

		if (applicationContext.isRunning()) {
			loader.setControllerFactory(applicationContext::getBean);
		} else {
			stop();
		}

		var resource = getClass().getResource("/scene/news-view.fxml");
		log.info("FXML resource: " + resource);

		loader.setLocation(resource);
		Parent load = loader.load();

		var scene = new Scene(load, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("News Viewer");
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		applicationContext.close();
		Platform.exit();
	}
}
