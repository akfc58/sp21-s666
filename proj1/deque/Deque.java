package deque;

public interface Deque<Loch> {

    default public boolean isEmpty() {
        return size() == 0;
    }
    public void addFirst(Loch item);
    public void addLast(Loch item);
    public int size();
    public void printDeque();
    public Loch removeFirst();
    public Loch removeLast();
    public Loch get(int index);
}