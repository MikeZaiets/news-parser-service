package ua.com.zmike.client.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.com.zmike.client.dto.NewsDTO;
import ua.com.zmike.client.service.NewsApiService;

@Service
@RequiredArgsConstructor
public class NewsApiServiceImpl implements NewsApiService {

	private final RestTemplate restTemplate;

	@Value("${client.news-api.url}")
	private String newsApiUrl;

	/**
	 * Fetches news within the specified time range.
	 *
	 * @param start Start of the time range.
	 * @param end   End of the time range.
	 * @return List of news items.
	 */
	public List<NewsDTO> getNews(LocalDateTime start, LocalDateTime end) {
		var timeStart = Timestamp.valueOf(start).getTime();
		var timeEnd = Timestamp.valueOf(end).getTime();
		var url = String.format("%s?start=%s&end=%s", newsApiUrl, timeStart, timeEnd);
		var newsArray = restTemplate.getForObject(url, NewsDTO[].class);
		return Arrays.asList(newsArray);
	}

	/**
	 * Creates a new news item.
	 *
	 * @param newsDTO News item to be created.
	 */
	public void createNews(NewsDTO newsDTO) {
		restTemplate.postForEntity(newsApiUrl, newsDTO, NewsDTO.class);
	}

	/**
	 * Updates an existing news item.
	 *
	 * @param id      ID of the news item to be updated.
	 * @param newsDTO Updated news data.
	 */
	public void updateNews(Long id, NewsDTO newsDTO) {
		var url = String.format("%s/%d", newsApiUrl, id);
		restTemplate.put(url, newsDTO);
	}

	/**
	 * Deletes a news item by its ID.
	 *
	 * @param id ID of the news item to be deleted.
	 */
	public void deleteNews(Long id) {
		var url = String.format("%s/%d", newsApiUrl, id);
		restTemplate.delete(url);
	}

	/**
	 * Deletes news that were published before the specified time.
	 *
	 * @param time LocalDateTime representing the cutoff time for deletion.
	 *             All news published before this time will be deleted.
	 */
	@Override
	public void deleteNewsBeforeTime(LocalDateTime time) {
		long beforeTime = Timestamp.valueOf(time).getTime();
		String url = String.format("%s?time=%d", newsApiUrl, beforeTime);
		restTemplate.delete(url);
	}

}
