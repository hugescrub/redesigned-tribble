package net.newsportal.service;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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
    public boolean createArticle(String title, String body) {
        if (!articleRepository.existsByTitle(title)) {
            Article article = new Article(title, body, LocalDateTime.now());
            articleRepository.save(article);

            WebClient client = WebClient.create(VALIDATION_BASE_URL);
            client.post()
                    .uri("/api/validator/validate")
                    .bodyValue(articleRepository.findByTitle(title).getId())
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(System.out::println);
            return true;
        }
        return false;
    }
}
