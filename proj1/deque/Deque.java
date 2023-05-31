
package deque;

public interface Deque<Loch> {

    default boolean isEmpty() {
        return size() == 0;
    }

    void addFirst(Loch item);

    void addLast(Loch item);

    int size();

    void printDeque();

    Loch removeFirst();

    Loch removeLast();

    Loch get(int index);
}
