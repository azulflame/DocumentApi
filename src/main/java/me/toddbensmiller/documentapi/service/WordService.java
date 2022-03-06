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

    /**
     * Get a word in the database by it's ID. Throws an exception if no matching
     * word is found
     * 
     * @param id
     * @return the word found
     * @throws IllegalStateException if there is no word with that ID in the
     *                               database
     */
    public Word getWord(Long id) {
        Optional<Word> w = repo.findById(id);
        if (w.isPresent()) {
            return w.get();
        }
        throw new IllegalStateException("Word with id " + id + " not found");
    }

    /**
     * Save a batch of words at a time
     * 
     * @param words A Set of words to save
     * @return a List of words saved
     */
    public List<Word> saveAll(Set<Word> words) {
        return repo.saveAll(words);
    }

    /**
     * Save a word in the database, returning the saved word, or the word in the
     * database if one already exists
     * 
     * @param word a Word to save
     * @return the Word object stored in the database
     */
    public Word saveWord(Word word) {
        if (word == null || word.getStem().length() == 0) {
            return null;
        }
        Optional<Word> stemMatch = repo.findByStem(word.getStem());
        if (stemMatch.isPresent()) {
            return stemMatch.get();
        }
        return repo.save(word);
    }

    /**
     * Save a word in the database, returning the saved word, or the word in the
     * database if one already exists
     * 
     * @param stem a stem to create a Word from, then save
     * @return the Word object stored in the database
     */
    public Word saveWord(String stem) {
        return saveWord(new Word(stem));
    }
}
