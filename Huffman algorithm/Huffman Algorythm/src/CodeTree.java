import java.util.*;

public class CodeTree {
    public final InternalNode root;
    private List<List<Integer>> codes;

    public CodeTree(InternalNode root, int symbolLimit) {
        this.root = root; //nukopijuojama saknis

        codes = new ArrayList<List<Integer>>(); // ArrayList<ArrayList<Integer>>
        //atsilaisvinama vietos
        for (int i = 0; i < symbolLimit; i++) {
            codes.add(null);
        }

        buildCodeList(root, new ArrayList<Integer>());  // Fill 'codes' with appropriate data
    }

    public static CodeTree buildCodeTree(Frequencies freqs) {

        Queue<NodeWithFrequency> pqueue = new PriorityQueue<NodeWithFrequency>();

        // Sukuriami lapai ne nulinems reiksmems turintiems dazniams
        for (int i = 0; i < freqs.frequencies.length; i++) {
            if (freqs.frequencies[i] > 0)
                pqueue.add(new NodeWithFrequency(new Leaf(i), i, freqs.frequencies[i]));//Sukuriamas lapas
        }
//**************************************************************************************
         //Apsauga. Turi buti bent du lapai
        for (int i = 0; i < freqs.frequencies.length && pqueue.size() < 2; i++) {
            //System.out.println("TEST1");
            if (freqs.frequencies[i] == 0)
                //System.out.println("TEST2");
                pqueue.add(new NodeWithFrequency(new Leaf(i), i, 0));//Pridedamas lapas su nulio danznius, jeigu egzistuoja maziau nei du lapai
        }
//*******************************************************************************

        // Repeatedly tie together two nodes with the lowest frequency
        while (pqueue.size() > 1) {//while vyksta iki tol, kol lieka vienas vidinis node su didziausiu dazniu
            //Pasiemami du lapai su maziausiais dazniais
            NodeWithFrequency x = pqueue.remove();
            NodeWithFrequency y = pqueue.remove();
            //Lapai sujungiami i vidini node
            pqueue.add(new NodeWithFrequency(
                    new InternalNode(x.node, y.node),//Paduodami left ir right vaikai
                    Math.min(x.lowestSymbol, y.lowestSymbol),//Pasiemama mazense ASCII reiksme
                    x.frequency + y.frequency));//Dazniai sudedami
            //System.out.println("x: "+x.lowestSymbol);
            //System.out.println("y: "+y.lowestSymbol);
        }

        //Remaining node
        InternalNode node_ = (InternalNode)pqueue.remove().node;//Root
        CodeTree code = new CodeTree(node_, freqs.frequencies.length); //Sudaromai simboliu kodai

        return code;
    }

    //Funkicija sudaro kodus rekursyviai eidama gilyn.
    private void buildCodeList(Node node, List<Integer> prefix) {
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node; //cast because Node class is abstract

            prefix.add(0);//prie kodo pridedamas 0
            buildCodeList(internalNode.leftChild, prefix);//Einama i kaire
            prefix.remove(prefix.size() - 1);//Pasiekus lapa, einama vienu zingsniu atgal

            prefix.add(1);//prie kodo pridedamas 1
            buildCodeList(internalNode.rightChild, prefix);//Einama i desine
            prefix.remove(prefix.size() - 1);//pasiekus lapa, einama vienu zingsniu atgal

        } else if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;

            if (leaf.symbol >= codes.size()) {
                throw new IllegalArgumentException("Symbol exceeds symbol limit");
            }
            if (codes.get(leaf.symbol) != null) {
                throw new IllegalArgumentException("Symbol has more than one code");
            }
            //System.out.println(prefix);
            codes.set(leaf.symbol, new ArrayList<Integer>(prefix));//Pasiekus lapa, irasomas to simbolio kodas
            //System.out.println("leaf.symbol: "+leaf.symbol);
            //System.out.println("prefix: "+prefix);

        } else {
            System.out.println("Error");
        }
    }

    public List<Integer> getCode(int symbol) {
        if (symbol < 0)
            throw new IllegalArgumentException("Illegal symbol");
        else if (codes.get(symbol) == null) {
            System.out.println(Integer.toString(symbol) + codes.get(symbol));
            throw new IllegalArgumentException("No code for given symbol");
        }
        else
            return codes.get(symbol);
    }
}
