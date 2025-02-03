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
}
