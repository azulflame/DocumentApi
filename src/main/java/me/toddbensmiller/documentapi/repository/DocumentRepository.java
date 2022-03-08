package me.toddbensmiller.documentapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.PartialDocument;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    public List<Document> findByTitle(String title);

    @Query("SELECT d.title FROM Document d WHERE d.id = :id")
    public String findTitleById(Long id);

    // @Query("SELECT NEW me.toddbensmiller.documentapi.entity.PartialDocument(d.id,
    // d.title) FROM Document d, TitleRelation t, Word w WHERE d.id = t.docId AND
    // w.id = t.wordId AND w.stem in (:stems)")
    // public List<PartialDocument> findByTitleStems(Set<String> stems);

    @Query("SELECT NEW me.toddbensmiller.documentapi.entity.PartialDocument(d.id, d.title) FROM Document d WHERE d.id in (:ids)")
    public List<PartialDocument> partialById(Set<Long> ids);
}
