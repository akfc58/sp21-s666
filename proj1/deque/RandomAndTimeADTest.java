package deque;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.Stopwatch;

public class RandomAndTimeADTest {

    /** A big size random test for almost all operations in AD. */
    @Test
    public void randomizedArrayDequeTest() {
        ArrayDeque<Integer> LLD1 = new ArrayDeque<>();
        ArrayDeque<Integer> LLD2 =new ArrayDeque<>();
        int N = 50000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 10000);
                LLD1.addLast(randVal);
                LLD2.addLast(randVal);
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 10000);
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
                if ( LLD1.size() > 0) {
                    int index = StdRandom.uniform(0, LLD1.size());
                    assertEquals(LLD1.get(index), LLD2.get(index));
                }
            }
        }
    }

    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times, ArrayDeque<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }


    public static void main(String[] args) {
        timeGetLast();
    }

    private static boolean numInArray(int x, int[] array) {
        for (int i = 0; i < array.length; i += 1) {
            if (x == array[i]) {
                return true;
            }
        }
        return false;
    }

    public static void timeGetLast() {
        ArrayDeque<Integer> testSLlist = new ArrayDeque<>();
        ArrayDeque<Integer> Ns = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCounts = new ArrayDeque<>();
        int[] testPoint = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 256000, 512000};
        for (int i = 0; i <testPoint.length; i += 1) {
            for (int j = 0; j <= testPoint[i]; j += 1) {
                testSLlist.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < 10000; k += 1) {
                testSLlist.get(testSLlist.size()-1);
            }
            Ns.addLast(testPoint[i]);
            times.addLast(sw.elapsedTime());
            opCounts.addLast(10000);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
