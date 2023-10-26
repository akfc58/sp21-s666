package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> nc;

    public MaxArrayDeque(Comparator<T> c) {
        nc = c;
    }

    public T max() {
        return max(nc);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        int maxindex = 0;
        for (int index = 0; index < size(); index++) {
            if (c.compare(get(index), get(maxindex)) > 0) {
                maxindex = index;
            }
        }
        return get(maxindex);
    }
}


