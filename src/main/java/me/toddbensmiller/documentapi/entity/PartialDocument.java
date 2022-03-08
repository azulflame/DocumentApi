package me.toddbensmiller.documentapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class PartialDocument {
    private Long id;
    private String title;

    public PartialDocument(Object[] objects) {
        this.id = (Long) objects[0];
        this.title = (String) objects[1];
    }
}
