package net.newsportal.service;

import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.repository.SourceRepository;
import net.newsportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class ArticleService {

    private static final String BASE_URL = "http://serviceAddr:servicePort";

    private final ArticleRepository articleRepository;
    private final SourceRepository sourceRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          SourceRepository sourceRepository,
                          UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.sourceRepository = sourceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean createArticle(String title, String body) {
        if (!articleRepository.existsByTitle(title)) {
            Article article = new Article(title, body, LocalDateTime.now());
            Long articleId = article.getId();
            articleRepository.save(article);

            WebClient client = WebClient.create(BASE_URL);
            client.post()
                    .uri("/serviceValidationEndpoint")
                    .bodyValue(articleId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(System.out::println);

            return true;
        }
        return false;
    }
}
