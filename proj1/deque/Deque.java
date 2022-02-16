package deque;

public interface Deque<Thing>{
    void addFirst(Thing item);
    void addLast(Thing item);
    int size();
    void printDeque();
    Thing removeFirst();
    Thing removeLast();
    Thing get(int index);

    default boolean isEmpty() {
        return size() == 0;
    }
}
