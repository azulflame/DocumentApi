package me.toddbensmiller.documentapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.toddbensmiller.documentapi.csvsaver.CsvSavable;

@Entity
@NoArgsConstructor
public @Data class TextRelation implements CsvSavable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTextGen")
    @SequenceGenerator(name = "seqTextGen", sequenceName = "seqText", initialValue = 1)
    @EqualsAndHashCode.Exclude
    private Long id;
    private Long wordId;
    private Long docId;
    @Transient
    private String stem;

    public TextRelation(Long wordId, Long docId) {
        this.wordId = wordId;
        this.docId = docId;
    }

    public TextRelation(String stem, Long docId) {
        this.stem = stem;
        this.docId = docId;
    }

    @Override
    public String toCsvLine() {
        return id + "," + wordId + "," + docId;
    }

    @Override
    public String getSqlString() {
        return "text_relation(id, word_id, doc_id)";
    }
}
