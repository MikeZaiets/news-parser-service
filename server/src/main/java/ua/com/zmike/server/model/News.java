package ua.com.zmike.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(schema = "news_service_schema", name = "news")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String headline;

	private String description;

	@Column(name = "publication_time")
	private Timestamp publicationTime;
}
