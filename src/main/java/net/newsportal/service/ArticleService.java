package net.newsportal.service;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.security.CookiesFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ArticleService {

    private static final String VALIDATION_BASE_URL = "http://localhost:8081";

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public boolean createArticle(String title, String body, HttpServletRequest request) {
        if (!articleRepository.existsByTitle(title)) {
            // collect cookies
            Map<String, String> cookies = new HashMap<>();
            Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(CookiesFilter.COOKIE_NAME) || cookie.getName().equals("JSESSIONID"))
                    .forEach(cookie -> cookies.put(cookie.getName(), cookie.getValue()));

            Article article = new Article(title, body, LocalDateTime.now());
            articleRepository.save(article);

            WebClient client = WebClient.create(VALIDATION_BASE_URL);
            client.post()
                    .uri("/api/validator/validate")
                    .bodyValue(articleRepository.findByTitle(title).getId())
                    .headers(httpHeaders ->
                            httpHeaders.setAll(cookies))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(System.out::println);
            return true;
        }
        return false;
    }
}
