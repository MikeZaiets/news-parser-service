package ua.com.zmike.server.controller;

import jakarta.validation.Valid;
import java.sql.Timestamp;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.com.zmike.server.dto.NewsDTO;
import ua.com.zmike.server.service.NewsService;

/**
 * Controller class for managing news-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting news.
 */
@Slf4j
@RestController
@Validated
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

	private final NewsService newsService;

	/**
	 * Creates a new news entry.
	 *
	 * @param product the details of the news entry to create, provided as a {@link NewsDTO}.
	 * @return the created news entry as a {@link NewsDTO}.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public NewsDTO create(@RequestBody @Valid NewsDTO product) {
		log.info("Add News with params: {}", product);
		return newsService.addOne(product);
	}

	/**
	 * Retrieves a set of news entries published between the specified start and end times.
	 * <p>
	 * Example:
	 * GET /news?start=1695643200000&end=1695643300000
	 *
	 * @param start the start time in milliseconds (epoch time).
	 * @param end   the end time in milliseconds (epoch time).
	 * @return a set of {@link NewsDTO} representing the news entries found within the time range.
	 */
	@GetMapping
	public Set<NewsDTO> getNews(@RequestParam long start, @RequestParam long end) {
		var startTime = new Timestamp(start);
		var endTime = new Timestamp(end);

		log.info("Find News in publication time between {} and {}", startTime, endTime);
		return newsService.findByPublicationTimeBetween(startTime, endTime);
	}

	/**
	 * Retrieves a news entry by its ID.
	 *
	 * @param id the ID of the news entry to retrieve.
	 * @return the news entry as a {@link NewsDTO}.
	 */
	@GetMapping("/{id}")
	public NewsDTO getById(@PathVariable("id") Long id) {
		log.info("Get News by id: {}", id);
		return newsService.getOneById(id);
	}

	/**
	 * Updates an existing news entry by its ID.
	 *
	 * @param id      the ID of the news entry to update.
	 * @param product the updated details of the news entry as a {@link NewsDTO}.
	 * @return the updated news entry as a {@link NewsDTO}.
	 */
	@PutMapping("/{id}")
	public NewsDTO update(@PathVariable("id") Long id,
						  @RequestBody NewsDTO product) {
		log.info("Update News by id: {} for params: {}", id, product);
		return newsService.updateOne(id, product);
	}

	/**
	 * Deletes a news entry by its ID.
	 *
	 * @param id the ID of the news entry to delete.
	 */
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		log.info("Delete News by id: {}", id);
		newsService.deleteOneById(id);
	}

	/**
	 * Deletes old news entries based on a pre-defined condition (e.g., older than entered time date).
	 * <p>
	 * Example:
	 * DELETE /news?time=1695643200000
	 *
	 * @param time the timestamp in milliseconds (since the epoch) representing
	 *             the upper boundary for deleting news entries. News published
	 *             before this time will be deleted.
	 */
	@DeleteMapping
	public void deleteBeforeTime(@RequestParam("time") long time) {
		var endTime = new Timestamp(time);
		log.info("Deleting news before time: {}", endTime);
		newsService.deleteBeforeTime(endTime);
	}
}

