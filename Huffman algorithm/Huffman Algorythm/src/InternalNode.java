
public final class InternalNode extends Node {

    public final Node leftChild;  // Not null

    public final Node rightChild;  // Not null



    public InternalNode(Node left, Node right) {
        leftChild = left;
        rightChild = right;
    }

}