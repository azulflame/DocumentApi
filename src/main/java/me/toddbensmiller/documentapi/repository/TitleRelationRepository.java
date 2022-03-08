package me.toddbensmiller.documentapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.toddbensmiller.documentapi.entity.TitleRelation;

public interface TitleRelationRepository extends JpaRepository<TitleRelation, Long> {
    @Query("SELECT t FROM TitleRelation t WHERE t.wordId IN (:wordIds)")
    List<TitleRelation> findAllByWordId(Set<Long> wordIds);
}