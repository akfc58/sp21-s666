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
        ArrayList<String> stack = new ArrayList<>();
        stack.add("");
        stack.add("");
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = 666;
                LLD1.addLast(randVal);
                LLD2.addLast(randVal);
                stack.add("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = 666;
                LLD1.addFirst(randVal);
                LLD2.addFirst(randVal);
                stack.add("addFirst(" + randVal + ")");
            } else if (operationNumber == 2) {
                // size
                int size1 = LLD1.size();
                int size2 = LLD2.size();
                stack.add("size()");
                assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                        + stack.get(stack.size() - 1), size1, size2);
            } else if (operationNumber == 3) {
                // removeLast
                if (LLD1.size() > 0) {
                    stack.add("removeLast()");
                    assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                            + stack.get(stack.size() - 1), LLD1.removeLast(), LLD2.removeLast());
                }
            } else if (operationNumber == 4) {
                // removeFirst
                if (LLD1.size() > 0) {
                    stack.add("removeFirst()");
                    assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                            + stack.get(stack.size() - 1), LLD1.removeFirst(), LLD2.removeFirst());
                }
            } else if (operationNumber == 5) {
                if (LLD1.size() > 0) {
                    int index = StdRandom.uniform(0, LLD1.size());
                    stack.add("get(" + index + ")");
                    assertEquals(stack.get(stack.size() - 3) + "\n" + stack.get(stack.size() - 2) + "\n"
                            + stack.get(stack.size() - 1), LLD1.get(index), LLD2.get(index));
                }
            }
        }
    }
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDequeEC.class);
    }

}
