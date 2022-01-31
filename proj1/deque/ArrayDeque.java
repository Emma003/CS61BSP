package deque;

/**
 * Array based double-ended queue. Accepts any generic type.
 * @param
 */

public class ArrayDeque{
    private int size, nextFirst, nextLast;
    private int[]items;
    private double usageFactor;

    // Creates empty linked list deque
    public ArrayDeque() {
        items = new int[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
        usageFactor = size / items.length;
    }

    // Adds an item of type int to the front of the deque.
    // Constant time
    public void addFirst(int item) {
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

    public void resizeUp() {
        int[]a = new int[size * 2];
        System.arraycopy(items,0,a,0,nextLast % items.length);
        //System.arraycopy(items,nextFirst + 1,a,nextFirst+1 + size,items.length - nextFirst+1);
        System.arraycopy(items, nextFirst + 1, a,nextFirst+1 + size,size - (nextFirst+1));
        items = a;
        nextFirst += size;
    }

    // Adds an item of type int to the back of the deque.
    // Constant time
    public void addLast(int item) {
        if (size == items.length) {
            this.resizeUp();
        }
        items[nextLast % items.length] = item;
        nextLast++;
        size++;
    }

    // Removes and returns the item at the front of the deque. If no such item exists, returns null.
    // Constant time
    public int removeFirst() {
        if (items.length >= 16 && usageFactor < 0.25) {
            resizeDown();
        }
        int first = items[nextFirst + 1];
        items[nextFirst+1] = 0; //SET IT TO NULL WITH GENERICS
        nextFirst++;
        size--;
        return first;
    }

    public void resizeDown() {

    }

    // Removes and returns the item at the back of the deque. If no such item exists, returns null.
    // Constant time
    public int removeLast() {
        if (items.length >= 16 && usageFactor < 0.25) {
            resizeDown();
        }
        int last = items[nextLast - 1];
        items[nextLast-1] = 0; //SET IT TO NULL WITH GENERICS
        nextLast--;
        size--;
        return last;
    }

    // Returns true if deque is empty, false otherwise.
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the number of items in the deque.
    // Constant time
    public int size() {
        return size;
    }

    // Gets the item at the given index. If no such item exists, returns null.
    // Uses iteration
    public int get(int index) {
        return items[index];
    }

    // Prints the items in the deque from first to last, separated by a space.
    public void printDeque() {
        for(int item: items) {
            System.out.print(item + " ");
        }
        System.out.println("");
    }

    public static void main (String[]args) {
        ArrayDeque list = new ArrayDeque();
        System.out.println(list.isEmpty());
        list.addLast(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        list.addFirst(5);
        list.addFirst(6);
        list.addFirst(7);
        list.addLast(8);
        list.printDeque();
        list.addFirst(10);
        list.printDeque();
        list.addLast(9);
        list.addFirst(2);
        list.addFirst(3);
        list.addFirst(4);
        list.printDeque();



    }
}
