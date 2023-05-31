package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class MaxDequeTest {

    @Test
    public void intCompareTest() {
        MaxArrayDeque<Integer> t1 = new MaxArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            t1.addLast(StdRandom.uniform(0, 60));
            t1.addLast(62);
        }
        t1.printDeque();
        assertEquals((Integer) 62, t1.max(new IntComparator()));
    }

    @Test
    public void intCompareTest2() {
        MaxArrayDeque<Integer> t1 = new MaxArrayDeque<>(new IntComparator());
        for (int i = 0; i < 10; i++) {
            t1.addLast(StdRandom.uniform(0, 60));
            t1.addLast(62);
        }
        t1.printDeque();
        assertEquals((Integer) 62, t1.max());
    }

    @Test
    public void StringCompareTest() {
        MaxArrayDeque<String> s1 = new MaxArrayDeque<>();
        Comparator<String> comparator = new StringComparator();
        s1.addLast("df");
        s1.addLast("ckkk");
        s1.addLast("rrww");
        s1.addLast("xx");
        s1.addLast("xa");
        assertEquals("xx", s1.max(comparator));
    }

    public class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    }
}
