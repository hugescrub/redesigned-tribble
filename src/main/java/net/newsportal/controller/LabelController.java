package net.newsportal.controller;

import net.newsportal.models.Label;
import net.newsportal.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/labels")
public class LabelController {
    @Autowired
    public LabelRepository labelRepository;

    // TODO write add, delete by ID methods
    @GetMapping
    public Iterable<Label> getLabels() {
        return labelRepository.findAll();
    }
}
