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
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/admin")
    String adminPage(Model model) {
        // TODO Получать только не проверенные классифированные статьи -> line 28-29
        List<Article> notApproved = articleRepository.findAll()
                .stream()
                .filter(article -> !article.isApproved() && article.getTag() != null)
                .collect(Collectors.toList());
        model.addAttribute("articles", notApproved);
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
