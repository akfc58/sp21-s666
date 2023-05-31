package deque;

import org.junit.Test;

import static org.junit.Assert.*;


/** Performs some linked list based deque tests. */
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
        assertTrue(t1.equals(t2));
    }
}