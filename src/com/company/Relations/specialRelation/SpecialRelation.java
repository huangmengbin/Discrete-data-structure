package com.company.Relations.specialRelation;

import com.company.Relations.Relation;

import java.util.Collection;

public abstract class SpecialRelation<T> {
    Collection<T> collection;
    Relation<T> relation;

    public Collection<T> getCollection() {
        return collection;
    }

    public Relation<T> getRelation() {
        return relation;
    }
}
