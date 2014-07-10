package com.gutspot.apps.android.mytodo.model;

public class AbstractEntity {
    private Long id;

    private long version;

    public AbstractEntity() {

    }

    public AbstractEntity(long id, long version) {
        this.id = id;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

}
