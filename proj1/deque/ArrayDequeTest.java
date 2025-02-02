package deque;

import org.junit.Test;

public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        ad1.addFirst("front");
        ad1.addLast("middle");
        ad1.addLast("back");

        System.out.println("Printing out deque: ");
        ad1.printDeque();
    }

    // more tests
    @Test
    public void testAddRemove() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        ad1.addFirst(10);
        ad1.addLast(20);
        ad1.addLast(30);

        ad1.removeFirst();
        ad1.removeLast();
    }

    @Test
    public void testIsEmptySize() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        ad1.addFirst(10);
        ad1.addLast(20);
        ad1.addLast(30);

        ad1.isEmpty();
        ad1.size();
    }

    @Test
    public void testGet() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        ad1.addFirst(10);
        ad1.addLast(20);
        ad1.addLast(30);

        ad1.get(0);
        ad1.get(1);
        ad1.get(2);
    }

}
