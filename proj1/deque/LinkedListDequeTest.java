package deque;

import org.junit.Test;

import static org.junit.Assert.*;


/** Performs some linked list based deque tests. */
public class LinkedListDequeTest {

    @Test
    /** Test for creation of 0 or 1 item Deque. */
    public void basicCreationTest() {
        LinkedListDeque<Integer> t1 = new LinkedListDeque<>(1); //Test 1 item deque.
        assertEquals(t1.size(), 1);
        assertTrue(!t1.isEmpty());
        LinkedListDeque<Integer> t2 = new LinkedListDeque<>(); // Test empty deque.
        assertEquals(t2.size(), 0);
        assertTrue(t2.isEmpty());

    }

    @Test
    /** Test basic add first or add last to empty or 1 item deque. */
    public void addFirstAddLastBasicTest() {
        LinkedListDeque<Integer> t1 = new LinkedListDeque<>(1); //Test 1 item deque.
        assertEquals(t1.size(), 1);
        LinkedListDeque<Integer> t2 = new LinkedListDeque<>(); // Test empty deque.
        assertEquals(t2.size(), 0);
        // Test add to 1 item deque.
        t1.addFirst(222);
        assertEquals(t1.size(),2);
        t1.addLast(444);
        assertEquals(t1.size(), 3);
        // Test add to empty deque.
        t2.addFirst(222);
        assertEquals(t2.size(), 1);
        t2.addLast(444);
        assertEquals(t2.size(), 2);
    }

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());
        lld1.addLast("middle");
        assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
        lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /** Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /** Test get and get recursive. */
    public void getTest() {
        // Test 0 or 1 item.
        LinkedListDeque<Integer> oneItem = new LinkedListDeque<>(1); //Test 1 item deque.
        assertEquals(1,(int) oneItem.get(0));
        LinkedListDeque<Integer> blank = new LinkedListDeque<>(); // Test empty deque.
        assertEquals(null, blank.get(0));

        // Test big deque.
        LinkedListDeque<Integer> bigDeque = new LinkedListDeque<Integer>();
        for (int i = 0; i < 100; i++) {
            bigDeque.addLast(i);
        }
        assertEquals(99, (int) bigDeque.get(99));
        assertEquals(12, (int) bigDeque.get(12));
        assertEquals(0, (int) bigDeque.get(0));
        bigDeque.printDeque();
    }

    @Test
    /** Test recursive version of get. */
    public void getRecursiveTest() {
        LinkedListDeque<Integer> bigDeque = new LinkedListDeque<Integer>();
        for (int i = 0; i < 100; i++) {
            bigDeque.addLast(i);
        }

        assertEquals(99, (int) bigDeque.getRecursive(99));
        assertEquals(12, (int) bigDeque.getRecursive(12));
        assertEquals(0, (int) bigDeque.getRecursive(0));
        bigDeque.printDeque();
    }
}
