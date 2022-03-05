package me.toddbensmiller.documentapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.Word;
import me.toddbensmiller.documentapi.service.DocumentService;
import me.toddbensmiller.documentapi.service.WordService;

@RestController
@RequestMapping("/api")
public class SpringController {
    private DocumentService docService;
    private WordService wordService;

    @Autowired
    public SpringController(DocumentService documentService, WordService wordService) {
        this.docService = documentService;
        this.wordService = wordService;
    }

    @GetMapping("/word/{id}")
    public Word getWord(@PathVariable("id") Long id) {
        return wordService.getWord(id);
    }

    @GetMapping("/document/{id}")
    public Document getDocument(@PathVariable("id") Long id) {
        return docService.getDocument(id);
    }

    @GetMapping("/documents")
    public List<Document> getDocuments() {
        return docService.getAll();
    }

    @GetMapping("/words")
    public List<Word> getWords() {
        return wordService.getAll();
    }

    @GetMapping("/query")
    public List<Document> query() {
        // TODO: implement
        return docService.getAll();
    }
}
