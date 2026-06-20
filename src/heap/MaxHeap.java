package heap;

import model.SimilarUser;
import java.util.LinkedList;
import java.util.Queue;

public class MaxHeap {
    private Node root;
    private int size;

    public MaxHeap() {
        this.root = null;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(SimilarUser data) {
        Node newNode = new Node(data);

        if (root == null) {
            root = newNode;
            size++;
            return;
        }

        Node parent = findInsertionParent();

        if (parent.getLeft() == null) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }

        newNode.setParent(parent);
        size++;

        heapifyUp(newNode);
    }

    public SimilarUser removeMax() {
        if (root == null) {
            return null;
        }

        SimilarUser maxData = root.getData();

        if (size == 1) {
            root = null;
            size--;
            return maxData;
        }

        Node lastNode = findLastNode();

        root.setData(lastNode.getData());

        Node parent = lastNode.getParent();

        if (parent.getLeft() == lastNode) {
            parent.setLeft(null);
        } else {
            parent.setRight(null);
        }

        size--;

        heapifyDown(root);

        return maxData;
    }

    private Node findInsertionParent() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.getLeft() == null || current.getRight() == null) {
                return current;
            }

            queue.add(current.getLeft());
            queue.add(current.getRight());
        }

        return null;
    }

    private Node findLastNode() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        Node current = null;

        while (!queue.isEmpty()) {
            current = queue.poll();

            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }

            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }

        return current;
    }

    private void heapifyUp(Node node) {
        while (node.getParent() != null &&
                node.getData().getSimilarity() > node.getParent().getData().getSimilarity()) {

            SimilarUser temp = node.getData();
            node.setData(node.getParent().getData());
            node.getParent().setData(temp);

            node = node.getParent();
        }
    }

    private void heapifyDown(Node node) {
        while (node != null) {
            Node largest = node;

            if (node.getLeft() != null &&
                    node.getLeft().getData().getSimilarity() > largest.getData().getSimilarity()) {
                largest = node.getLeft();
            }

            if (node.getRight() != null &&
                    node.getRight().getData().getSimilarity() > largest.getData().getSimilarity()) {
                largest = node.getRight();
            }

            if (largest == node) {
                break;
            }

            SimilarUser temp = node.getData();
            node.setData(largest.getData());
            largest.setData(temp);

            node = largest;
        }
    }
}