package net.newsportal.repository;

import net.newsportal.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByCreatedBefore(LocalDateTime date);
    List<Article> findAllByBodyContains(String bodyPart);
    List<Article> findAllByTag(String tag);
    Article findByTitle(String title);
    Boolean existsByTitle(String title);
}
