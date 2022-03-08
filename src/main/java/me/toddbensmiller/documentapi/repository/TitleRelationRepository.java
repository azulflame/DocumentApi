package me.toddbensmiller.documentapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.toddbensmiller.documentapi.entity.TitleRelation;

public interface TitleRelationRepository extends JpaRepository<TitleRelation, Long> {
}