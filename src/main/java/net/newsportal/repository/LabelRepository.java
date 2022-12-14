package net.newsportal.repository;

import net.newsportal.models.Label;
import org.springframework.data.repository.CrudRepository;

public interface LabelRepository extends CrudRepository<Label, Long> {
}
