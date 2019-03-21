import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanCompress {
    public static int symbolLimit;
    public static int bits;

    public static void main(String[] args) throws IOException {



//        System.out.println(args[0] + " " + args[1] + " " + args[2]);
//        //Required two files: input and output
//        if (args.length != 3) {
//            System.out.println("Error: arguments not specified");
//            System.exit(1);
//            return;
//        } else if(Integer.valueOf(args[0]) > 24){
//            System.out.println("Error: word too long");
//            System.exit(1);
//        }
        String inputFileName = "";
        String outputFileName = "";
        try {
            // FOR TESTING
            if(args.length == 3){
                bits = Integer.valueOf(args[0]);
                inputFileName = args[1];
                outputFileName = args[2];
            }
            else {
                Scanner c = new Scanner(System.in);
                System.out.println("Iveskite zodzio ilgi");
                bits = c.nextInt();
                if (bits > 24) {
                    System.out.println("Error: word too long: ");
                    System.exit(1);
                }
                System.out.println("Iveskite nuskaitoma faila: ");
                inputFileName = c.next();
                System.out.println("Iveskite faila i kuri bus irasomas suspaustas kodas: ");
                outputFileName = c.next();
                c.close();
            }

            symbolLimit = (int)Math.pow(2,bits);
            //System.out.println(symbolLimit);
            File inputFile = new File(inputFileName);
            File outputFile = new File(outputFileName);

            Frequencies freqs = Frequencies.getFrequencies(inputFile, bits);//Gaunami faile esanciu simboliu dazniai
            freqs.increment(symbolLimit);//symbolLimit - EOF, pridedamas EOF prie dazniu lenteles

            //freqs.printFreqs();


            CodeTree code = CodeTree.buildCodeTree(freqs); //Sudaromas kodu medis


            //System.out.println("length: "+freqs.frequencies.length);
            CanonicalCode canonCode = new CanonicalCode(code); //suskaiciuoja simboliu gylius
            code = canonCode.toCodeTree();


            try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)))) {
                try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                    writeFirstByte(bits, out);
                    writeCodeLengthTable(out, canonCode);
                    writeValue((int) Math.pow(2, bits) + 1, bits +1, out);
                    compress(code, in, out);
                }
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static void writeCodeLengthTable(BitOutputStream out, CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < symbolLimit+1; i++) {
            int val = canonCode.getCodeLength(i);

            if(val > 0) {
                for (int j = bits+1 -1; j >= 0; j--) {
                    out.write((i >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
                    //System.out.println("Bit: "+((val >>> j) & 1));
                    //System.out.println("Bit: "+((val >>> j) & 1));
                }
                for (int j = bits+1-1; j >= 0; j--) {
                    out.write((val >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
                    //System.out.println("Bit: "+((val >>> j) & 1));
                }
            }
        }
    }

    static void compress(CodeTree code, BitInputStream in, BitOutputStream out) throws IOException {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        int val = 0;
        int bit = 0;
        int count = 0;
        while (true) {
            val = 0;
            bit = 0;
            count = 0;
            for (int j = 0; j < bits; j++){
                bit = in.read();
                if(bit == -1) {
                    break;
                }
                count++;
                val = (val << 1) | bit;
            }

            if (bit == -1)
            {
                break;
            }
            enc.write(val);
            //System.out.println("val " + val);
        }
        enc.write(symbolLimit);  // EOF
        writeFirstByte(count, out);

        if(val > 0) {
             //val = (val << 1) | 0;
            //System.out.println("not full val " + val);
            for (int j = 0; j < count; j++){
                out.write((val >>> j) & 1);
            }
        }

    }
    static public void writeFirstByte(int val, BitOutputStream out)throws IOException{
        for (int j = 8-1; j >= 0; j--) {
            out.write((val >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
            //System.out.println("Bit: "+((val >>> j) & 1));
        }
    }
    static public void writeValue(int val, int bits, BitOutputStream out)throws IOException{
        for (int j = bits-1; j >= 0; j--) {
            out.write((val >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
            //System.out.println("Bit: "+((val >>> j) & 1));
        }
    }
}