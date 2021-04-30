package helper;

public class OneSet<T> implements ItemSet<T> {
    public T first;

    public OneSet(T first){
        this.first = first;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        return first.equals(((OneSet<T>) obj).first);
    }

    @Override
    public int hashCode() {
        return first.hashCode();
    }
}