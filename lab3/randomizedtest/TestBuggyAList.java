package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> list1 = new AListNoResizing<>();
        BuggyAList<Integer> list2 = new BuggyAList<>();
        for (int i = 4; i <= 6; i+= 1) {
            list1.addLast(i);
            list2.addLast(i);
        }
        for (int j = 4; j <= 6; j += 1) {
            assertEquals(list1.size(), list2.size());
            assertEquals(list1.removeLast(), list2.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 =new BuggyAList<>();
        int N = 500000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
                // System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size2 = L2.size();
                assertEquals(size, size2);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() > 0) {
                    assertEquals(L.getLast(), L2.getLast());
                }
            } else {
                if (L.size() > 0) {
                    assertEquals(L.removeLast(), L2.removeLast());
                }

            }
        }
    }
}
