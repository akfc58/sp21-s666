package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
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
        SLList<Integer> testSLlist = new SLList<>();
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int[] testPoint = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int i = 0; i <testPoint.length; i += 1) {
            for (int j = 0; j <= testPoint[i]; j += 1) {
                testSLlist.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < 10000; k += 1) {
                testSLlist.getLast();
            }
            Ns.addLast(testPoint[i]);
            times.addLast(sw.elapsedTime());
            opCounts.addLast(10000);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
