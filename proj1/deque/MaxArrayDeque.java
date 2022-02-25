package deque;
import java.util.Comparator;

public class MaxArrayDeque<Thing> extends ArrayDeque {
    public MaxArrayDeque(Comparator<Thing> c) {

    }

    // returns max element in the deque as governed by the previously given comparator
    public Thing max() {
        if (this.isEmpty()) {
            return null;
        }
        Thing max = null;


        return null;
    }

    public Thing max (Comparator<Thing> c) {
        if (this.isEmpty()) {
            return null;
        }

        return null;
    }

    public static class ThingComparator<Thing extends Comparable<Thing>> implements Comparator<Thing> {
        public int compare(Thing a, Thing b){
            return a.compareTo(b);
        }
    }


}
