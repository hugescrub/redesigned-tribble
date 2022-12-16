package net.newsportal.models;

import jdk.jfr.BooleanFlag;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 60)
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 800)
    private String body;

    @NotNull
    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private User author;


    @Column(columnDefinition = "TEXT")
    private String tag = null;

    // :^)
    @Transient
    private ClassificationResult classificationResult;

    @Column
    private Long classificationId;

    public Long getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Long classificationId) {
        this.classificationId = classificationId;
    }

    @BooleanFlag
    private boolean isApproved = false;

    @BooleanFlag
    private boolean isFake = true;

    public Article() {
    }

    public Article(String title, String body, LocalDateTime created) {
        this.title = title;
        this.body = body;
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public void setFake(boolean isFake) {
        this.isFake = isFake;
    }
    public ClassificationResult getClassificationResult() {
        return classificationResult;
    }

    public void setClassificationResult(ClassificationResult classificationResult) {
        this.classificationResult = classificationResult;
    }

    @Override
    public String toString() {
        return "Article{" +
                "\nid=" + id +
                "\ntitle='" + title + '\'' +
                "\nbody='" + body + '\'' +
                "\ncreated=" + created +
                "\n}";
    }
}
