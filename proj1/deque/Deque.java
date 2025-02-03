package deque;

import java.util.Iterator;

public interface Deque<T> extends Iterable<T> {
    void addFirst(T item);
    void addLast(T item);
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);
    default boolean isEmpty() {
        return size() == 0;
    }
    class DequeIterator<T> implements Iterator<T> {
        private int index;
        private Deque<T> deque;

        public DequeIterator(Deque<T> deque) {
            this.deque = deque;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < deque.size();
        }

        @Override
        public T next() {
            T item = deque.get(index);
            index += 1;
            return item;
        }
    }
    default Iterator<T> iterator() {
        return new DequeIterator<>(this);
    }

}
