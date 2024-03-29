package deque;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListDequeTest {

    @Test
    public void equalsTest() {
        LinkedListDeque<String> t1 = new LinkedListDeque<>();
        t1.addFirst("last");
        t1.addFirst("middle");
        t1.addFirst("first");
        ArrayDeque<String> t2 = new ArrayDeque<>();
        t2.addFirst("last");
        t2.addFirst("middle");
        t2.addFirst("first");
        assertEquals(t1, t2);
    }
}
