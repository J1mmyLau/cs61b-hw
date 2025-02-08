package bstmap;

import java.security.PublicKey;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node n) {
        if (n == null) {
            return;
        }
        printInOrder(n.left);
        System.out.println(n.key + " : " + n.value);
        printInOrder(n.right);
    }

    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(Node n, K key) {
        if(n == null) {
            return false;
        }
        if(n.key.equals(key)) {
            return true;
        }
        int cmp = ((Comparable<K>) key).compareTo(n.key);
        if (cmp < 0) {
            return containsKey(n.left, key);
        } else {
            return containsKey(n.right, key);
        }

    }
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node n, K key) {
        if (n == null) {
            return null;
        }
        int cmp = ((Comparable<K>) key).compareTo(n.key);
        if (cmp < 0) {
            return get(n.left, key);
        } else if (cmp > 0) {
            return get(n.right, key);
        } else {
            return n.value;
        }
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node n, K key, V value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = ((Comparable<K>) key).compareTo(n.key);
        if (cmp < 0) {
            n.left = put(n.left, key, value);
        } else if (cmp > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.value = value;
        }
        return n;
    }

    public V remove(K key) {
        V value = get(key);
        if (value != null) {
            root = remove(root, key);
            size--;
        }
        return value;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();

    }

    private Node remove(Node n, K key) {
        if (n == null) {
            return null;
        }
        int cmp = ((Comparable<K>) key).compareTo(n.key);
        if (cmp < 0) {
            n.left = remove(n.left, key);
        } else if (cmp > 0) {
            n.right = remove(n.right, key);
        } else {
            if (n.right == null) {
                return n.left;
            }
            if (n.left == null) {
                return n.right;
            }
            Node temp = n;
            n = min(temp.right);
            n.right = removeMin(temp.right);
            n.left = temp.left;
        }
        return n;
    }

    private Node min(Node n) {
        if (n.left == null) {
            return n;
        } else {
            return min(n.left);
        }
    }

    private Node removeMin(Node n) {
        if (n.left == null) {
            return n.right;
        }
        n.left = removeMin(n.left);
        return n;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        addKeys(root, keys);
        return keys;
    }

    private void addKeys(Node n, Set<K> keys) {
        if (n == null) {
            return;
        }
        keys.add(n.key);
        addKeys(n.left, keys);
        addKeys(n.right, keys);
    }
}
