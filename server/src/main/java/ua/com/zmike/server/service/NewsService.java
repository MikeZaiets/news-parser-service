package ua.com.zmike.server.service;

import java.sql.Timestamp;
import java.util.Set;
import ua.com.zmike.server.dto.NewsDTO;

public interface NewsService {

	Set<NewsDTO> findByPublicationTimeBetween(Timestamp start, Timestamp end);

	NewsDTO getOneById(Long id);

	NewsDTO addOne(NewsDTO product);

	NewsDTO updateOne(Long id, NewsDTO product);

	void deleteOneById(Long id);

	void deleteBeforeTime(Timestamp endTime);

}
