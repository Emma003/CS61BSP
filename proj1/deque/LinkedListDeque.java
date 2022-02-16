package deque;
/**
 * Linked-list based double-ended queue. Accepts any generic type.
 * @param
 */

public class LinkedListDeque<Thing> implements Deque<Thing>{

    // Nested Node class
    private class Node {
        private Thing item;
        private Node next;
        private Node previous;

        public Node(Node previous, Thing i, Node next) {
            this.item = i;
            this.previous = previous;
            this.next = next;
        }
    }

    private int size;
    private Node sentinel;

    // Creates empty linked list deque
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
        size = 0;
    }

    // Adds an item of type int to the front of the deque.
    // Constant time
    @Override
    public void addFirst(Thing item) {
        if (size == 0) {
            sentinel.next = new Node(sentinel, item, sentinel);
            sentinel.previous = sentinel.next;
        } else {
            sentinel.next = new Node(sentinel, item, sentinel.next);
            sentinel.next.next.previous = sentinel.next;
        }
        size++;
    }

    // Adds an item of type int to the back of the deque.
    // Constant time
    @Override
    public void addLast(Thing item) {
        if (size == 0) {
            sentinel.next = new Node(sentinel, item, sentinel);
            sentinel.previous = sentinel.next;
        } else {
            sentinel.previous.next = new Node(sentinel.previous, item, sentinel);
            sentinel.previous = sentinel.previous.next;
        }
        size++;
    }

    // Removes and returns the item at the front of the deque. If no such item exists, returns null.
    // Constant time
    @Override
    public Thing removeFirst() {
        if (size == 0) {
            return null; // MAKE SURE TO RETURN NULL WHEN YOU CHANGE INT TO GENERIC TYPE
        }

        Thing first = sentinel.next.item;
        if (size == 1) {
            sentinel.next = sentinel;
            sentinel.previous = sentinel;
        } else {
            sentinel.next = sentinel.next.next;
            sentinel.next.previous = sentinel;
        }
        size--;
        return first;
    }

    // Removes and returns the item at the back of the deque. If no such item exists, returns null.
    // Constant time
    @Override
    public Thing removeLast() {
        if (size == 0) {
            return null; // MAKE SURE TO RETURN NULL WHEN YOU CHANGE INT TO GENERIC TYPE
        }

        Thing last = sentinel.previous.item;
        if (size == 1) {
            sentinel.next = sentinel;
            sentinel.previous = sentinel;
        } else {
            sentinel.previous = sentinel.previous.previous;
            sentinel.previous.next = sentinel;
        }
        size--;
        return last;
    }


    // Returns the number of items in the deque.
    // Constant time
    @Override
    public int size() {
        return size;
    }

    // Gets the item at the given index. If no such item exists, returns null.
    // Uses iteration
    @Override
    public Thing get(int index) {
        if (index >= size || size == 0) {
            return null; // MAKE SURE TO RETURN NULL WHEN YOU CHANGE INT TO GENERIC TYPE
        }

        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    // Gets the item at the given index. If no such item exists, returns null.
    // Uses recursion
    public Thing getRecursive(int index) {
        if (index >= size || size == 0) {
            return null; // MAKE SURE TO RETURN NULL WHEN YOU CHANGE INT TO GENERIC TYPE
        }
        Node p = this.sentinel;
        Thing element = this.getRecursiveHelper(p, index + 1); // Helper method
        return element;
    }

    public Thing getRecursiveHelper(Node p, int index) {
        if (index == 0) {
            return p.item;
        }
        p = p.next;
        return this.getRecursiveHelper(p, index - 1);
    }

    // Prints the items in the deque from first to last, separated by a space.
    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println("");
    }

    public static void main (String[]args) {
        LinkedListDeque<String> L = new LinkedListDeque();
        L.addLast("hello");
        L.addLast("hi");
        L.addFirst("add first");
        L.addLast("yuh");
        String last = L.removeLast();

        System.out.println(L.getRecursive(2));
        L.printDeque();
    }
}