package net.newsportal.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.newsportal.models.Source;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleDto {
    private String title;
    private String body;
    private Source source;
    private String authorUsername;
}
