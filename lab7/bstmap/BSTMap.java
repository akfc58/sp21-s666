package bstmap;

import java.util.*;
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root; // root of BSTMap
    private Set<K> allKeys = new HashSet<>();
    private class BSTNode {
        private K key;
        private V val;
        private BSTNode left;
        private BSTNode right;
        private int size;

        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    public BSTMap() {
        root = null;
    }

    private void getAllKeysHelper(BSTNode n) {
        if (n == null) {
            return;
        }
        getAllKeysHelper(n.left);
        allKeys.add(n.key); // in a Set, order doesn't matter.
        getAllKeysHelper(n.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyhelper(root, key) != null;
    }

    private K containsKeyhelper(BSTNode n, K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must be comparable");
        }
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            return containsKeyhelper(n.left, key);
        }
        if (cmp > 0) {
            return containsKeyhelper(n.right, key);
        }
        return n.key;
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode n, K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must be comparable");
        }
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            return get(n.left, key);
        }
        if (cmp > 0) {
            return get(n.right, key);
        }
        return n.val;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode n) {
        if (n == null) {
            return 0;
        }
        return n.size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode n, K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key must be comparable");
        }
        if (n == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = put(n.left, key, value);
        } else if (cmp > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.val = value;
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;

    }

    @Override
    public Set<K> keySet() {
        getAllKeysHelper(root);
        return allKeys;
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
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        private K curr;
        private List<K> toList = new ArrayList<>();

        public BSTMapIterator() {
            toList.addAll(allKeys);
            toList.sort(Comparator.naturalOrder());
        }

        @Override
        public boolean hasNext() {
            return !toList.isEmpty();
        }

        @Override
        public K next() {
            return toList.remove(0);
        }
    }

    public void printInOrder() {
        printInOrderhelper(this.root);
    }

    private void printInOrderhelper(BSTNode n) {
        if (n == null) {
            return;
        }
        printInOrderhelper(n.left);
        // System.out.println("key is: " + n.key + ". value is: " + n.val + ".");
        printInOrderhelper(n.right);
    }
}
