package me.zombie_striker.gsl.data;

public class Pair<K,V> {

    private K first;
    private V second;

    public Pair(K k, V v){
        this.first = k;
        this.second = v;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}
