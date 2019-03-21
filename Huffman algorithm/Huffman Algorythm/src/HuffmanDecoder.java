import java.io.IOException;
import java.util.Objects;


/**
 * Reads from a Huffman-coded bit stream and decodes symbols. Not thread-safe.
 * @see HuffmanEncoder
 */
public final class HuffmanDecoder {

    /*---- Fields ----*/

    // The underlying bit input stream (not null).
    private BitInputStream input;

    /**
     * The code tree to use in the next {@link#read()} operation. Must be given a non-{@code null}
     * value before calling read(). The tree can be changed after each symbol decoded, as long
     * as the encoder and decoder have the same tree at the same point in the code stream.
     */
    public CodeTree codeTree;



    /*---- Constructor ----*/

    /**
     * Constructs a Huffman decoder based on the specified bit input stream.
     * @param in the bit input stream to read from
     * @throws NullPointerException if the input stream is {@code null}
     */
    public HuffmanDecoder(BitInputStream in) {
        input = Objects.requireNonNull(in);
    }



    /*---- Method ----*/


    public int read() throws IOException {
        if (codeTree == null)
            throw new NullPointerException("Code tree is null");

        InternalNode currentNode = codeTree.root;
        while (true) {
            int temp = input.readNoEof();
            //System.out.println(temp);
            Node nextNode;
            if      (temp == 0) nextNode = currentNode.leftChild;
            else if (temp == 1) nextNode = currentNode.rightChild;
            else throw new AssertionError("Invalid value from readNoEof()");

            if (nextNode instanceof Leaf){
                return ((Leaf)nextNode).symbol;
            }
            else if (nextNode instanceof InternalNode)
                currentNode = (InternalNode)nextNode;
            else
                throw new AssertionError("Illegal node type");
        }
    }

}