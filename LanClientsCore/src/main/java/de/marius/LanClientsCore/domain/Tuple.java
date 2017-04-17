package de.marius.LanClientsCore.domain;

/**
 * A utility class to hold two values of any type.
 *
 * @param <K>
 * @param <V>
 */
public class Tuple<K, V> {
    private K x;
    private V y;

    public Tuple(K x, V y) {
        this.x = x;
        this.y = y;
    }

    public V getY() {
        return y;
    }

    public K getX() {
        return x;
    }

    @Override
    public String toString() {
        return "<" + x.toString() + ", " + y.toString() + ">";
    }

}