package tester;

import static org.junit.Assert.*;

import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class TestArrayDequeEC {

    /** A big size random test for almost all operations in AD. */
    @Test
    public void randomizedArrayDequeTest() {
        StudentArrayDeque<Integer> LLD1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> LLD2 = new ArrayDequeSolution<>();
        int N = 500000;
        String stack = "";
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = 666;
                LLD1.addLast(randVal);
                LLD2.addLast(randVal);
                stack += "addLast(" + randVal + ")\n";
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = 666;
                LLD1.addFirst(randVal);
                LLD2.addFirst(randVal);
                stack += "addFirst(" + randVal + ")\n";
            } else if (operationNumber == 2) {
                // size
                int size1 = LLD1.size();
                int size2 = LLD2.size();
                stack += "size()\n";
                assertEquals(stack, size1, size2);
            } else if (operationNumber == 3) {
                // removeLast
                if (LLD1.size() > 0) {
                    stack += "removeLast()\n";
                    assertEquals(stack, LLD1.removeLast(), LLD2.removeLast());
                }
            } else if (operationNumber == 4) {
                // removeFirst
                if (LLD1.size() > 0) {
                    stack += "removeFirst()\n";
                    assertEquals(stack, LLD1.removeFirst(), LLD2.removeFirst());
                }
            } else if (operationNumber == 5) {
                // get
                if (LLD1.size() > 0) {
                    int index = StdRandom.uniform(0, LLD1.size());
                    stack += "get(" + index + ")\n";
                    assertEquals(stack, LLD1.get(index), LLD2.get(index));
                }
            }
        }
    }
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeEC.class);
    }

}
