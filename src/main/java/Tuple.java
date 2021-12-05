public class Tuple<T, S> {
    private T first;
    private S second;

    public Tuple(T x, S y){
        this.first = x;
        this.second = y;
    }

    public T getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
