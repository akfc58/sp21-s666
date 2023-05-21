package deque;

public class LinkedListDeque<Loch> {

    private class StuffNode {
        public Loch item;
        public StuffNode prev;
        public StuffNode next;

        public StuffNode(Loch i, StuffNode p, StuffNode n){
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

    /** Creates a SINGLE item Deque. */
    public LinkedListDeque(Loch x) {
        sentinel = new StuffNode(null, null, null); // How to fill in sentinel's item?
        StuffNode first = new StuffNode(x, sentinel, sentinel);
        sentinel.prev = first;
        sentinel.next = first;
        size = 1;
    }

    public void addFirst(Loch x) {
        StuffNode temp = new StuffNode(x, null, null);
        temp.prev = sentinel;
        temp.next = sentinel.next;
        sentinel.next.prev = temp;
        sentinel.next = temp;
        size += 1;
    }

    public void addLast(Loch x) {
        StuffNode temp = new StuffNode(x, null, null);
        temp.prev = sentinel.prev;
        temp.next = sentinel;
        sentinel.prev.next = temp;
        sentinel.prev = temp;
        size += 1;
    }

    public boolean isEmpty() {
        if (sentinel.next == sentinel) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public Loch removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        Loch value = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return value;
    }

    public Loch removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        Loch value = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return value;
    }

    public void printDeque() {
        StuffNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println(); // Print a blank line after the deque.
    }

    public Loch get(int index) {
        if (isEmpty()) {
            return null;
        }
        StuffNode p = sentinel;
        for (int i = 0; i <= index;i += 1) {
            p = p.next;
        }
        return p.item;
    }

    private Loch getRecursiveHelper(int index, StuffNode p, int i) {
        if (i == index){
            return p.item;
        }
        return getRecursiveHelper(index, p.next, i + 1);
    }

    public Loch getRecursive(int index) {
        if (isEmpty()) {
            return null;
        }
        return getRecursiveHelper(index, sentinel.next, 0);
    }
}