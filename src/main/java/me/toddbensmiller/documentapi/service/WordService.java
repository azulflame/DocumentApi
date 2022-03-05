package me.toddbensmiller.documentapi.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.toddbensmiller.documentapi.entity.Word;
import me.toddbensmiller.documentapi.repository.WordRepository;

@Service
public class WordService {
    private WordRepository repo;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.repo = wordRepository;
    }

    public Word getWord(Long id) {
        Optional<Word> w = repo.findById(id);
        if (w.isPresent()) {
            return w.get();
        }
        throw new IllegalStateException("Word with id " + id + " not found");
    }

    public List<Word> getAll() {
        return repo.findAll();
    }

    public List<Word> saveAll(Set<Word> words) {
        return repo.saveAll(words);
    }

    public Word saveWord(Word w) {
        if (w == null || w.getStem().length() == 0) {
            return null;
        }
        Optional<Word> stemMatch = repo.findByStem(w.getStem());
        if (stemMatch.isPresent()) {
            return stemMatch.get();
        }
        return repo.save(w);
    }

    public Word saveWord(String stem) {
        return saveWord(new Word(stem));
    }
}
