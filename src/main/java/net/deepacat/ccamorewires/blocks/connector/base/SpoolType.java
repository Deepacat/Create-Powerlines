package net.deepacat.ccamorewires.blocks.connector.base;

import net.deepacat.ccamorewires.energy.WireType;

public enum SpoolType {
    COPPER("COPPER"),
    GOLD("GOLD"),
    ELECTRUM("ELECTRUM");

    public final String name;

    SpoolType(String name) {
        this.name = name;
    }
}
