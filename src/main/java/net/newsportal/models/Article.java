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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 30)
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "article_sources",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "source_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"article_id", "source_id"}))
    private List<Source> sources = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private User author;


    @Column(columnDefinition = "TEXT")
    private String tag = null;

    // :^)
    @Transient
    private ClassificationResult classificationResult;

    @BooleanFlag
    private boolean isApproved = false;

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

    public void setSources(List<Source> sources) {
        this.sources = sources;
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
