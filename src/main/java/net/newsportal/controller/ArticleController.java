package net.newsportal.controller;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.models.Article;
import net.newsportal.models.ArticlePatchBody;
import net.newsportal.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/portal/news")
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Article> article = articleRepository.findById(Long.valueOf(id));
        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(article.get());
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PATCH)
    public ResponseEntity<?> setTags(@PathVariable String id, @RequestBody ArticlePatchBody articlePatchBody) {
        Optional<Article> article = articleRepository.findById(Long.valueOf(id));

        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Article articleItem = article.get();

        if (articlePatchBody.classificationResult != null) {
            articleItem.setTag(articlePatchBody.classificationResult);
        }
        if (articlePatchBody.classificationId != null) {
            articleItem.setClassificationId(articlePatchBody.classificationId);
        }
        if (articlePatchBody.isApproved != null) {
            articleItem.setApproved(articlePatchBody.isApproved);
        }
        articleRepository.save(article.get());

        return ResponseEntity.ok().body("OK");
    }

    // DEPRECATED
    @RequestMapping(value="/approve/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> setIsApproved(@PathVariable String id) {
        Optional<Article> article = articleRepository.findById(Long.valueOf(id));

        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        article.get().setApproved(true);
        articleRepository.save(article.get());

        return ResponseEntity.ok().body("OK");
    }
}
