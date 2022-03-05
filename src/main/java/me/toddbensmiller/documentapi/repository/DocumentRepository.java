package me.toddbensmiller.documentapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.toddbensmiller.documentapi.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    public List<Document> findByTitle(String title);
}
