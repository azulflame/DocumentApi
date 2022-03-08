package me.toddbensmiller.documentapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.toddbensmiller.documentapi.entity.TextRelation;

public interface TextRelationRepository extends JpaRepository<TextRelation, Long> {
    @Query("SELECT t FROM TextRelation t WHERE t.wordId IN (:wordIds)")
    List<TextRelation> findAllByWordId(Set<Long> wordIds);
}
