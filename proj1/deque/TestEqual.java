package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestEqual {
    @Test
    public void testEqual() {
        ArrayDeque<String> ad1 = new ArrayDeque<String>();
        LinkedListDeque<String> ad2 = new LinkedListDeque<String>();

        ad1.addFirst("front");
        ad1.addLast("middle");
        ad1.addLast("back");

        ad2.addFirst("front");
        ad2.addLast("middle");
        ad2.addLast("back");

        assertEquals(ad1, ad2);
    }

    @Test
    public void deepEqualTest() {
        //very long deque
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> ad2 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000; i++) {
            ad1.addLast(i);
            ad2.addLast(i);
        }
        assertEquals(ad1, ad2);

    }
}
