package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resizeDouble down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadFactor;

    // You should probably define some more!

    /** Constructors */
    @Override
    public void clear() {
        buckets = createTable(16);
        size = 0;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    @Override
    public boolean containsKey(K key) {
        int index = hash(key);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return false;
        }
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private boolean checkLoadDouble() {
        return (double) size / buckets.length > loadFactor;
    }

    private void resizeDouble() {
        Collection<Node>[] newBuckets = createTable(buckets.length * 2);
        for (Collection<Node> bucket : buckets) {
            if (bucket == null) {
                continue;
            }
            for (Node node : bucket) {
                int index = (node.key.hashCode() & 0x7fffffff) % newBuckets.length;
                Collection<Node> newBucket = newBuckets[index];
                if (newBucket == null) {
                    newBucket = createBucket();
                    newBuckets[index] = newBucket;
                }
                newBucket.add(node);
            }
        }
        buckets = newBuckets;
    }

    @Override
    public void put(K key, V value) {
        if (checkLoadDouble()) {
            resizeDouble();
        }
        int index = hash(key);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            bucket = createBucket();
            buckets[index] = bucket;
        }
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        bucket.add(createNode(key, value));
        size += 1;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket == null) {
                continue;
            }
            for (Node node : bucket) {
                keySet.add(node.key);
            }
        }
        return keySet;
    }

    private boolean checkLoadHalf() {
        return (double) size / buckets.length < 1 / loadFactor;
    }

    private void resizeHalf(){
        Collection<Node>[] newBuckets = createTable(buckets.length / 2);
        for (Collection<Node> bucket : buckets) {
            if (bucket == null) {
                continue;
            }
            for (Node node : bucket) {
                int index = (node.key.hashCode() & 0x7fffffff) % newBuckets.length;
                Collection<Node> newBucket = newBuckets[index];
                if (newBucket == null) {
                    newBucket = createBucket();
                    newBuckets[index] = newBucket;
                }
                newBucket.add(node);
            }
        }
        buckets = newBuckets;
    }

    @Override
    public V remove(K key) {
        if (checkLoadHalf()) {
            resizeHalf();
        }
        int index = hash(key);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                size -= 1;
                return node.value;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (checkLoadHalf()) {
            resizeHalf();
        }
        int index = hash(key);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }
        for (Node node : bucket) {
            if (node.key.equals(key) && node.value.equals(value)) {
                bucket.remove(node);
                size -= 1;
                return node.value;
            }
        }
        return null;
    }

    public MyHashMap() {
        //help me finish this copilot
        buckets = createTable(16);
        size = 0;
        loadFactor = 1.5;
    }

    public MyHashMap(int initialSize) {
        // YOUR CODE HERE
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = 1.5;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        //finish this copilot
        Collection<Node> bucket = new ArrayList<>();
        return bucket;
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
