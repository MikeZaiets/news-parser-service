package ua.com.zmike.server.converter.impl;

import java.sql.Timestamp;
import org.springframework.stereotype.Component;
import ua.com.zmike.server.converter.DtoConverter;
import ua.com.zmike.server.dto.NewsDTO;
import ua.com.zmike.server.model.News;

/**
 * Implementation of the DtoConverter interface for converting between NewsDTO and News entities.
 * This class provides methods to convert a News entity to a NewsDTO and vice versa.
 */
@Component
public class NewsConverterImpl implements DtoConverter<NewsDTO, News> {

	/**
	 * Converts a News entity to a NewsDTO object.
	 *
	 * @param news the News entity to be converted
	 * @return the converted NewsDTO object
	 */
	@Override
	public NewsDTO convertToDto(News news) {
		return NewsDTO.builder()
				.id(news.getId())
				.headline(news.getHeadline())
				.description(news.getDescription())
				.publicationTime(news.getPublicationTime().toLocalDateTime())
				.build();
	}

	/**
	 * Converts a NewsDTO object to a News entity.
	 *
	 * @param newsDto the NewsDTO object to be converted
	 * @return the converted News entity
	 */
	@Override
	public News convertFromDto(NewsDTO newsDto) {
		var news = new News();
		news.setId(newsDto.getId());
		news.setHeadline(newsDto.getHeadline());
		news.setDescription(newsDto.getDescription());
		news.setPublicationTime(Timestamp.valueOf(newsDto.getPublicationTime()));
		return news;
	}
}
