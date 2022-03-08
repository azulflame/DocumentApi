package me.toddbensmiller.documentapi.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.toddbensmiller.documentapi.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    public List<Document> findByTitle(String title);

    @Query("SELECT d.title FROM Document d WHERE d.id = :id")
    public String findTitleById(Long id);

    // @Query("SELECT NEW me.toddbensmiller.documentapi.entity.PartialDocument(d.id,
    // d.title) FROM Document d, TitleRelation t, Word w WHERE d.id = t.docId AND
    // w.id = t.wordId AND w.stem in (:stems)")
    // public List<PartialDocument> findByTitleStems(Set<String> stems);

    // @Query("SELECT NEW me.toddbensmiller.documentapi.entity.PartialDocument(d.id,
    // d.title) FROM Document d WHERE d.id in (:ids)")
    // public List<PartialDocument> partialById(Set<Long> ids);

    // @Query("select NEW me.toddbensmiller.documentapi.entity.PartialDocument(d.id,
    // d.title) from Document d inner join (select t.docId, count(distinct word_id)
    // as asdf from TextRelation t where word_id in (:wordIds) group by doc_id) a on
    // a.doc_id = d.id and a.asdf = :countVal order by a.asdf desc")
    // @Query("SELECT NEW
    // me.toddbensmiller.documentapi.entity.PartialDocument(d.id,d.title) FROM
    // Document d WHERE :countVal = (SELECT COUNT(DISTINCT t.wordId) FROM
    // TextRelation t WHERE t.wordId IN (:wordIds) AND d.id = t.docId)")
    @Query(nativeQuery = true, value = "select d.* as title from docs d inner join (select doc_id, count(distinct word_id) as asdf from text_relation where word_id in (:wordIds) group by doc_id) a on a.doc_id = d.id and a.asdf = :countVal")
    public List<Document> partialById(Set<Long> wordIds, Long countVal);
}
