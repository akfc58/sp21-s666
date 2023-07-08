package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


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
    // You should probably define some more!
    /** Set of keys. */
    private Set<K> keySet; //TODO clear after iteration. buggy?
    /** Size of items. */
    private int N;
    /** Max load factor of the bucket. */
    private double max;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
        N = 0;
        max = 0.75;
        keySet = new HashSet<>();
    }


    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        N = 0;
        max = 0.75;
        keySet = new HashSet<>();
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
        N = 0;
        max = maxLoad;
        keySet = new HashSet<>();
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
        return new LinkedList<>();
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
        return new Collection[tableSize]; // illegal to create parameterized type of array.
        //TODO: verify that all items are same type of Collection.
    }

    @Override
    public void clear() {
        // 1. create table the same length as before, clear content.
        // 2. change paras to initial state.
        buckets = createTable(buckets.length);
        keySet = new HashSet<>();
        N = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int pos = positionHelper(key);
        if (buckets[pos] == null) {
            return false;
        }
        for (Node each: buckets[pos]) {
            if (each.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        V res = null;
        int pos = positionHelper(key);
        Collection<Node> n = buckets[pos];
        if (n == null) {
            return null;
        }
        for (Node each: n) {
            if (each.key.equals(key)) {
                res = each.value;
            }
        }
        return res;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public void put(K key, V value) {
        // create new node, calculate key hash, put into the right bucket.
        // if bucket is not there, create one. if key is there, update value
        Node newNode = createNode(key, value);
        int pos = positionHelper(key);
        if (buckets[pos] == null) {
            Collection<Node> bucket = createBucket();
            buckets[pos] = bucket;
            bucket.add(newNode);
            keySet.add(newNode.key);
            N += 1;
            return;
        }
        for (Node each: buckets[pos]) {
            if (each.key.equals(key)) {
                each.value = value; // update value.
                return;
            }
        }
        buckets[pos].add(newNode);
        keySet.add(newNode.key);
        N += 1;
    }

    private int positionHelper(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new KeySetIterator();
    }

    private class KeySetIterator implements Iterator<K> {
        private int pos;
        private K[] keyArray = (K[]) keySet.toArray();

        public KeySetIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < keySet.size();
        }

        @Override
        public K next() {
            K item = keyArray[pos];
            pos += 1;
            return item;
        }
    }
}
