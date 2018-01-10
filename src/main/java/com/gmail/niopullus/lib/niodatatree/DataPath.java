package com.gmail.niopullus.lib.niodatatree;

import java.util.ArrayList;
import java.util.List;

/**Used to keep track of directory paths to be entered
 * Created by Owen on 4/13/2016.
 */
public class DataPath {

    private List<Integer> path;

    public DataPath(final int... path) {
        super();
        this.path = new ArrayList<>();
        for (int i : path) {
            this.path.add(i);
        }
    }

    public DataPath() {
        path = null;
    }

    public int get() {
        final int firstPath = path.get(0);
        path.remove(0);
        return firstPath;
    }

    public int oget(final int index) {
        return path.get(index);
    }

    public int count() {
        if (path == null) {
            return 0;
        } else {
            return path.size();
        }
    }

}
