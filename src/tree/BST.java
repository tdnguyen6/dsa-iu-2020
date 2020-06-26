/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree;

import java.util.List;
import list.DLinkedList;
import stacknqueue.Queue;
import stacknqueue.Stack;

/**
 *
 * @author LTSACH
 */
public class BST<T> implements IBinarySearchTree<T>, ITreeWalker<T> {

    private int size;
    Node<T> root;

    public BST() {
        this.size = 0;
        this.root = null;
    }

    ///
    private int key(T item) {
        return item.hashCode();
    }

    private Node<T> add(Node<T> root, T item) {
        if (root == null) {
            return new Node<>(item, null, null);
        }
        if (key(item) < key(root.item)) {
            root.left = this.add(root.left, item);
        } else {
            root.right = this.add(root.right, item);
        }
        return root;
    }

    @Override
    public void add(T item) {
        root = add(this.root, item);
        this.size += 1;
    }

    private Node<T> remove(Node<T> root, Object key) {
        if (root == null) {
            return null;
        }
        if (key.hashCode() < key(root.item)) {
            root.left = remove(root.left, key);
            return root;
        } else if (key.hashCode() > key(root.item)) {
            root.right = remove(root.right, key);
            return root;
        } else {
            if (root.left == null) {
                this.size -= 1;
                return root.right;
            } else if (root.right == null) {
                this.size -= 1;
                return root.left;
            } else {
                Node<T> largest = root.left;

                // find largest of the left subtree
                while (largest.right != null) {
                    largest = largest.right;
                }

                root.item = largest.item;
                root.left = remove(root.left, largest.item);
                return root;
            }
        }
    }

    @Override
    public void remove(Object key) {
        remove(root, key);
    }

    private T search(Node<T> root, Object key) {
        if (root == null) {
            return null;
        }
        if (root.item.equals(key)) {
            return root.item;
        }
        T leftRes = search(root.left, key);
        if (leftRes != null) {
            return leftRes;
        }

        T rightRes = search(root.right, key);

        return rightRes;
    }

    @Override
    public T search(Object key) {
        return search(this.root, key);
    }

    @Override
    public int size() {
        return this.size;
    }

    public String toString() {
        return this.root.toString();
    }

    public void println() {
        System.out.println(this.toString());
    }

    private void ascendingList(Node<T> root, List<T> list) {
        if (root == null) {
            return;
        }
        ascendingList(root.left, list);
        list.add(root.item);
        ascendingList(root.right, list);
    }

    @Override
    public List<T> ascendingList() {
        List<T> list = new DLinkedList<>();
        ascendingList(root, list);
        return list;
    }

    private void descendingList(Node<T> root, List<T> list) {
        if (root == null) {
            return;
        }
        descendingList(root.right, list);
        list.add(root.item);
        descendingList(root.left, list);
    }

    @Override
    public List<T> descendingList() {
        List<T> list = new DLinkedList<>();
        descendingList(root, list);
        return list;
    }

    @Override
    public List<T> dfs() {
        List<T> list = new DLinkedList<>();
        Stack<Node<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()) {
            Node<T> node = stack.pop();
            list.add(node.item);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        return list;
    }

    @Override
    public List<T> bfs() {
        List<T> list = new DLinkedList<>();

        Queue<Node<T>> queue = new Queue<>();
        queue.push(root);
        while (!queue.empty()) {
            Node<T> node = queue.pop();
            list.add(node.item);
            if (node.left != null) {
                queue.push(node.left);
            }
            if (node.right != null) {
                queue.push(node.right);
            }
        }
        return list;
    }

    private void nlr(Node<T> root, List<T> list) {
        if (root == null) {
            return;
        }
        list.add(root.item);
        nlr(root.left, list);
        nlr(root.right, list);
    }

    private void lrn(Node<T> root, List<T> list) {
        if (root == null) {
            return;
        }
        lrn(root.left, list);
        lrn(root.right, list);
        list.add(root.item);
    }

    private void lnr(Node<T> root, List<T> list) {
        if (root == null) {
            return;
        }
        lnr(root.left, list);
        list.add(root.item);
        lnr(root.right, list);
    }

    @Override
    public List<T> nlr() {
        List<T> list = new DLinkedList<>();
        nlr(root, list);
        return list;
    }

    @Override
    public List<T> lrn() {
        List<T> list = new DLinkedList<>();
        lrn(root, list);
        return list;
    }

    @Override
    public List<T> lnr() {
        List<T> list = new DLinkedList<>();
        lnr(root, list);
        return list;
    }

    //
    public class Node<T> {

        T item;
        Node<T> left, right;

        public Node(T data, Node<T> left, Node<T> right) {
            this.item = data;
            this.left = left;
            this.right = right;
        }

        public String toString() {
            String desc = "";
            if (this.left == null && this.right == null) {
                desc = String.format("(%s)", item);
            }
            if (this.left == null && this.right != null) {
                desc = String.format("(%s () %s)", item, this.right.toString());
            }
            if (this.left != null && this.right == null) {
                desc = String.format("(%s %s ())", item, this.left.toString());
            }
            if (this.left != null && this.right != null) {
                desc = String.format("(%s %s %s)", item,
                        this.left.toString(), this.right.toString());
            }
            return desc;
        }
    }
}
