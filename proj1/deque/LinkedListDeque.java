package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class StuffNode {
        private T item;
        private StuffNode prev;
        private StuffNode next;

        StuffNode(T i, StuffNode p, StuffNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private StuffNode sentinel;
    private int size;

    /** Creates a EMPTY Deque. */
    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null); // How to fill in sentinel's item?
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        StuffNode temp = new StuffNode(x, null, null);
        temp.prev = sentinel;
        temp.next = sentinel.next;
        sentinel.next.prev = temp;
        sentinel.next = temp;
        size += 1;
    }

    @Override
    public void addLast(T x) {
        StuffNode temp = new StuffNode(x, null, null);
        temp.prev = sentinel.prev;
        temp.next = sentinel;
        sentinel.prev.next = temp;
        sentinel.prev = temp;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        T value = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return value;
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        T value = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return value;
    }

    @Override
    public void printDeque() {
        StuffNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println(); // Print a blank line after the deque.
    }

    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }
        StuffNode p = sentinel;
        for (int i = 0; i <= index; i += 1) {
            p = p.next;
        }
        return p.item;
    }

    private T getRecursiveHelper(int index, StuffNode p, int i) {
        if (i == index) {
            return p.item;
        }
        return getRecursiveHelper(index, p.next, i + 1);
    }

    public T getRecursive(int index) {
        if (isEmpty()) {
            return null;
        }
        return getRecursiveHelper(index, sentinel.next, 0);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        int wisPos;

        LinkedListDequeIterator() {
            wisPos = 0;
        }
        @Override
        public T next() {
            T returnItem = get(wisPos);
            wisPos += 1;
            return returnItem;
        }

        @Override
        public boolean hasNext() {
            if (wisPos < size) {
                return true;
            }
            return false;
        }
    }

    public boolean contains(T x) {
        for (int index = 0; index < size; index++) {
            if (get(index).equals(x)) {  // must use equals.
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // for efficiency
        }
        if (obj == null) {
            return false;
        }
        if (!((obj instanceof Deque) && (this instanceof Deque))) {
            return false;
        }
        Deque<T> o = (Deque<T>) obj;
        if (o.size() != this.size()) {
            return false;
        }
        for (T each : this) {
            if (!(o.contains(each))) {
                return false;
            }
        }
        return true;
    }
}
