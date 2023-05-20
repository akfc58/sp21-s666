package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    private static boolean numInArray(int x, int[] array) {
        for (int i = 0; i < array.length; i += 1) {
            if (x == array[i]) {
                return true;
            }
        }
        return false;
    }

    public static void timeAListConstruction() {
        AList<Integer> testAlist = new AList<>();
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int[] testPoint = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        Stopwatch sw = new Stopwatch();
        int count = 0;
        while (testAlist.size() <= 128000) {
            testAlist.addLast(1);
            count += 1;
            if (numInArray(testAlist.size(),testPoint)){
                Ns.addLast(testAlist.size());
                times.addLast(sw.elapsedTime());
                opCounts.addLast(count);
            }
        }
        printTimingTable(Ns, times, opCounts);
    }
}
