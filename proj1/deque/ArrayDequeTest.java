package deque;

import org.junit.Test;
import static org.junit.Assert.*;

/** Performs some array based deque tests. */
public class ArrayDequeTest {

    @Test
    /** Test for creation of 0 or 1 item Deque. */
    public void basicCreationTest() {
        ArrayDeque<Integer> t1 = new ArrayDeque<>(1); //Test 1 item deque.
        assertEquals(t1.size(), 1);
        assertTrue(!t1.isEmpty());
        ArrayDeque<Integer> t2 = new ArrayDeque<>(); // Test empty deque.
        assertEquals(t2.size(), 0);
        assertTrue(t2.isEmpty());
    }

    @Test
    /** Test basic add first or add last to empty or 1 item deque. */
    public void addTest() {
        ArrayDeque<String> t1 = new ArrayDeque<>("is");
        t1.addLast("a");
        t1.addLast("example");
        t1.addLast("sentence.");
        t1.addFirst("this");
        assertEquals(5, t1.size());
        t1.printDeque();

        ArrayDeque<Integer> t2 = new ArrayDeque<>();
        for (int i = 0; i <= 6; i += 1){
            t2.addFirst(i);
        }
        assertEquals(7, t2.size());
        t2.printDeque();
    }

    @Test
    /** Test removeLast. */
    public void removeLastTest() {
        // Test remove items[0].
        ArrayDeque<String> t1 = new ArrayDeque<>("is");
        t1.addLast("a");
        t1.addLast("example");
        t1.addLast("sentence.");
        t1.addFirst("this");
        t1.printDeque();
        System.out.println(t1.removeLast());
        assertEquals(4, t1.size());
        t1.printDeque();
        t1.addLast("new sentence.");
        t1.printDeque();

        // Test normal remove.
        ArrayDeque<Integer> t2 = new ArrayDeque<>();
        for (int i = 0; i <= 2; i += 1){
            t2.addFirst(i);
        }
        assertEquals(3, t2.size());
        t2.printDeque();
        System.out.println(t2.removeLast());
        assertEquals(2, t2.size());
        t2.printDeque();

        // Test remove item[item.length].
        ArrayDeque<Integer> t3 = new ArrayDeque<>();
        for (int i = 0; i <= 2; i += 1){
            t3.addLast(i);
        }
        assertEquals(3, t3.size());
        t3.printDeque();
        System.out.println(t3.removeLast());
        assertEquals(2, t3.size());
        t3.printDeque();
        t3.addLast(999);
        t3.addLast(888);
        t3.printDeque();
    }
}
