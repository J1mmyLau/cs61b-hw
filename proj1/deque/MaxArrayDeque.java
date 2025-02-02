package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    public T max() {
        return max(this.comparator);
    }

    public T max(Comparator<T> comparator) {
        if (this.size() == 0) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            T currentItem = get(i);
            if (comparator.compare(currentItem, max) > 0) {
                max = currentItem;
            }
        }
        return max;
    }
}