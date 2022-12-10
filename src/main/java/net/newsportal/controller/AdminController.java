package net.newsportal.controller;

import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/admin")
    String adminPage(Model model) {
        // TODO Получать только не проверенные классифированные статьи
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "admin";
    }

    @GetMapping("/compose")
    String composePage() {
        return "compose";
    }

    @PostMapping(value = "/compose", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String createArticle(Model model, @RequestBody MultiValueMap<String, String> formData) {
        String title = formData.get("title").get(0);
        String body = formData.get("body").get(0);

        // TODO create article logic

        return "compose";
    }
}
