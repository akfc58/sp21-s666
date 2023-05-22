package deque;

public class ArrayDeque<Loch> {
    private Loch[] items;
    private int size;
    private int nextFirst = 4;
    private int nextLast = 5;

    public ArrayDeque() {
        items = (Loch[]) new Object[8]; //Original length is 8.
        size = 0;
    }
    public ArrayDeque(Loch x) {
        items = (Loch[]) new Object[8];
        items[5] = x;
        nextLast += 1;
        size = 1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public void addFirst(Loch x) {
        if (nextFirst == 0) {
            items[nextFirst] = x;
            nextFirst = items.length - 1;
        } else {
            items[nextFirst] = x;
            nextFirst -= 1;
        }
        size += 1;
    }

    public void addLast(Loch x) {
        if (nextLast == items.length - 1) {
            items[nextLast] = x;
            nextLast = 0;
        }else {
            items[nextLast] = x;
            nextLast += 1;
        }
        size += 1;
    }

    public Loch removeLast() {
        if (isEmpty()){
            return null;
        }
        if (nextLast == 0) {
            nextLast = items.length - 1;
        } else {
            nextLast = nextLast - 1;
        }
        size -= 1;
        // return the removed last value where nextLast is pointing at after remove.
        Loch value = items[nextLast];
        return value;
    }

    public Loch removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst += 1;
        }
        size -= 1;
        Loch value = items[nextFirst];
        return value;
    }

    public Loch get(int index) {
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
}
