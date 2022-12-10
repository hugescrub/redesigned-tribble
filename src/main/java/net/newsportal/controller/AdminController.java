package net.newsportal.controller;

import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final ArticleRepository articleRepository;

    @Autowired
    public AdminController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<Article> articles = articleRepository.findAll()
                .stream()
                .filter(article -> !article.isApproved() && article.getTag() != null)
                .collect(Collectors.toList());
        model.addAttribute("articles", articles);
        return "admin";
    }

    @GetMapping("/compose")
    public String composePage() {
        return "compose";
    }

    @PostMapping(value = "/compose", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createArticle(Model model, @RequestBody MultiValueMap<String, String> formData) {
        String title = formData.get("title").get(0);
        String body = formData.get("body").get(0);

        // TODO create article logic

        return "compose";
    }
}
