package me.toddbensmiller.documentapi.csvsaver;

public interface CsvSavable {
    public String toCsvLine();

    public String getSqlString();

    public void setId(Long id);
}
