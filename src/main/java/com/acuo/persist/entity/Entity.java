package com.acuo.persist.entity;

public interface Entity<T extends Entity> {

    Long getId();
    /*@Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || id == null || getClass() != o.getClass()) return false;

        T entity = (T) o;

        return id.equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return (id == null) ? -1 : id.hashCode();
    }*/
}