package com.gutspot.apps.android.mytodo.model;

public abstract class AbstractEntity {
    private Long id;

    public AbstractEntity() {

    }

    public AbstractEntity(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
