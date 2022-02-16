package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

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

    public static void timeAListConstruction() {
        AList<Integer> items = new AList<>();
        AList<Integer> timingTest = new AList<>();
        AList<Double> times = new AList<>();

        for (int x = 1000; x <= 128000 ;x += x) {
            Stopwatch sw = new Stopwatch();
            for (int y = x; y <= 2*x; y++) {
                items.addLast(y);
            }
            double timeInSeconds = sw.elapsedTime();
            timingTest.addLast(x);
            times.addLast(timeInSeconds);
        }
        printTimingTable(timingTest,times,timingTest);

    }
}
