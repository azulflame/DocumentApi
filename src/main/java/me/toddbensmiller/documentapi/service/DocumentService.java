package me.toddbensmiller.documentapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.PartialDocument;
import me.toddbensmiller.documentapi.entity.Word;
import me.toddbensmiller.documentapi.parse.Stemmer;
import me.toddbensmiller.documentapi.repository.DocumentRepository;

@Service
public class DocumentService {
    private DocumentRepository repo;
    private WordService wordService;
    private Stemmer stemmer;

    private static final Set<Character> keep = new HashSet<>(
            "abcdefghijklmnopqrstuvwxyz ".chars().mapToObj(c -> (char) c).collect(Collectors.toList()));

    @Autowired
    public DocumentService(DocumentRepository documentRepository, Stemmer stemmer, WordService wordService) {
        repo = documentRepository;
        this.stemmer = stemmer;
        this.wordService = wordService;
    }

    /**
     * Return a document with a specific ID
     * 
     * @param id The document ID to fetch
     * @return the document with that ID, if one exists
     */
    public Document getDocument(Long id) {
        return repo.findById(id).get();
    }

    /**
     * Processes and returns a document where relations have already been processed.
     * 
     * @param doc A preprocessed Document to add to the database
     * @return The document stored in the database
     */
    public Document addProcessedDocument(Document doc) {
        if (doc.getText() == null || doc.getText().length() == 0 || doc.getTitle() == null
                || doc.getTitle().length() == 0) {
            throw new IllegalStateException("Must have a title and a text body");
        }
        List<Document> matchingTitles = repo.findByTitle(doc.getTitle());
        for (Document document : matchingTitles) {
            if (document.getText().equals(doc.getText())) {
                return document;
            }
        }
        return repo.save(doc);
    }

    /**
     * Process and add a Document to the database.
     * Requires the text and title to be populated.
     * If an existing Document is in the database with that title and text, return
     * that document instead
     * 
     * @param doc A document to process and save
     * @return The document stored or found in the database
     */
    public Document addDocument(Document doc) {
        // require document title and text to be present
        if (doc.getText() == null || doc.getText().length() == 0 || doc.getTitle() == null
                || doc.getTitle().length() == 0) {
            throw new IllegalStateException("Must have a title and a text body");
        }
        List<Document> matchingTitles = repo.findByTitle(doc.getTitle());
        for (Document document : matchingTitles) {
            if (document.getText().equals(doc.getText())) {
                return document;
            }
        }
        doc.setContainedInText(getWordsFromText(doc.getText()));
        doc.setContainedInTitle(getWordsFromText(doc.getTitle()));
        return repo.save(doc);
    }

    /**
     * Return a list of all documents.
     * This will likely be very slow. Recommended to not use.
     * 
     * @return A List of all documents in the database
     */
    public List<Document> getAll() {
        return repo.findAll();
    }

    /**
     * Returns a Set containing unique Word entities, found in a body of text
     * 
     * @param text A body of text
     * @return The Set of unique words
     */
    public Set<Word> getWordsFromText(String text) {
        return getImperitiveStemsFromText(text).stream()
                .map(x -> wordService.saveWord(x))
                .collect(Collectors.toSet());
    }

    /**
     * Returns a Set object containing the unique stems found in the supplied body
     * of text. Currently only works with the english language.
     * 
     * @param text A body of text
     * @return The Set of unique stems
     */
    public Set<String> getImperitiveStemsFromText(String text) {
        String toLowercase = text.toLowerCase();
        StringBuilder filtered = new StringBuilder(toLowercase.length());
        for (char c : toLowercase.toCharArray()) {
            if (keep.contains(c)) {
                filtered.append(c);
            }
        }
        String[] split = filtered.toString().split(" ");
        Set<String> asASet = new HashSet<>();
        for (String word : split) {
            asASet.add(word);
        }
        Set<String> stemmed = new HashSet<>();
        for (String word : asASet) {
            stemmed.add(stemmer.stemWord(word));
        }
        return stemmed;
    }

    public void saveAll(Set<Document> docs) {
        repo.saveAll(docs);
    }

    @Transactional
    public List<PartialDocument> getFromQuery(String query) {
        Set<Word> words = getWordsFromText(query);
        Set<Long> ids = new HashSet<>(repo.findAllByTextWords(words, Long.valueOf("" + words.size())));
        return ids.parallelStream().map(x -> new PartialDocument(x, repo.findTitleById(x)))
                .collect(Collectors.toList());
    }
}
