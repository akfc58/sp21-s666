package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void equalsTest() {
        LinkedListDeque<String> t2 = new LinkedListDeque<>();
        ArrayDeque<String> t1 = new ArrayDeque<>();
        t1.addFirst("last");
        t1.addFirst("middle");
        t1.addFirst("first");
        t2.addFirst("last");
        t2.addFirst("middle");
        t2.addFirst("first");
        assertEquals(t1, t2);
    }

    @Test
    /** Test for array shrink. */
    public void shrinkTest() {
        ArrayDeque<Integer> t = new ArrayDeque<>();
        for (int i = 0; i <= 20; i++) {
            t.addLast(i);
        }
        t.printDeque();
        for (int i = 0; i <= 20; i++) {
            t.removeFirst();
            t.removeLast();
        }
        t.printDeque();
    }

    /** A big size random test for almost all operations in AD. */
    @Test
    public void randomizedArrayDequeTest() {
        ArrayDeque<Integer> LLD1 = new ArrayDeque<>();
        ArrayDeque<Integer> LLD2 = new ArrayDeque<>();
        int N = 5000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = 666;
                LLD1.addLast(randVal);
                LLD2.addLast(randVal);
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = 666;
                LLD1.addFirst(randVal);
                LLD2.addFirst(randVal);
            } else if (operationNumber == 2) {
                // size
                int size1 = LLD1.size();
                int size2 = LLD2.size();
                assertEquals(size1, size2);
            } else if (operationNumber == 3) {
                // removeLast
                if (LLD1.size() > 0) {
                    assertEquals(LLD1.removeLast(), LLD2.removeLast());
                }
            } else if (operationNumber == 4) {
                // removeFirst
                if (LLD1.size() > 0) {
                    assertEquals(LLD1.removeFirst(), LLD2.removeFirst());
                }
            } else if (operationNumber == 5) {
                // get
                if (LLD1.size() > 0) {
                    int index = StdRandom.uniform(0, LLD1.size());
                    assertEquals(LLD1.get(index), LLD2.get(index));
                }
            }
        }
    }
}
