package deque;

import java.util.Comparator;

public class MaxArrayDeque<Loch> extends ArrayDeque<Loch> {
    private Comparator<Loch> nc;

    public MaxArrayDeque(Comparator<Loch> c) {
        nc = c;
    }

    public MaxArrayDeque() {

    }

    public Loch max() {
        return max(nc);
    }

    public Loch max(Comparator<Loch> c) {
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


