package net.newsportal.controller;

import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ArticleRepository articleRepository;

    @Autowired
    public HomeController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public String getHome(Model model) {
        List<Article> articles = articleRepository.findAll()
                .stream()
                .filter(article -> article.isApproved() && article.getTag() != null)
                .collect(Collectors.toList());
        model.addAttribute("articles", articles);
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/article/{id}")
    public String getArticle(Model model, @PathVariable Long id) {
        if (articleRepository.findById(id).isPresent()) {
            Article article = articleRepository.findById(id).get();
            if (article.isApproved() && article.getTag() != null)
                model.addAttribute("article", article);
            else
                model.addAttribute("error", "This article is not yet approved.");
        }
        return "article";
    }
}
