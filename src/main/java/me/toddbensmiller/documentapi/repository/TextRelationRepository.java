package me.toddbensmiller.documentapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.toddbensmiller.documentapi.entity.TextRelation;

public interface TextRelationRepository extends JpaRepository<TextRelation, Long> {
}
