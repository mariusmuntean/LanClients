/**
 * Created by Marius on 23.01.2017.
 */


public class Tuple<K,V>{
    private K x;
    private V y;

    public Tuple(K x, V y){
        this.x = x;
        this.y = y;
    }

    public V getY() {
        return y;
    }

    public K getX() {
        return x;
    }

}