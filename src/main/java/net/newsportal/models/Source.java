package net.newsportal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "sources")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String sourceName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;
        Source source = (Source) o;
        return sourceName.equals(source.sourceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceName);
    }

    @Override
    public String toString() {
        return "Source{" +
                "\nid=" + id +
                "\nsourceName='" + sourceName + '\'' +
                "\n}";
    }
}
