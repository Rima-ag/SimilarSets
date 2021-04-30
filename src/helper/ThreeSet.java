package helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ThreeSet<T> implements ItemSet<T>{
    public T first;
    public T second;
    public T third;

    public ThreeSet(T first, T second, T third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public ThreeSet(TwoSet<T> one, TwoSet<T> two){
        Set<T> items = new HashSet<>();
        for(TwoSet<T> set : new ArrayList<TwoSet<T>>(){{add(one); add(two);}}){
            items.add(set.first);
            items.add(set.second);
        }
        Iterator<T> it = items.iterator();
        first = it.next();
        second = it.next();
        third = it.next();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        ThreeSet<T> three=  (ThreeSet<T>) obj;
        return (first.equals(three.first) || first.equals(three.second) || first.equals(three.third)) &&
                (second.equals(three.first) || second.equals(three.second) || second.equals(three.third)) &&
                (third.equals(three.first) || third.equals(three.second) || third.equals(three.third));
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode() ^ third.hashCode();
    }

    @Override
    public String toString(){
        return first.toString() + " + " + second.toString() + " -> " + third.toString();
    }
}