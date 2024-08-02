package net.deepacat.createpowerlines.blocks.connector.types;

public enum ConnectorStyle {
    SMALL("small", 5, flatten(rect(0, 0, 4, 4), rect(5, 9, 2, 2), rect(1, 13, 2, 2), rect(5, 13, 2, 2))),
    LARGE("large", 7, flatten(rect(0, 0, 4, 5), rect(5, 5, 4, 4), rect(11, 5, 4, 4), rect(5, 11, 4, 4), rect(11, 11, 4, 4)));

    public static final int SIZE = 16;

    ConnectorStyle(String name, int baseHeight, int[] indices) {
        this.name = name;
        this.baseHeight = baseHeight;
        this.indices = indices;
    }

    public final String name;
    public final int baseHeight;
    public final int[] indices;

    static private int[] rect(int x, int y, int w, int h) {
        int[] result = new int[w * h];
        int idx = 0;
        for (int j = 0; j != h; ++j) {
            for (int i = 0; i != w; ++i) {
                result[idx++] = (y + j) * SIZE + x + i;
            }
        }
        return result;
    }

    static private int[] flatten(int[]... nested) {
        int totalLen = 0;
        for (int[] xs : nested) {
            totalLen += xs.length;
        }
        int[] result = new int[totalLen];
        int idx = 0;
        for (int[] xs : nested) {
            for (int x : xs) {
                result[idx++] = x;
            }
        }
        return result;
    }
}
