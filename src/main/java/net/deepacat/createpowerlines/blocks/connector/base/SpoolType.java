package net.deepacat.createpowerlines.blocks.connector.base;

public enum SpoolType {
    COPPER("COPPER"),
    GOLD("GOLD"),
    ELECTRUM("ELECTRUM");

    public final String name;

    SpoolType(String name) {
        this.name = name;
    }
}
