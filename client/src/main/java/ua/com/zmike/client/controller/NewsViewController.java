package ua.com.zmike.client.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ua.com.zmike.client.dto.NewsDTO;
import ua.com.zmike.client.service.NewsApiService;

/**
 * NewsViewController handles the interactions between the JavaFX UI
 * and the news data retrieved from the {@link NewsApiService}.
 * It manages loading, displaying, creating, updating, and deleting news.
 */
@Controller
@RequiredArgsConstructor
public class NewsViewController {

	private final NewsApiService newsApiService;

	@FXML
	private Label headlineLabel;
	@FXML
	private Label descriptionLabel;
	@FXML
	private Label publicationTimeLabel;

	private List<NewsDTO> newsList;
	private int currentIndex = 0;

	/**
	 * Initializes the controller, loads the news data and displays the first news entry.
	 */
	@FXML
	public void initialize() {
		loadAllNews();
		showCurrentNews();
	}

	/**
	 * Loads the news from the {@link NewsApiService} within the past 24 hours.
	 */
	private void loadAllNews() {
		LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).minusDays(1);
		LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59);
		loadNewsByDatePeriod(start, end);
	}

	/**
	 * Displays the current news based on the current index.
	 * If the list is empty or the index is out of range, the display will be cleared.
	 */
	private void showCurrentNews() {
		if (!Objects.isNull(newsList) && !newsList.isEmpty() && currentIndex >= 0 && currentIndex < newsList.size()) {
			var currentNews = newsList.get(currentIndex);
			headlineLabel.setText(currentNews.getHeadline());
			descriptionLabel.setText(currentNews.getDescription());
			publicationTimeLabel.setText(currentNews.getPublicationTime().toString());
		} else {
			clearNewsDisplay();
		}
	}

	/**
	 * Clears the news display fields when no news is available.
	 */
	private void clearNewsDisplay() {
		headlineLabel.setText("No news available");
		descriptionLabel.setText("No description ");
		publicationTimeLabel.setText("No time");
	}

	/**
	 * Displays the next news entry in the list. If at the last news, shows an alert.
	 */
	@FXML
	public void onNextNews() {
		if (currentIndex < newsList.size() - 1) {
			currentIndex++;
			showCurrentNews();
		} else {
			showInfoAlert("You are already viewing the latest news.");
		}
	}

	/**
	 * Displays the previous news entry in the list. If at the first news, shows an alert.
	 */
	@FXML
	public void onPreviousNews() {
		if (currentIndex > 0) {
			currentIndex--;
			showCurrentNews();
		} else {
			showInfoAlert("You are already viewing the first news.");
		}
	}

	/**
	 * Loads and displays the news from the night and morning (00:00 to 11:59).
	 */
	@FXML
	public void onMorningNews() {
		loadNewsByTimePeriod(0, 0, 11, 59);
	}

	/**
	 * Loads and displays the news from the afternoon (12:00 to 17:59).
	 */
	@FXML
	public void onDayNews() {
		loadNewsByTimePeriod(12, 0, 17, 59);
	}

	/**
	 * Loads and displays the news from the evening (18:00 to 23:59).
	 */
	@FXML
	public void onEveningNews() {
		loadNewsByTimePeriod(18, 0, 23, 59);
	}

	/**
	 * Loads the news within a specified time period defined by start and end hours and minutes,
	 * and updates the display with the fetched news.
	 *
	 * @param startHour    the start hour of the time period (0-23)
	 * @param startMinutes the start minutes of the time period (0-59)
	 * @param endHour      the end hour of the time period (0-23)
	 * @param endMinutes   the end minutes of the time period (0-59)
	 */
	private void loadNewsByTimePeriod(int startHour, int startMinutes, int endHour, int endMinutes) {
		var start = LocalDateTime.now().withHour(startHour).withMinute(startMinutes);
		var end = LocalDateTime.now().withHour(endHour).withMinute(endMinutes);

		loadNewsByDatePeriod(start, end);
	}

	/**
	 * Loads the news within a specified date period defined by start and end LocalDateTime,
	 * and updates the news list display.
	 *
	 * @param start the start date and time of the period to load news
	 * @param end   the end date and time of the period to load news
	 */
	private void loadNewsByDatePeriod(LocalDateTime start, LocalDateTime end) {
		try {
			newsList = newsApiService.getNews(start, end);
			newsList.sort(Comparator.comparing(NewsDTO::getPublicationTime));

			if (newsList.isEmpty()) {
				showInfoAlert("No news found for the selected time period.");
			} else {
				currentIndex = 0;
				showCurrentNews();
			}
		} catch (Exception e) {
			Platform.runLater(() -> showErrorAlert("Failed to load news: " + e.getMessage()));
		}
	}

	/**
	 * Opens a dialog to create a new news entry and sends it to the API.
	 */
	@FXML
	public void onCreateNews() {
		Dialog<NewsDTO> dialog = new Dialog<>();
		dialog.setTitle("Create news");
		dialog.setHeaderText("Enter the headline and description of the news:");

		var okButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		var headlineField = new TextField();
		headlineField.setPromptText("Headline");

		var descriptionField = new TextArea();
		descriptionField.setPromptText("Description");

		var grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		grid.add(new Label("Headline:"), 0, 0);
		grid.add(headlineField, 1, 0);
		grid.add(new Label("Description:"), 0, 1);
		grid.add(descriptionField, 1, 1);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return NewsDTO.builder()
						.headline(headlineField.getText())
						.description(descriptionField.getText())
						.publicationTime(LocalDateTime.now())
						.build();
			}
			return null;
		});

		var result = dialog.showAndWait();

		result.ifPresent(news -> {
			try {
				newsApiService.createNews(news);
				loadAllNews();
				currentIndex = newsList.size() - 1;
				showCurrentNews();
			} catch (Exception e) {
				showErrorAlert("Failed to create news: " + e.getMessage());
			}
		});
	}

	/**
	 * Opens a dialog to edit the current news title and description,
	 * and updates the news via the API.
	 */
	@FXML
	public void onEditNews() {
		if (!newsList.isEmpty()) {
			var currentNews = newsList.get(currentIndex);

			// Create a dialog to edit title and description
			Dialog<NewsDTO> dialog = new Dialog<>();
			dialog.setTitle("Edit News");
			dialog.setHeaderText("Change the title and description of the news:");

			var okButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

			var titleField = new TextField(currentNews.getHeadline());
			var descriptionField = new TextArea(currentNews.getDescription());

			var grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.add(new Label("Title:"), 0, 0);
			grid.add(titleField, 1, 0);
			grid.add(new Label("Description:"), 0, 1);
			grid.add(descriptionField, 1, 1);

			dialog.getDialogPane().setContent(grid);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == okButtonType) {
					currentNews.setHeadline(titleField.getText());
					currentNews.setDescription(descriptionField.getText());
					currentNews.setPublicationTime(LocalDateTime.now());
					return currentNews;
				}
				return null;
			});

			// Show the dialog and handle the result
			var result = dialog.showAndWait();

			result.ifPresent(updatedNews -> {
				try {
					newsApiService.updateNews(updatedNews.getId(), updatedNews);
					loadAllNews();
					showCurrentNews();
				} catch (Exception e) {
					showErrorAlert("Failed to update news: " + e.getMessage());
				}
			});
		} else {
			showWarningAlert("No news available to edit.");
		}
	}


	/**
	 * Deletes the current news entry and reloads the news list from the API.
	 */
	@FXML
	public void onDeleteNews() {
		if (!newsList.isEmpty()) {
			var currentNews = newsList.get(currentIndex);

			try {
				newsApiService.deleteNews(currentNews.getId());
				loadAllNews();
				if (currentIndex >= newsList.size()) {
					currentIndex = newsList.size() - 1;
				}
				showCurrentNews();
			} catch (Exception e) {
				showErrorAlert("Failed to delete news: " + e.getMessage());
			}
		} else {
			showWarningAlert("No news available to delete.");
		}
	}

	/**
	 * Shows an error alert with the given message.
	 *
	 * @param content the message to display in the error alert
	 */
	private void showErrorAlert(String content) {
		showAlert(AlertType.ERROR, content);
	}

	/**
	 * Shows a warning alert with the given message.
	 *
	 * @param content the message to display in the warning alert
	 */
	private void showWarningAlert(String content) {
		showAlert(AlertType.WARNING, content);
	}

	/**
	 * Shows an information alert with the given message.
	 *
	 * @param content the message to display in the information alert
	 */
	private void showInfoAlert(String content) {
		showAlert(AlertType.INFORMATION, content);
	}

	/**
	 * Displays an alert with the specified type
	 */

	private void showAlert(AlertType type, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(type.name());
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
