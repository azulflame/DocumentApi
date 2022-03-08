package me.toddbensmiller.documentapi.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.toddbensmiller.documentapi.entity.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
    public Optional<Word> findByStem(String stem);

    @Query("SELECT w FROM Word w WHERE w.stem in (:stems)")
    public List<Word> findAllByStem(Set<String> stems);
}
