

public final class Leaf extends Node {

    public final int symbol;  // Always non-negative



    public Leaf(int sym) {
        if (sym < 0)
            throw new IllegalArgumentException("Symbol value must be non-negative");
        symbol = sym;
    }

}