package me.toddbensmiller.documentapi.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EnableAutoConfiguration
@Table(name = "words")
public @Data class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqDocGen")
    @SequenceGenerator(name = "seqDocGen", sequenceName = "seqDoc", initialValue = 1)
    @EqualsAndHashCode.Exclude
    private Long id;
    @NonNull
    @Column(columnDefinition = "TEXT")
    private String stem;
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "containedInTitle")
    private Set<Document> titles;
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "containedInText")
    private Set<Document> texts;
}