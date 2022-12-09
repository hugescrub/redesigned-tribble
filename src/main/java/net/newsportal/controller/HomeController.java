package net.newsportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @GetMapping
    String getHome(Model model) {
        Article article = new Article();
        article.title = "Article";
        article.body = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Dignissimos iusto nam laudantium perspiciatis harum assumenda reiciendis est ex molestiae neque.";
        article.author = "Mario";
        article.date = "2022/1/1";
        article.id = "1";

        Article article2 = new Article();
        article2.title = "Article 2";
        article2.body = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Dignissimos iusto nam laudantium perspiciatis harum assumenda reiciendis est ex molestiae neque.";
        article2.author = "Luigi";
        article2.date = "2022/1/2";
        article2.id = "2";

        model.addAttribute("articles", List.of(article, article2));

        return "home";
    }
}

// Mock Class
class Article {
    public String id;
    public String title;
    public String body;
    public String author;
    public String date;
}
