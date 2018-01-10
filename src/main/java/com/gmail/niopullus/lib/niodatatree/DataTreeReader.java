package com.gmail.niopullus.lib.niodatatree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 11/4/2016.
 */
public class DataTreeReader {

    private int index;
    private DataTree data;
    private DataTreeReader subReader;
    private DataTreeReader superReader;
    private List<Integer> superDir;

    DataTreeReader(final DataTree data, final List<Integer> superDir) {
        super();
        index = 0;
        this.data = data;
        subReader = null;
        this.superDir = superDir;
    }

    public DataTreeReader(final DataTree data) {
        this(data, new ArrayList<>());
    }

    public void setSuperReader(final DataTreeReader reader) {
        superReader = reader;
    }

    private void incrementIndex() {
        index += 1;
    }

    private List<Integer> createSuperDir() {
        final List<Integer> result = new ArrayList<>();
        result.addAll(superDir);
        result.add(index);
        return result;
    }

    private int[] getPath() {
        final int[] result = new int[superDir.size() + 1];
        for (int i = 0; i < superDir.size(); i++) {
            result[i] = superDir.get(i);
        }
        result[superDir.size()] = index;
        return result;
    }

    public Object read() {
        final int[] path = getPath();
        final int[] trimmedPath = trimPath(path);
        final int size = data.getSize(trimmedPath);
        if (index < size) {
            if (subReader == null) {
                incrementIndex();
                return data.get(path);
            } else {
                return subReader.read();
            }
        } else {
            return null;
        }
    }

    public void closeSubReader() {
        subReader = null;
    }

    private int[] trimPath(final int[] path) {
        final int[] result = new int[path.length - 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = path[i];
        }
        return result;
    }

    public Object traverse() {
        final int[] path = getPath();
        final int[] trimmedPath = trimPath(path);
        final int size = data.getSize(trimmedPath);
        if (index < size) {
            if (subReader == null) {
                final Object newData;
                newData = data.get(path);
                if (newData instanceof List) {
                    enterFolder();
                    return traverse();
                } else {
                    incrementIndex();
                    return newData;
                }
            } else {
                return subReader.traverse();
            }
        } else {
            if (superReader != null) {
                superReader.closeSubReader();
                superReader.incrementIndex();
                return superReader.traverse();
            }
            return null;
        }
    }

    public int readI() {
        return (int) read();
    }

    public double readD() {
        return (double) read();
    }

    public String readS() {
        return (String) read();
    }

    public boolean readB() {
        return (boolean) read();
    }

    public void enterFolder() {
        final List<Integer> createdDir = createSuperDir();
        subReader = new DataTreeReader(data, createdDir);
        subReader.setSuperReader(this);
    }

    public void exitFolder() {
        subReader = null;
    }

    public void skip() {
        incrementIndex();
    }

    public void skip(final int quant) {
        for (int i = 0; i < quant; i++) {
            skip();
        }
    }

}
