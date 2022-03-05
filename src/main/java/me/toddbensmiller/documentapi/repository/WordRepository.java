package me.toddbensmiller.documentapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.toddbensmiller.documentapi.entity.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
    public Optional<Word> findByStem(String stem);
}
