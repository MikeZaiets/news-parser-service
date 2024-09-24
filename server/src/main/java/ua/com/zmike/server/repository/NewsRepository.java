package ua.com.zmike.server.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.zmike.server.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {

	List<News> findByPublicationTimeBetween(Timestamp start, Timestamp end);

	void deleteByPublicationTimeBefore(Timestamp timeBefore);
}
