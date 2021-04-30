package helper;

public class TwoSet<T> implements ItemSet<T>{
    public T first;
    public T second;

    public TwoSet(T first, T second){
        this.first = first;
        this.second = second;
    }

    public TwoSet(OneSet<T> first, OneSet<T> second){
        this.first = first.first;
        this.second = second.first;
    }

    public Boolean contains(OneSet<T> num){
        return first.equals(num.first) || second.equals(num.first);
    }

    public Boolean containsOneItem(TwoSet<T> set){
        return !this.equals(set) && (
                first.equals(set.first) ||
                        first.equals(set.second) ||
                        second.equals(set.first) ||
                        second.equals(set.second)
                );
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        TwoSet<T> two = (TwoSet<T>) obj;
        return (first.equals(two.first) && second.equals(two.second)) || (second.equals(two.first) && first.equals(two.second));
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }
}