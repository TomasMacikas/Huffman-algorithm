// Helper structure for buildCodeTree()
public class NodeWithFrequency implements Comparable<NodeWithFrequency> {

    public final Node node;
    public final int lowestSymbol;
    public final long frequency;  // Using wider type prevents overflow


    public NodeWithFrequency(Node nd, int lowSym, long freq) {
        node = nd;
        lowestSymbol = lowSym;
        frequency = freq;
    }


    // Sort by ascending frequency, breaking ties by ascending symbol value.
    public int compareTo(NodeWithFrequency other) {
        if (frequency < other.frequency)
            return -1;
        else if (frequency > other.frequency)
            return 1;
        else if (lowestSymbol < other.lowestSymbol)
            return -1;
        else if (lowestSymbol > other.lowestSymbol)
            return 1;
        else
            return 0;
    }

}