package heap;

import model.SimilarUser;

public class Node {

    private SimilarUser data;

    private Node left;
    private Node right;

    private Node parent;

    public Node(SimilarUser data) {
        this.data = data;
    }

    public SimilarUser getData() {
        return data;
    }

    public void setData(SimilarUser data) {
        this.data = data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}