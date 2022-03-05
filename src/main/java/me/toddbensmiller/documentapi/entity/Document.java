package me.toddbensmiller.documentapi.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@EnableAutoConfiguration
@Table(name = "docs")
public @Data class Document {
    public Document() {
        containedInText = new HashSet<>();
        containedInTitle = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqDocGen")
    @SequenceGenerator(name = "seqDocGen", sequenceName = "seqDoc", initialValue = 1)
    @EqualsAndHashCode.Exclude
    private Long id;
    @NonNull
    @Column(columnDefinition = "TEXT")
    private String title;
    @NonNull
    @Column(columnDefinition = "TEXT")
    private String text;
    @NonNull
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "title_words", joinColumns = @JoinColumn(name = "doc_id"), inverseJoinColumns = @JoinColumn(name = "word_id"))
    private Set<Word> containedInTitle;
    @NonNull
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "text_words", joinColumns = @JoinColumn(name = "doc_id"), inverseJoinColumns = @JoinColumn(name = "word_id"))
    private Set<Word> containedInText;
}
