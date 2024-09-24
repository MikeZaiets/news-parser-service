package ua.com.zmike.client.schaduler;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.zmike.client.service.NewsApiService;
import ua.com.zmike.client.service.NewsParserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

	private final NewsParserService parserService;
	private final NewsApiService apiService;

	@Scheduled(cron = "${client.cron.clean}")
	public void cleanOldNews() {
		log.info("Executing clean old News (before now)");
		apiService.deleteNewsBeforeTime(LocalDateTime.now());
	}

	@Scheduled(cron = "${client.cron.parse}")
	public void parseAndSendNews() {
		try {
			parserService.parseNewsFromWebsite().forEach(apiService::createNews);
		} catch (Exception e) {
			log.error("Error parsing news", e);
		}
	}
}
