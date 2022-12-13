package net.newsportal.controller;

import net.newsportal.models.Article;
import net.newsportal.models.ERole;
import net.newsportal.models.Role;
import net.newsportal.repository.ArticleRepository;
import net.newsportal.repository.UserRepository;
import net.newsportal.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CookieService cookieService;

    @Autowired
    public HomeController(ArticleRepository articleRepository, UserRepository userRepository, CookieService cookieService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.cookieService = cookieService;
    }

    @GetMapping
    public String getHome(Model model, HttpServletRequest request) {
        String userRole = cookieService.getRoleFromCookie(request);
        boolean isLoggedIn = false;
        if (userRole != "") isLoggedIn = true;
        model.addAttribute("role", userRole);
        model.addAttribute("isLoggedIn", isLoggedIn);

        List<Article> articles = articleRepository.findAll()
                .stream()
                .filter(article -> article.isApproved() && article.getTag() != null)
                .collect(Collectors.toList());
        model.addAttribute("articles", articles);
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        String userRole = cookieService.getRoleFromCookie(request);
        boolean isLoggedIn = false;
        if (userRole != "") isLoggedIn = true;
        model.addAttribute("role", userRole);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "about";
    }

    @GetMapping("/article/{id}")
    public String getArticle(Model model, @PathVariable Long id, HttpServletRequest request) {
        String userRole = cookieService.getRoleFromCookie(request);
        boolean isLoggedIn = false;
        if (userRole != "") isLoggedIn = true;
        model.addAttribute("role", userRole);
        model.addAttribute("isLoggedIn", isLoggedIn);

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
