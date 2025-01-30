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
    public void randomizedmutiloperationTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        for (int i = 0; i < 100000; i++) {
            int operation = StdRandom.uniform(0, 4);
            if (operation == 0) {
                L.addLast(i);
                B.addLast(i);
            } else if (operation == 1) {
                assertEquals(L.size(), B.size());
            } else if (operation == 2) {
                if (L.size() > 0) {
                    assertEquals(L.removeLast(), B.removeLast());
                }
            } else {
                if (L.size() > 0) {
                    int index = StdRandom.uniform(0, L.size());
                    assertEquals(L.get(index), B.get(index));
                }
            }
        }
    }
}
