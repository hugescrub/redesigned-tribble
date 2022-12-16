package net.newsportal.models;

import lombok.Data;

@Data
public class ArticlePatchBody {
    private String classificationResult;

    private Boolean isApproved;

    private Boolean isFake;

    private Long classificationId;

    public ArticlePatchBody() {
    }
}
