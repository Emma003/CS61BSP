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
        BuggyAList<Integer> buggyList = new BuggyAList<>();
        AListNoResizing<Integer> noResizing = new AListNoResizing<>();

        buggyList.addLast(4);
        buggyList.addLast(5);
        buggyList.addLast(6);

        noResizing.addLast(4);
        noResizing.addLast(5);
        noResizing.addLast(6);

        assertEquals(noResizing.removeLast(), buggyList.removeLast());
        assertEquals(noResizing.removeLast(), buggyList.removeLast());
        assertEquals(noResizing.removeLast(), buggyList.removeLast());
    }

    @Test
    public void randomizedTest () {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                assertEquals(L.size(), B.size());
            } else if (L.size() > 0 && operationNumber == 2) {
                // getLast
                assertEquals(L.getLast(), B.getLast());
            } else if (L.size() > 0 && operationNumber == 3) {
                // removeLast
                assertEquals(L.removeLast(), B.removeLast());
            }
        }
    }
}
