package com.gmail.niopullus.lib.niodatatree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 3/19/2017.
 */
public class Folder {

    private List<Object> contents;

    public Folder(final List<Object> contents) {
        super();
        this.contents = contents;
    }

    public Folder() {
        this(new ArrayList<>());
    }

    public Object get(final int index) {
        return contents.get(index);
    }

    public List<Object> getContents() {
        return contents;
    }

    public void setContents(final List<Object> contents) {
        this.contents = contents;
    }

    public void add(final Object object) {
        contents.add(object);
    }

    public void set(final int index, final Object value) {
        contents.set(index, value);
    }

    public Object remove(final int index) {
        return contents.remove(index);
    }

    public int count() {
        return contents.size();
    }

}
