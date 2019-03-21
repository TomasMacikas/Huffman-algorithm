import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class CanonicalCode {

    private int symbolLimit = HuffmanCompress.symbolLimit+1;
    private int[] codeLengths =  new int[symbolLimit];

    public CanonicalCode(CodeTree tree) {
        buildCodeLengths(tree.root, 0);
    }


    // Recursive helper method for the above constructor.
    private void buildCodeLengths(Node node, int depth) { //Rekursyviai ieskoma medzio kiekvienos reiksmes gylio
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode)node; //node casting to internalnode
            buildCodeLengths(internalNode.leftChild , depth + 1);//Einama i kaire puse. Prie gylio pridedamas 1
            buildCodeLengths(internalNode.rightChild, depth + 1);//Einama i desine puse. Prie gylio pridedamas 1
        } else if (node instanceof Leaf) {
            int symbol = ((Leaf)node).symbol; //Gaunamas to lapo, iki kurio atkeliavom, reiksme
            codeLengths[symbol] = depth; //Priskiriamas gylis reiksmei
        } else {
            System.out.println("Error. Node is nor a leaf nor a internalNode");
        }
    }



    /*---- Various methods ----*/
    /**
     * Returns the code length of the specified symbol value. The result is 0
     * if the symbol has node code; otherwise the result is a positive number.
     * @param symbol the symbol value to query
     * @return the code length of the symbol, which is non-negative
     * @throws IllegalArgumentException if the symbol is out of range
     */
    public int getCodeLength(int symbol) {
        if (symbol < 0 || symbol >= codeLengths.length)
            throw new IllegalArgumentException("Symbol out of range");
        return codeLengths[symbol];
    }


    /**
     * Returns the canonical code tree for this canonical Huffman code.
     * @return the canonical code tree
     */
    public CodeTree toCodeTree() {
        List<Node> nodes = new ArrayList<Node>();
        for (int i = max(codeLengths); i >= 0; i--) {  // Descend through code lengths
            List<Node> newNodes = new ArrayList<Node>();

            // Add leaves for symbols with positive code length i
            if (i > 0) {
                for (int j = 0; j < codeLengths.length; j++) {
                    if (codeLengths[j] == i)
                        newNodes.add(new Leaf(j)); //Isidedame ASCII reiksmes ilgiausio kodo
                }
            }

            // Merge pairs of nodes from the previous deeper layer
            for (int j = 0; j < nodes.size(); j += 2) {
                newNodes.add(new InternalNode(nodes.get(j), nodes.get(j + 1))); //Sujungia lapus i InternalNodes
            }
            nodes = newNodes;
        }
        return new CodeTree((InternalNode)nodes.get(0), codeLengths.length);
    }


    // Returns the maximum value in the given array, which must have at least 1 element.
    private static int max(int[] array) {
        int result = array[0];
        for (int x : array)
            result = Math.max(x, result);
        return result;
    }


    //DECOMPRESS
    public CanonicalCode(int[] codeLens) {
        // Check basic validity
        Objects.requireNonNull(codeLens);
        if (codeLens.length < 2)
            throw new IllegalArgumentException("At least 2 symbols needed");
        for (int cl : codeLens) {
            if (cl < 0)
                throw new IllegalArgumentException("Illegal code length");
        }

        // Copy once and check for tree validity
        codeLengths = codeLens.clone();
        Arrays.sort(codeLengths);
        int currentLevel = codeLengths[codeLengths.length - 1];
        int numNodesAtLevel = 0;
        for (int i = codeLengths.length - 1; i >= 0 && codeLengths[i] > 0; i--) {
            int cl = codeLengths[i];
            while (cl < currentLevel) {
                if (numNodesAtLevel % 2 != 0)
                    throw new IllegalArgumentException("Under-full Huffman code tree");
                numNodesAtLevel /= 2;
                currentLevel--;
            }
            numNodesAtLevel++;
        }
        while (currentLevel > 0) {
            if (numNodesAtLevel % 2 != 0)
                throw new IllegalArgumentException("Under-full Huffman code tree");
            numNodesAtLevel /= 2;
            currentLevel--;
        }
        if (numNodesAtLevel < 1)
            throw new IllegalArgumentException("Under-full Huffman code tree");
        if (numNodesAtLevel > 1)
            throw new IllegalArgumentException("Over-full Huffman code tree");

        // Copy again
        System.arraycopy(codeLens, 0, codeLengths, 0, codeLens.length);
    }


}