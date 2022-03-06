package me.toddbensmiller.documentapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.PartialDocument;
import me.toddbensmiller.documentapi.entity.Word;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    public List<Document> findByTitle(String title);

    @Query("SELECT distinct d.id FROM Document d JOIN d.containedInText as a WHERE a IN (:words) GROUP BY d.id having count(a.id) = :size")
    public List<Long> findAllByTextWords(Set<Word> words, Long size);

    @Query("SELECT d.title FROM Document d WHERE d.id = :id")
    public String findTitleById(Long id);

    @Query("SELECT NEW me.toddbensmiller.documentapi.entity.PartialDocument(d.id, d.title) FROM Document d WHERE d.id IN (:ids)")
    public List<PartialDocument> findTitlesById(Set<Long> ids);
}
