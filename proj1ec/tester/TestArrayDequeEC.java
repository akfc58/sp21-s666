package tester;

import static org.junit.Assert.*;

import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {

    /** A big size random test for almost all operations in AD. */
    @Test
    public void randomizedArrayDequeTest() {
        StudentArrayDeque<Integer> LLD1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> LLD2 = new ArrayDequeSolution<>();
        int N = 500000;
        ArrayDequeSolution<String> stack = new ArrayDequeSolution<>();
        stack.addLast("");
        stack.addLast("");
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 10000);
                LLD1.addLast(randVal);
                LLD2.addLast(randVal);
                stack.addLast("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 10000);
                LLD1.addFirst(randVal);
                LLD2.addFirst(randVal);
                stack.addLast("addFirst(" + randVal + ")");
            } else if (operationNumber == 2) {
                // size
                int size1 = LLD1.size();
                int size2 = LLD2.size();
                stack.addLast("size()");
                assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                        + stack.get(stack.size() - 1) + "\n", size1, size2);
            } else if (operationNumber == 3) {
                // removeLast
                if (LLD1.size() > 0) {
                    stack.addLast("removeLast()");
                    assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                            + stack.get(stack.size() - 1) + "\n", LLD1.removeLast(), LLD2.removeLast());
                }
            } else if (operationNumber == 4) {
                // removeFirst
                if (LLD1.size() > 0) {
                    stack.addLast("removeFirst()");
                    assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                            + stack.get(stack.size() - 1) + "\n", LLD1.removeFirst(), LLD2.removeFirst());
                }
            } else if (operationNumber == 5) {
                if (LLD1.size() > 0) {
                    int index = StdRandom.uniform(0, LLD1.size());
                    stack.addLast("get(" + index + ")");
                    assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                            + stack.get(stack.size() - 1) + "\n", LLD1.get(index), LLD2.get(index));
                }
            }
        }
    }
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeEC.class);
    }

}
