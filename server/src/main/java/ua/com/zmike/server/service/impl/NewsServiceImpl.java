package ua.com.zmike.server.service.impl;

import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.zmike.server.converter.DtoConverter;
import ua.com.zmike.server.dto.NewsDTO;
import ua.com.zmike.server.exception.TargetNotFoundException;
import ua.com.zmike.server.model.News;
import ua.com.zmike.server.repository.NewsRepository;
import ua.com.zmike.server.service.NewsService;

/**
 * Implementation of the {@link NewsService} interface.
 * This service provides CRUD operations and custom logic for managing {@link News} entities.
 * Uses {@link DtoConverter} to convert between {@link NewsDTO} and {@link News} entities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

	private final DtoConverter<NewsDTO, News> converter;
	private final NewsRepository newsRepository;

	/**
	 * Finds all news published between the given start and end {@link Timestamp}.
	 *
	 * @param start The start timestamp for the search range.
	 * @param end   The end timestamp for the search range.
	 * @return A set of {@link NewsDTO} objects matching the publication time range.
	 */
	@Override
	public Set<NewsDTO> findByPublicationTimeBetween(Timestamp start, Timestamp end) {
		return newsRepository.findByPublicationTimeBetween(start, end).stream()
				.map(converter::convertToDto)
				.collect(Collectors.toSet());
	}

	/**
	 * Adds a new news entry.
	 *
	 * @param newsDto The DTO representing the news to be added.
	 * @return The DTO of the saved news entry.
	 */
	@Override
	@Transactional
	public NewsDTO addOne(NewsDTO newsDto) {
		var news = converter.convertFromDto(newsDto);
		return converter.convertToDto(newsRepository.save(news));
	}

	/**
	 * Retrieves a single news entry by its ID.
	 *
	 * @param id The ID of the news entry to retrieve.
	 * @return The DTO of the found news entry.
	 * @throws TargetNotFoundException if no news with the given ID is found.
	 */
	@Override
	public NewsDTO getOneById(Long id) {
		return converter.convertToDto(getExistingNewsById(id));
	}

	/**
	 * Updates an existing news entry by its ID.
	 *
	 * @param id      The ID of the news entry to update.
	 * @param newsDto The DTO containing the updated information.
	 * @return The DTO of the updated news entry.
	 * @throws TargetNotFoundException if no news with the given ID is found.
	 */
	@Override
	@Transactional
	public NewsDTO updateOne(Long id, NewsDTO newsDto) {
		var news = getExistingNewsById(id);
		news.setHeadline(newsDto.getHeadline());
		news.setDescription(newsDto.getDescription());
		news.setPublicationTime(Timestamp.valueOf(newsDto.getPublicationTime()));
		return converter.convertToDto(newsRepository.save(news));
	}

	/**
	 * Deletes a news entry by its ID.
	 *
	 * @param id The ID of the news entry to delete.
	 */
	@Override
	@Transactional
	public void deleteOneById(Long id) {
		newsRepository.deleteById(id);
	}

	/**
	 * Deletes all news entries published before a given timestamp.
	 *
	 * @param time The timestamp before which all news should be deleted.
	 */
	@Override
	@Transactional
	public void deleteBeforeTime(Timestamp time) {
		newsRepository.deleteByPublicationTimeBefore(time);
	}

	/**
	 * Retrieves an existing news entry by its ID.
	 *
	 * @param id The ID of the news entry to retrieve.
	 * @return The found {@link News} entity.
	 * @throws TargetNotFoundException if no news with the given ID is found.
	 */
	private News getExistingNewsById(Long id) {
		return newsRepository.findById(id)
				.orElseThrow(() -> new TargetNotFoundException("News", "id", id));
	}

}
