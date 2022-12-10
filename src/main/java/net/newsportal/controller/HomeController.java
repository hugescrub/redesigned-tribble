package net.newsportal.controller;

import net.newsportal.models.Article;
import net.newsportal.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    ArticleRepository articleRepository;

    @GetMapping
    String getHome(Model model) {
        // TODO Получать только проверенные классифированные статьи
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "home";
    }

    @GetMapping("/about")
    String about() {
        return "about";
    }

    @GetMapping("/article/{id}")
    String getArticle(Model model, @PathVariable String id) {
        // TODO Не давать перейти на непроверенную, неклассифицированную статью
        Optional<Article> article = articleRepository.findById(Long.valueOf(id));
        if (article.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("article", article.get());
        return "article";
    }
}
