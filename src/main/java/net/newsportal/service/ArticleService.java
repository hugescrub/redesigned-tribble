package net.newsportal.service;

import net.newsportal.models.Article;
import net.newsportal.models.Source;
import net.newsportal.models.User;
import net.newsportal.models.dto.ArticleDto;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.repository.SourceRepository;
import net.newsportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class ArticleService {

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
    public HttpStatus createArticle(ArticleDto articleDto) {
        final String authorUsername = articleDto.getAuthorUsername();
        if (articleRepository.existsByTitle(articleDto.getTitle())) {
            return HttpStatus.BAD_REQUEST; // return exists with such title message from controller
        } else if (authorUsername != null && userRepository.existsByUsername(authorUsername)) {
            User user = userRepository.findByUsername(authorUsername);
            // get source from database associated with source passed in request body
            Source source = sourceRepository.findBySourceName(articleDto.getSource().getSourceName());
            Article article = new Article(
                    articleDto.getTitle(),
                    articleDto.getBody(),
                    LocalDateTime.now()
            );
            article.setAuthor(user);
            article.getSources().add(source);
            articleRepository.save(article);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND; // return no user found in database
        }
    }
}
