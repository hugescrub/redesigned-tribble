package net.newsportal.controller;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.models.Article;
import net.newsportal.models.ArticlePatchBody;
import net.newsportal.payload.response.MessageResponse;
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
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(article.get());
    }

    @RequestMapping(value = "/{articleId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> setTags(@PathVariable Long articleId, @RequestBody ArticlePatchBody articlePatchBody) {
        Article article = articleRepository.findById(articleId).get();

        if (articlePatchBody.getClassificationResult() != null) {
            article.setTag(articlePatchBody.getClassificationResult());
        }
        if (articlePatchBody.getClassificationId() != null) {
            article.setClassificationId(articlePatchBody.getClassificationId());
        }
        if (articlePatchBody.getClassificationResult() != null
                && articlePatchBody.getClassificationResult().equals("")) {
            article.setTag(null);
        }
        if(articlePatchBody.getIsApproved() != null){
            article.setApproved(articlePatchBody.getIsApproved());
        }
        if (articlePatchBody.getClassificationId() != null) {
            article.setClassificationId(articlePatchBody.getClassificationId());
        }
        if (articlePatchBody.getIsFake() != null) {
            article.setFake(articlePatchBody.getIsFake());
        }
        articleRepository.save(article);
        return ResponseEntity.ok()
                .body(new MessageResponse("OK"));
    }

    // DEPRECATED
    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
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
