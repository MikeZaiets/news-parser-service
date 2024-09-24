package ua.com.zmike.client.service;

import java.time.LocalDateTime;
import java.util.List;
import ua.com.zmike.client.dto.NewsDTO;

public interface NewsApiService {

	List<NewsDTO> getNews(LocalDateTime start, LocalDateTime end);

	void createNews(NewsDTO newsDTO);

	void updateNews(Long id, NewsDTO newsDTO);

	void deleteNews(Long id);

	void deleteNewsBeforeTime(LocalDateTime beforeTime);

}
