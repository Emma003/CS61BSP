package deque;

/**
 * Array based double-ended queue. Accepts any generic type.
 * @param
 */

public class ArrayDeque<Thing>{
    private int size, nextFirst, nextLast;
    private Thing[]items;

    // Creates empty linked list deque
    public ArrayDeque() {
        items = (Thing[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }

    // Adds an item of type int to the front of the deque.
    // Constant time
    public void addFirst(Thing item) {
        if (size == items.length) {
            this.resizeUp();
        }
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
        items[nextFirst % items.length] = item;
        nextFirst--;
        size++;

    }

    // Adds an item of type int to the back of the deque.
    // Constant time
    public void addLast(Thing item) {
        if (size == items.length) {
            this.resizeUp();
        }
        if (nextLast > 7) {
            nextLast = 0;
        }
        items[nextLast] = item;
        nextLast++;
        size++;
    }

    public void resizeUp() {
//        int[]a = new int[size * 2];
//        System.arraycopy(items,0,a,0,nextLast % items.length);
//        //System.arraycopy(items,nextFirst + 1,a,nextFirst+1 + size,items.length - nextFirst+1);
//        System.arraycopy(items, nextFirst + 1, a,nextFirst+1 + size,size - (nextFirst+1));
//        items = a;
//        nextFirst += size;
    }

    // Removes and returns the item at the front of the deque. If no such item exists, returns null.
    // Constant time
    public Thing removeLast() {
        //resize
        double usageFactor = 1.0 * size/items.length;
        if (items.length >= 16 && usageFactor < 0.25) {
            resizeDown();
        }
        //out of bounds
        if (nextLast == 0) {
            nextLast = items.length;
        }

        Thing last = items[nextLast-1];
        items[nextLast-1] = null;
        nextLast--;
        size--;
        return last;
    }

    // Removes and returns the item at the back of the deque. If no such item exists, returns null.
    // Constant time
    public Thing removeFirst() {
        // resize
        double usageFactor = 1.0 * size/items.length;
        if (items.length >= 16 && usageFactor < 0.25) {
            resizeDown();
        }
        // out of bounds
        if(nextFirst == items.length -1) {
            nextFirst = -1;
        }

        Thing first = items[nextFirst + 1]; //CHANGE TO NULL
        items[nextFirst + 1] = null;
        nextFirst++;
        size--;

        return first;
    }

    public void resizeDown() {

    }


    // Returns true if deque is empty, false otherwise.
    public boolean isEmpty() {

        return size == 0;
    }

    // Returns the number of items in the deque.
    // Constant time
    public int size() {
        if (size < 0) {
            return 0;
        }
        return size;
    }

    // Gets the item at the given index. If no such item exists, returns null.
    // Uses iteration
    public Thing get(int index) {

        return items[index];
    }

    // Prints the items in the deque from first to last, separated by a space.
    public void printDeque() {
        for(Thing item: items) {
            System.out.print(item + " ");
        }
        System.out.println("");
    }

    public static void main (String[]args) {
        ArrayDeque<String> list = new ArrayDeque<>();
        System.out.println(list.isEmpty());
//        list.addFirst(1);
//        list.addFirst(2);
//        list.addFirst(3);
//        list.addFirst(4);
//        list.addFirst(5);
//        list.addFirst(6);
//        list.addFirst(7);
//        list.addFirst(8);

//        list.removeFirst();
//        list.removeFirst();
//        list.removeFirst();
//        list.removeFirst();
//        list.removeFirst();
//        list.removeFirst();
//        list.removeFirst();
//        list.removeFirst();

        list.addLast("emma");
        list.addLast("emmy");
        list.addLast("emz");
        list.addLast("emzo");
        list.addLast("emz2trappy");
        list.addLast("emzino");
        list.addLast("big m");
        list.addLast("m money");

        list.removeLast();
        list.removeLast();
        list.removeLast();
        list.removeLast();

        System.out.println(list.get(0));

        list.printDeque();










    }
}
