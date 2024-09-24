package ua.com.zmike.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsDTO {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	@NotBlank
	private String headline;
	@NotBlank
	private String description;
	@NotNull
	private LocalDateTime publicationTime;
}
