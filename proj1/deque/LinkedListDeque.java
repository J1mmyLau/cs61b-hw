package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node p;
        public LinkedListDequeIterator() {
            p = sentinel.next;
        }
        public boolean hasNext() {
            return p != sentinel;
        }
        public T next() {
            T item = p.item;
            p = p.next;
            return item;
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass() || o.getClass() != LinkedListDeque.class) {
            return false;
        }
        if(o.getClass() == LinkedListDeque.class){
            LinkedListDeque<T> other = (LinkedListDeque<T>) o;
            if (other.size() != this.size()) {
                return false;
            }
            Iterator<T> otherIterator = other.iterator();
            Iterator<T> thisIterator = this.iterator();
            while (otherIterator.hasNext()) {
                if (!otherIterator.next().equals(thisIterator.next())) {
                    return false;
                }
            }
            return true;
        }
        else if(o.getClass() == ArrayDeque.class){
            ArrayDeque<T> other = (ArrayDeque<T>) o;
            if (other.size() != this.size()) {
                return false;
            }
            Iterator<T> otherIterator = other.iterator();
            Iterator<T> thisIterator = this.iterator();
            while (otherIterator.hasNext()) {
                if (!otherIterator.next().equals(thisIterator.next())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node firstNode = sentinel.next;
        sentinel.next = firstNode.next;
        firstNode.next.prev = sentinel;
        size -= 1;
        return firstNode.item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        size -= 1;
        return lastNode.item;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.next, index - 1);
    }
}
