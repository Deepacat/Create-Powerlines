package net.deepacat.ccamorewires.blocks.connector;

public enum ConnectorType {
    Small("Small"),
    Large("Large"),
    Huge("Huge"),
    Giant("Giant"),
    Massive("Massive");

    public final String name;

    ConnectorType(String name) {
        this.name = name;
    }
}
