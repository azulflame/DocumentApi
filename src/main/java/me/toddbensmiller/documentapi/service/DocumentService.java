package me.toddbensmiller.documentapi.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.Word;
import me.toddbensmiller.documentapi.parse.Stemmer;
import me.toddbensmiller.documentapi.parse.Stopwords;
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

    public Document getDocument(Long id) {
        return repo.findById(id).get();
    }

    public void addProcessedDocument(Document d) {
        if (d.getText() == null || d.getText().length() == 0 || d.getTitle() == null || d.getTitle().length() == 0) {
            throw new IllegalStateException("Must have a title and a text body");
        }
        repo.save(d);
    }

    public void addDocument(Document d) {
        // require document title and text to be present
        if (d.getText() == null || d.getText().length() == 0 || d.getTitle() == null || d.getTitle().length() == 0) {
            throw new IllegalStateException("Must have a title and a text body");
        }
        List<Document> matchingTitles = repo.findByTitle(d.getTitle());
        matchingTitles.forEach(document -> {
            if (document.getText().equals(d.getText())) {
                throw new IllegalStateException("Document matches an existing document");
            }
        });
        // d.setContainedInText(getWordsFromText(d.getText()));
        // d.setContainedInTitle(getWordsFromText(d.getTitle()));
        repo.save(d);
    }

    public List<Document> getAll() {
        return repo.findAll();
    }

    public Set<Word> getWordsFromText(String text) {
        return getStemsFromText(text).stream()
                .map(x -> wordService.saveWord(x))
                .collect(Collectors.toSet());
    }

    public Set<String> getStemsFromText(String text) {
        return Arrays.stream(
                text
                        // normalize data, restrict to chars and spaces
                        .toLowerCase()
                        .chars()
                        .filter(x -> keep.contains((char) x))
                        .mapToObj(x -> "" + (char) x)
                        .collect(Collectors.joining())
                        // split into words
                        .split(" "))
                // remove stopwords
                .filter(x -> !Stopwords.get().contains(x))
                // stem word, save as a Word.
                .map(x -> stemmer.stemWord((String) x))
                // save as a Set to remove duplicates
                .collect(Collectors.toSet());
    }

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
}
