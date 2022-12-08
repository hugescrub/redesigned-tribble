package net.newsportal.repository;

import net.newsportal.models.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {
    Source findBySourceName(String sourceName);
}
