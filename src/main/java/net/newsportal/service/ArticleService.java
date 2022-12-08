package net.newsportal.service;

import net.newsportal.models.Article;
import net.newsportal.models.Source;
import net.newsportal.models.dto.ArticleDto;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SourceRepository sourceRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, SourceRepository sourceRepository) {
        this.articleRepository = articleRepository;
        this.sourceRepository = sourceRepository;
    }

    @Transactional
    public boolean createArticle(ArticleDto articleDto) {
        if (articleRepository.existsByTitle(articleDto.getTitle())) {
            return false; // return exists with such title message from controller
        } else {
            // get source from database associated with source passed in request body
            Source source = sourceRepository.findBySourceName(articleDto.getSource().getSourceName());
            // write the article to database
            Article article = new Article(
                    articleDto.getTitle(),
                    articleDto.getBody(),
                    LocalDateTime.now()
            );
            article.getSources().add(source);
            articleRepository.save(article);
            return true;
        }
    }
}
