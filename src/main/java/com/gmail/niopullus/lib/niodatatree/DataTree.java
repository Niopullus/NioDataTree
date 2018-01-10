package com.gmail.niopullus.lib.niodatatree;

import java.util.ArrayList;
import java.util.List;

/**Handles Integers, Doubles, Strings and Booleans
 * Used to transform a network of data into a text file
 * Created by Owen on 4/12/2016.
 */
public class DataTree implements Crushable {

    private Folder data;

    public DataTree() {
        super();
        data = new Folder();
    }

    private Object get(final DataPath path, final Folder folder) {
        if (path.count() == 0) {
            return data;
        } else if (path.count() == 1) {
            return folder.get(path.get());
        } else {
            final int folderDir = path.get();
            return get(path, (Folder) folder.get(folderDir));
        }
    }

    public Object get(final int... pathcontent) {
        final DataPath path = new DataPath(pathcontent);
        Object object = get(path, data);
        if (object instanceof Folder) {
            final Folder folder = (Folder) object;
            object = convertFoldersToLists(folder);
        }
        return object;
    }

    public List<Object> get() {
        return convertFoldersToLists(data);
    }

    private Folder getFolder() {
        return data;
    }

    public Integer getI(final int... pathContent) {
        final Object object = get(pathContent);
        if (object != null && !(object instanceof Integer)) {
            throw new DataTreeException("Expected Integer when extracting data, received " + object.getClass());
        }
        return (Integer) object;
    }

    public Double getD(final int... pathContent) {
        final Object object = get(pathContent);
        if (object != null && !(object instanceof Double)) {
            throw new DataTreeException("Expected Double when extracting data, received " + object.getClass());
        }
        return (Double) object;
    }

    public String getS(final int... pathContent) {
        final Object object = get(pathContent);
        if (object != null && !(object instanceof String)) {
            throw new DataTreeException("Expected String when extracting data, received " + object.getClass());
        }
        return (String) object;
    }

    public Boolean getB(final int... pathContent) {
        final Object object = get(pathContent);
        if (object != null && !(object instanceof Boolean)) {
            throw new DataTreeException("Expected Boolean when extracting data, received " + object.getClass());
        }
        return (Boolean) object;
    }

    public List<Object> getF(final int... pathContent) {
        final Object object = get(pathContent);
        if (object != null && !(object instanceof List)) {
            throw new DataTreeException("Expected List when extracting data, received " + object.getClass());
        }
        return (List<Object>) object;
    }

    private Folder getFolder(final int... pathContent) {
        final Object object = get(pathContent);
        return (Folder) object;
    }

    public DataTree getBranch(final int... pathContent) {
        final DataPath path = new DataPath(pathContent);
        final Folder folderContents = (Folder) get(path, data);
        if (folderContents != null) {
            final DataTree data = new DataTree();
            data.data = folderContents;
            return data;
        }
        return null;
    }

    private int getSize(final DataPath path, final Folder folder) {
        if (path.count() == 0) {
            return folder.count();
        } else {
            int folderDir = path.get();
            if (!(folder.get(folderDir) instanceof Folder)) {
                throw new DataTreeException("Failed to navigate using given path");
            }
            return getSize(path, (Folder) folder.get(folderDir));
        }
    }

    public int getSize(final int... pathcontents) {
        final DataPath path = new DataPath(pathcontents);
        return getSize(path, data);
    }

    public int getSize() {
        return data.count();
    }

    public boolean isFolder(final int... pathContent) {
        final DataPath path = new DataPath(pathContent);
        final Object object = get(path, data);
        return object instanceof Folder;
    }

    public String toString() {
        return data.toString();
    }

    public void set(final Crushable crushable) {
        final DataTree data = crushable.crush();
        this.data = data.getFolder();
    }

    private int add(final Object object, final DataPath path, final Folder folder) {
        if (path.count() == 0) {
            folder.add(object);
            return folder.count() - 1;
        } else {
            final int folderDir = path.get();
            if (!(folder.get(folderDir) instanceof Folder)) {
                throw new DataTreeException("Failed to navigate using given path");
            }
            return add(object, path, (Folder) folder.get(folderDir));
        }
    }

    public int add(final Integer i, final int... pathContent) {
        final DataPath path = new DataPath(pathContent);
        return add(i, path, data);
    }

    public int add(final Integer i) {
        data.add(i);
        return data.count() - 1;
    }

    public int add(final Double d, final int... pathContent) {
        final DataPath path = new DataPath(pathContent);
        return add(d, path, data);
    }

    public int add(final Double d) {
        data.add(d);
        return data.count() - 1;
    }

    public int add(final Boolean b, final int... pathContent) {
        final DataPath path = new DataPath(pathContent);
        return add(b, path, data);
    }

    public int add(final Boolean b) {
        data.add(b);
        return data.count() - 1;
    }

    public int add(final String s, final int... pathContent) {
        final DataPath path = new DataPath(pathContent);
        return add(s, path, data);
    }

    public int add(final String s) {
        data.add(s);
        return data.count() - 1;
    }

    public int addNull(final int... pathcontent) {
        final DataPath path = new DataPath(pathcontent);
        return add(null, path, data);
    }

    public int addNull() {
        data.add(null);
        return data.count() - 1;
    }

    public int add(final List<? extends Crushable> f, final int... pathcontent) {
        final DataPath path = new DataPath(pathcontent);
        if (f != null) {
            final Folder folder = new Folder();
            for (Crushable object : f) {
                final DataTree subData = object.crush();
                folder.add(subData.get());
            }
            return add(folder, path, data);
        } else {
            return add(null, path, data);
        }
    }

    public int add(final List<? extends Crushable> f) {
        if (f != null) {
            final Folder folder = new Folder();
            for (Crushable object : f) {
                final DataTree subData = object.crush();
                folder.add(subData.data);
            }
            data.add(folder);
        } else {
            data.add(null);
        }
        return data.count() - 1;
    }

    public int addFolder(final int... pathcontent) {
        final DataPath path = new DataPath(pathcontent);
        return add(new Folder(), path, data);
    }

    public int addFolder() {
        data.add(new Folder());
        return data.count() - 1;
    }

    public int add(final Crushable object, final int... pathcontent) {
        final DataTree data = object.crush();
        final Folder folder = data.data;
        final DataPath path = new DataPath(pathcontent);
        return add(folder, path, this.data);
    }

    public int add(final Crushable object) {
        final DataTree data = object.crush();
        final Folder folder = data.data;
        this.data.add(folder);
        return this.data.count() - 1;
    }

    private String compress(final Folder folder) {
        String data = "";
        for (Object o : folder.getContents()) {
            String stringValue = "";
            String lengthPart;
            char type = 'i';
            if (o == null) {
                type = 'n';
            } else if (o instanceof Folder) {
                final Folder innerFolder = (Folder) o;
                type = 'f';
                stringValue = compress(innerFolder);
            } else if (o instanceof Double) {
                type = 'd';
                stringValue = o.toString();
            } else if (o instanceof Boolean) {
                type = 'b';
                stringValue = compressBoolean((boolean) o);
            } else if (o instanceof String) {
                type = 's';
                stringValue = o.toString();
            } else if (o instanceof Integer) {
                type = 'i';
                stringValue = o.toString();
            }
            lengthPart = "(" + stringValue.length() + ")";
            data += type + lengthPart + "[" + stringValue + "]";
        }
        return data;
    }

    public String compress() {
        return compress(data);
    }

    private Folder copy(final Folder data) {
        final Folder copy = new Folder();
        for (Object o : data.getContents()) {
            if (o instanceof List) {
                copy.add(copy((Folder) o));
            } else {
                copy.add(o);
            }
        }
        return copy;
    }

    public DataTree copy() {
        final Folder folder = copy(data);
        final DataTree result = new DataTree();
        result.data = folder;
        return result;
    }

    private List<Object> convertFoldersToLists(final Folder folder) {
        final List<Object> result = new ArrayList<>();
        final List<Object> folderContents = folder.getContents();
        for (Object object : folderContents) {
            if (object instanceof Folder) {
                final Folder item = (Folder) object;
                object = convertFoldersToLists(item);
            }
            result.add(object);
        }
        return result;
    }

    private String scan(final Folder folder) {
        String result = "";
        for (int i = 0; i < folder.count(); i++) {
            final Object object = folder.get(i);
            if (i != 0) {
                result += ",";
            }
            if (object instanceof Folder) {
                final Folder innerFolder = (Folder) object;
                result += scan(innerFolder);
            } else {
                result += object.toString();
            }
        }
        return result;
    }

    public String scan() {
        return scan(data);
    }

    public DataTree crush() {
        return this;
    }

    private static String compressBoolean(final boolean b) {
        return b ? "1" : "0";
    }

    private static Boolean decompressBoolean(final String line) {
        if (line == null || !(line.equals("0") || line.equals("1"))) {
            throw new DataTreeException("Failed to decompress boolean");
        }
        return line.equals("1");
    }

    public static DataTree decompress(final String input) {
        final Decompression decompression = new Decompression(input);
        return decompression.getResult();
    }

    public static class Decompression {

        private Folder result;
        private String lineData;

        public Decompression(final String lineData) {
            super();
            this.lineData = lineData;
            result = new Folder();
            executeDecompression();
        }

        public DataTree getResult() {
            final DataTree dataTreeResult = new DataTree();
            dataTreeResult.data = result;
            return dataTreeResult;
        }

        private void executeDecompression() {
            final int count = lineData.length();
            int index = 0;
            while (index < count) {
                final DataType dataType = getDataType(index);
                final int leftParenthesisIndex = index + 1;
                final int dataLength;
                final String dataLengthString;
                final Object object;
                int rightParenthesisIndex = leftParenthesisIndex;
                while (true) {
                    final char c = lineData.charAt(rightParenthesisIndex);
                    if (c == ')') {
                        break;
                    }
                    rightParenthesisIndex += 1;
                }
                dataLengthString = lineData.substring(leftParenthesisIndex + 1, rightParenthesisIndex);
                dataLength = Integer.parseInt(dataLengthString);
                index = rightParenthesisIndex + 2;
                object = extractData(index, dataLength, dataType);
                result.add(object);
                index += dataLength + 1;
            }
        }

        private DataType getDataType(final int index) {
            final char c = lineData.charAt(index);
            DataType result = null;
            switch (c) {
                case 'i': result = DataType.INTEGER; break;
                case 'd': result = DataType.DOUBLE; break;
                case 's': result = DataType.STRING; break;
                case 'b': result = DataType.BOOLEAN; break;
                case 'f': result = DataType.FOLDER; break;
                case 'n': result = DataType.NULL; break;
            }
            return result;
        }

        private Object extractData(final int index, final int dataLength, final DataType dataType) {
            final String dataString = lineData.substring(index, index + dataLength);
            Object result = null;
            if (dataType == DataType.FOLDER) {
                final Decompression decompression = new Decompression(dataString);
                result = decompression.result;
            } else if (dataType == DataType.NULL) {
                result = null;
            } else if (dataType == DataType.INTEGER) {
                result = Integer.parseInt(dataString);
            } else if (dataType == DataType.DOUBLE) {
                result = Double.parseDouble(dataString);
            } else if (dataType == DataType.BOOLEAN) {
                result = DataTree.decompressBoolean(dataString);
            } else if (dataType == DataType.STRING) {
                result = dataString;
            }
            return result;
        }

    }

    public enum DataType {

        INTEGER,
        DOUBLE,
        STRING,
        BOOLEAN,
        FOLDER,
        NULL

    }

    public static class DataTreeException extends RuntimeException {

        public DataTreeException(final String message) {
            super(message);
        }

    }

}