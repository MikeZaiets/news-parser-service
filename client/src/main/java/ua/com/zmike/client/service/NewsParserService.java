package ua.com.zmike.client.service;

import java.util.List;
import ua.com.zmike.client.dto.NewsDTO;


public interface NewsParserService {

	List<NewsDTO> parseNewsFromWebsite();

}
