package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int size;
    private int capacity;
    private int nextFirst;
    private int nextLast;

    private class ArrayDequeIterator implements Iterator<T> {
        private int index;
        public ArrayDequeIterator() {
            index = 0;
        }
        public boolean hasNext() {
            return index < size;
        }
        public T next() {
            T item = get(index);
            index += 1;
            return item;
        }
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        capacity = 8;
        nextFirst = 0;
        nextLast = 1;
    }

    public ArrayDeque(int capacity) {
        items = (T[]) new Object[capacity];
        size = 0;
        this.capacity = capacity;
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int newCapacity) {
        T[] newItems = (T[]) new Object[newCapacity];
        int oldIndex = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            newItems[i] = items[oldIndex];
            oldIndex = plusOne(oldIndex);
        }
        items = newItems;
        capacity = newCapacity;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private int minusOne(int index) {
        return (index - 1 + capacity) % capacity;
    }

    private int plusOne(int index) {
        return (index + 1) % capacity;
    }

    public int capacity() {
        return capacity;
    }
    private void checkResize() {
        if (size == capacity) {
            resize(capacity * 2);
        } else if (capacity >= 16 && size < capacity / 4) {
            resize(capacity / 2);
        }
    }

    public void addFirst(T item) {
        checkResize();
        items[nextFirst] = item;
        nextFirst = minusOne(nextFirst);
        size += 1;
    }

    public void addLast(T item) {
        checkResize();
        items[nextLast] = item;
        nextLast = plusOne(nextLast);
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int index = plusOne(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[index] + " ");
            index = plusOne(index);
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = plusOne(nextFirst);
        T firstItem = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;
        checkResize();
        return firstItem;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        nextLast = minusOne(nextLast);
        T lastItem = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        checkResize();
        return lastItem;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int realIndex = (plusOne(nextFirst) + index) % capacity;
        return items[realIndex];
    }


}
