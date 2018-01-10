package com.gmail.niopullus.lib.niodatatree;

/**
 * Created by Owen on 3/21/2017.
 */
public class Test {

    public static void main(final String[] args) {
        final DataTree data1 = new DataTree();
        final TestType test = new TestType();
        test.value = 29;
        data1.add(5);
        data1.add(false);
        data1.add(3.14);
        data1.add("e is 2.17...");
        data1.addFolder();
        data1.add("frick frack", 4);
        data1.add(true, 4);
        data1.add(2.2222223, 4);
        data1.addNull();
        data1.add("Trick: []()][9]0[99]][09]");
        data1.add(2);
        data1.add(test, 4);
        final String stringData = data1.compress();
        final DataTree data2 = DataTree.decompress(stringData);
        final DataTree branch = data2.getBranch(5);
        System.out.println(branch);
    }

    public static class TestType implements Crushable {

        public int value;

        public DataTree crush() {
            final DataTree result = new DataTree();
            result.add(value);
            return result;
        }

        public static TestType uncrush(final DataTree data) {
            final TestType result = new TestType();
            result.value = data.getI(0);
            return result;
        }

    }

}
