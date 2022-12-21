package net.newsportal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.newsportal.models.Article;
import net.newsportal.models.ClassificationResult;
import net.newsportal.models.Label;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.repository.LabelRepository;
import net.newsportal.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Autowired
    public AdminController(ArticleRepository articleRepository,
                           ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    @Autowired
    public LabelRepository labelRepository;

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<Article> articles = articleRepository.findAll()
                .stream()
                .filter(article -> !article.isApproved() && article.getTag() != null)
                .collect(Collectors.toList());

        // :^)
        for (var article : articles) {
            try {
                ClassificationResult classificationResult = new ObjectMapper().readValue(article.getTag(),
                        ClassificationResult.class);
                article.setClassificationResult(classificationResult);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        var labels = labelRepository.findAll();
        model.addAttribute("labels", labels);

        model.addAttribute("articles", articles);
        return "admin";
    }

    @GetMapping("/tags")
    public String tagsPage(Model model) {
        var labels = labelRepository.findAll();
        model.addAttribute("labels", labels);
        return "tags";
    }

    @GetMapping("/results")
    public String resultsPage(Model model) {
        return "results";
    }

    @PostMapping(value = "/tags", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addTag(Model model, @RequestBody MultiValueMap<String, String> formData) {
        String tag = formData.get("tag").get(0);

        Label newLabel = new Label(tag);
        labelRepository.save(newLabel);

        var labels = labelRepository.findAll();
        model.addAttribute("labels", labels);

        return "tags";
    }

    @GetMapping("/compose")
    public String composePage() {
        return "compose";
    }

    @PostMapping(value = "/compose", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createArticle(Model model, @RequestBody MultiValueMap<String, String> formData, HttpServletRequest request) {
        String title = formData.get("title").get(0);
        String body = formData.get("body").get(0);
        articleService.createArticle(title, body, request);
        return "compose";
    }
}
