package ua.com.zmike.client.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.com.zmike.client.dto.NewsDTO;
import ua.com.zmike.client.service.NewsParserService;

@Service
@Slf4j
public class NewsParserServiceImpl implements NewsParserService {

	@Value("${client.news-site.url}")
	private String newsSiteUrl;

	@Value("${client.news-site.selector.news-item}")
	private String newsSelector;

	@Value("${client.news-site.selector.headline}")
	private String headlineSelector;

	@Value("${client.news-site.selector.description}")
	private String descriptionSelector;

	@Value("${client.news-site.selector.publication-time}")
	private String publicationTimeSelector;

	@Override
	public List<NewsDTO> parseNewsFromWebsite() {
		List<NewsDTO> newsList = new ArrayList<>();

		try {
			var parentDocument = Jsoup.connect(newsSiteUrl).get();
			var newsElements = parentDocument.select(newsSelector);

			for (Element newsElement : newsElements) {
				var newsDetailsLink = newsElement.select("a").attr("href");

				if (!newsDetailsLink.startsWith("http")) {
					newsDetailsLink = newsSiteUrl + newsDetailsLink;
				}

				var headline = fetchHeadline(newsDetailsLink);
				var description = fetchNewsDescription(newsDetailsLink);
				var publicationTimeText = fetchPublicationTime(newsDetailsLink);

				if (!headline.isEmpty() && !description.isEmpty() && !publicationTimeText.isEmpty()) {
					var publicationTime = OffsetDateTime.parse(publicationTimeText).toLocalDateTime();

					if (publicationTime.isAfter(LocalDateTime.now().withHour(0).withMinute(0))) {
						var newsDTO = NewsDTO.builder()
								.headline(headline)
								.description(description)
								.publicationTime(publicationTime)
								.build();

						newsList.add(newsDTO);
					}
				} else {
					log.warn("News item contains empty fields: headline='{}', description='{}', publicationTime='{}'",
							headline, description, publicationTimeText);
				}
			}
		} catch (IOException e) {
			log.error("Error while parsing news from {}: {}", newsSiteUrl, e.getMessage(), e);
		} catch (Exception e) {
			log.error("Unexpected error during parsing: {}", e.getMessage(), e);
		}
		return newsList;
	}

	private String fetchHeadline(String newsLink) {
		try {
			Document newsDoc = Jsoup.connect(newsLink).get();
			return newsDoc.select(headlineSelector).text();
		} catch (IOException e) {
			log.error("Error while fetching headline from {}: {}", newsLink, e.getMessage(), e);
			return "";
		}
	}

	private String fetchNewsDescription(String newsLink) {
		try {
			Document newsDoc = Jsoup.connect(newsLink).get();
			return newsDoc.select(descriptionSelector).text();
		} catch (IOException e) {
			log.error("Error while fetching news description from {}: {}", newsLink, e.getMessage(), e);
			return "";
		}
	}

	private String fetchPublicationTime(String newsLink) {
		try {
			Document newsDoc = Jsoup.connect(newsLink).get();
			return newsDoc.select(publicationTimeSelector).attr("datetime");
		} catch (IOException e) {
			log.error("Error while fetching publication time from {}: {}", newsLink, e.getMessage(), e);
			return "";
		}
	}
}
