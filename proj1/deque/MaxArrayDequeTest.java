package deque;

import org.junit.Test;
import java.util.Comparator;
import java.util.Random;
import static org.junit.Assert.*;

public class MaxArrayDequeTest {

    @Test
    public void randomizedTest() {
        Random rand = new Random();
        Comparator<Integer> comparator = Integer::compareTo;
        MaxArrayDeque<Integer> maxDeque = new MaxArrayDeque<>(comparator);

        int maxElement = Integer.MIN_VALUE;
        for (int i = 0; i < 1000; i++) {
            int element = rand.nextInt(10000);
            maxDeque.addLast(element);
            if (element > maxElement) {
                maxElement = element;
            }
        }

        assertEquals("The max element should be the largest element added", (Integer) maxElement, maxDeque.max());
    }
}