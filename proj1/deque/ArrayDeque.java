package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst = 4;
    private int nextLast = 5;

    public ArrayDeque() {
        items = (T[]) new Object[8]; //Original length is 8.
        size = 0;
    }
    public ArrayDeque(T x) {
        items = (T[]) new Object[8];
        items[5] = x;
        nextLast += 1;
        size = 1;
    }

    @Override
    public int size() {
        return size;
    }



    /**
     * Copy the first part of items to the new container trivially,
     * and copy the second part of the loop structure to the last part
     * of new container, reassign next first to the new location.
     *
     * @param cap
     */
    private void resize(int cap) {
        T[] res = (T[]) new Object[cap];
        System.arraycopy(items, 0, res, 0, nextLast);
        int lengthOfLastPart = items.length - 1 - nextFirst;
        System.arraycopy(items, nextFirst + 1, res, res.length - lengthOfLastPart, lengthOfLastPart);
        items = res;
        nextFirst = res.length - lengthOfLastPart - 1;
    }

    private void cutSize(int cap) {
        T[] res = (T[]) new Object[cap];
        boolean nextLastInFront = false;
        if (nextFirst < nextLast) {
            int diff = nextFirst - 5 + 1; // difference between old long Array start index with new result Array.
            // new underlying array always start from 5.
            for (int i = nextFirst + 1; i < nextLast; i++) { // items stays between nextFirst and nextLast.
                if (i - diff >= res.length) {
                    res[i - diff - res.length] = items[i];
                    nextLastInFront = true;
                } else {
                    res[i - diff] = items[i]; // copy each item to res, start from default nextLast = 5.
                }
            }
            items = res;
            nextFirst = 4;
            if (nextLastInFront) {
                nextLast = this.size() + nextFirst - items.length + 1;
            } else {
                nextLast = this.size() + nextFirst + 1;
            }
        } else {
            return;
            //throw new RuntimeException("just assuming this won't happen");
        }
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
        //if (size < items.length / 2 && size >= 7) {
        //    cutSize(items.length / 2);
        //}

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
        //if (size < items.length / 2 && size >= 7) {
        //    cutSize(items.length / 2);
        //}
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
}
