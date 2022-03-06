package me.toddbensmiller.documentapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.PartialDocument;
import me.toddbensmiller.documentapi.service.DocumentService;

@RestController
@RequestMapping("/api")
public class SpringController {
    private DocumentService docService;

    @Autowired
    public SpringController(DocumentService documentService) {
        this.docService = documentService;
    }

    @GetMapping("/document/{id}")
    public Document getDocument(@PathVariable("id") Long id) {
        return docService.getDocument(id);
    }

    @GetMapping("/query")
    public List<PartialDocument> query(@RequestParam("query") String query) {
        return docService.getFromQuery(query);
    }

    @PostMapping("/document")
    public Long createDocument(@RequestParam("title") String title, @RequestParam("body") String text) {
        Document d = new Document();
        d.setTitle(title);
        d.setText(text);
        return docService.addDocument(d).getId();
    }
}
