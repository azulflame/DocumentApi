package me.toddbensmiller.documentapi.util;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

public class EntityCache<T> {

    private int batchSize;
    private ArrayList<T> list;
    private JpaRepository<T, Long> repo;

    public EntityCache(JpaRepository<T, Long> repo, int batchSize) {
        this.repo = repo;
        list = new ArrayList<T>(batchSize);
        this.batchSize = batchSize;
    }

    public void add(T item) {
        list.add(item);
        if (list.size() >= batchSize) {
            sync();
        }
    }

    public void sync() {
        repo.saveAll(list);
        clear();
    }

    public void clear() {
        list.clear();
        list.ensureCapacity(batchSize);
    }

}
