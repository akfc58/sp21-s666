package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst = 4;
    private int nextLast = 5;

    public ArrayDeque() {
        items = (T[]) new Object[8]; //Original length is 8.
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }



    /**
     * Copy the first part of items to the new container trivially,
     * and copy the second part of the loop structure to the last part
     * of new container, reassign next first to the new location.
     */
    private void resize(int cap) {
        T[] res = (T[]) new Object[cap];
        System.arraycopy(items, 0, res, 0, nextLast);
        int lengthOfLastPart = items.length - 1 - nextFirst;
        System.arraycopy(items, nextFirst + 1, res,
                res.length - lengthOfLastPart, lengthOfLastPart);
        items = res;
        nextFirst = res.length - lengthOfLastPart - 1;
    }

    private void shrink(int cap) {
        T[] res = (T[]) new Object[cap];
        int start = 5;
        for (T each : this) {
            if (start == cap) {
                start = 0;
            }
            res[start] = each;
            start += 1;
        }
        nextFirst = 4;
        nextLast = start;
        items = res;
    }

    @Override
    public void addFirst(T x) {
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }
        if (nextFirst == 0) {
            items[nextFirst] = x;
            nextFirst = items.length - 1;
        } else {
            items[nextFirst] = x;
            nextFirst -= 1;
        }
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (nextFirst == nextLast) {
            resize(items.length * 2);
        }
        if (nextLast == items.length - 1) {
            items[nextLast] = x;
            nextLast = 0;
        } else {
            items[nextLast] = x;
            nextLast += 1;
        }
        size += 1;
    }

    @Override
    public T removeLast() {
        if (size < items.length / 2 && size >= 7) {
            shrink(items.length / 2 + 1);
        }

        if (isEmpty()) {
            return null;
        }

        if (nextLast == 0) {
            nextLast = items.length - 1;
        } else {
            nextLast = nextLast - 1;
        }
        T removed = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        // return the removed last value where nextLast is pointing at after remove.
        return removed;
    }

    @Override
    public T removeFirst() {
        if (size < items.length / 2 && size >= 7) {
            shrink(items.length / 2 + 1);
        }
        if (isEmpty()) {
            return null;
        }
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst += 1;
        }
        size -= 1;
        T removed = items[nextFirst];
        items[nextFirst] = null;
        return removed;
    }

    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }
        int indexInItems = nextFirst + 1 + index;
        if (indexInItems < items.length) {
            return items[indexInItems];
        } else {
            return items[indexInItems - items.length];
        }
    }

    @Override
    public void printDeque() {
        if (nextLast > nextFirst) {
            for (int index = nextFirst + 1; index < nextLast; index += 1) {
                System.out.print(items[index] + " ");
            }
            System.out.println();
        } else {
            for (int index = nextFirst + 1; index < items.length; index += 1) {
                System.out.print(items[index] + " ");
            }
            for (int index = 0; index < nextLast; index += 1) {
                System.out.print(items[index] + " ");
            }
            System.out.println();
        }
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        int wisPos;

        ArrayDequeIterator() {
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
            return wisPos < size;
        }
    }

    private boolean contains(T x) {
        for (int index = 0; index < size; index++) {
            if (get(index).equals(x)) {  // must use equals.
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // for efficiency
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Deque)) {
            return false;
        }
        Deque<T> o = (Deque<T>) obj;
        if (o.size() != this.size()) {
            return false;
        }
        for (int index = 0; index < o.size(); index++) {
            if (!(this.contains(o.get(index)))) {
                return false;
            }
        }
        return true;
    }
}
