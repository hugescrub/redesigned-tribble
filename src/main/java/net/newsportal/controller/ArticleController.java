package net.newsportal.controller;

import lombok.extern.slf4j.Slf4j;
import net.newsportal.models.Article;
import net.newsportal.models.ArticlePatchBody;
import net.newsportal.models.dto.ArticleDto;
import net.newsportal.payload.response.MessageResponse;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.service.ArticleService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/portal/news")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleService articleService,
                             ArticleRepository articleRepository) {
        this.articleService = articleService;
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

    @PostMapping("/add")
    public ResponseEntity<?> addArticle(@RequestBody ArticleDto article) {
        if(articleService.createArticle(article).equals(HttpStatus.OK)) {
            return ResponseEntity.ok()
                    .body(new MessageResponse("Successfully added new article."));
        } else if (articleService.createArticle(article).equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The article with such title already exists."));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @GetMapping("/valid") // show all valid articles (validated, not fake, with a tag)
    public String getAllValid(Model model) {
        throw new NotYetImplementedException();
        /*
        TODO
            /Pseudocode/
            if(article.!fake && article.tag.existsInDb:? && article.author.existsInDb)
                -> return view with articles
         */
    }
}
