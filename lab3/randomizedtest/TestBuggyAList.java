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

        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);
            } else if (L.size() > 0 && operationNumber == 2) {
                // getLast
                int last = L.getLast();
                System.out.println("get last: " + last);
            } else if (L.size() > 0 && operationNumber == 3) {
                // removeLast
                int last = L.removeLast();
                System.out.println("removeLast: " + last);
            }
        }
    }
}
