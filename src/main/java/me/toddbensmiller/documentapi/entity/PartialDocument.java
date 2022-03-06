package me.toddbensmiller.documentapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class PartialDocument {
    private Long id;
    private String title;
}
