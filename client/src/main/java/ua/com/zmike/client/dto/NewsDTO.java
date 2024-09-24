package ua.com.zmike.client.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsDTO {

	private Long id;

	private String headline;

	private String description;

	private LocalDateTime publicationTime;

}
