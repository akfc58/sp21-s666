package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root; // root of BSTMap
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

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (containsKeyhelper(root, key) != null) {
            return true;
        }
        return false;
    }

    private K containsKeyhelper(BSTNode n, K key) {
        if(!(key instanceof Comparable)) {
            throw new IllegalArgumentException("key must be comparable");
        }
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            return containsKeyhelper(n.left, key);
        }
        if (cmp < 0) {
            return containsKeyhelper(n.right, key);
        }
        return n.key;
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode n, K key) {
        if(!(key instanceof Comparable)) {
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
        if (!(key instanceof Comparable)) {
            throw new IllegalArgumentException("key must be comparable");
        }
        if (n == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = put(n.left, key, value);
        }else if (cmp > 0) {
            n.right = put(n.right, key, value);
        } else {
            n.val = value;
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;

    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();
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
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder(){
        // TODO
        printInOrderhelper(this.root);
    }

    private BSTNode printInOrderhelper(BSTNode n) {
        if (n == null) {
            return null;
        }
        printInOrderhelper(n.left);
        System.out.println("key is: " + n.key + ". value is: " + n.val + ".");
        printInOrderhelper(n.right);
        return n;
    }




}