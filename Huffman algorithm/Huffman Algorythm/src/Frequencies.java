import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Frequencies {

    public int[] frequencies = new int[HuffmanCompress.symbolLimit+1];

    public static Frequencies getFrequencies(File file, int bits) throws IOException {

        Frequencies freqs = new Frequencies();
        BitInputStream input = new BitInputStream(new BufferedInputStream(new FileInputStream(file)));//Atsidaromas failas binariniam nuskaitymui

        while(true){
            // For this file format, we read 8 bits in big endian
            int bit = 0;
            int val = 0;
            for (int j = 0; j < bits; j++){
                bit = input.read();
                if(bit == -1)break;
                val = (val << 1) | bit;
            }
            if(bit == -1){
//                if(val > 0){
//                    System.out.println("not full val freq " + val);
//                    freqs.increment(val);
//                }
                break;
            }
            //System.out.println(val);
            freqs.increment(val);
        }
        System.out.println("*****");

//        while (true) {
//            byte[] arr = new byte[bits];
//            int val = 0;
//            int b = input.read();//Nuskaitoma po 8 bitus, kuri atitinka ASCII kodu reiksmes
//            //System.out.println(b);
//            //val - bitai
//            for (int i = bits; i > 0; i--){
//                val = getBit(b,i-1);
//            }
//            // System.out.println(val);
//            if (b == -1) { //Kai pasiekiama failo pabaiga
//                break;
//            }
//            freqs.increment(b); //Pridedamas +1 prie simbolio daznio
//        }
        return freqs;
    }

    public void increment(int symbol) {
        //checkSymbol(symbol);
        frequencies[symbol]++;
    }

//    private void checkSymbol(int symbol) {
//        if (symbol < 0 || symbol >= 257) {
//            throw new IllegalArgumentException("Illegal symbol. Symbol out of range.");
//        }
//    }

    public void printFreqs(){ // funkcija isspauzdinti dazniu lentele
        for (int i = 0; i< HuffmanCompress.symbolLimit+1; i++){
            System.out.println(frequencies[i]);
        }

    }

    static int getBit(int n, int k) {
        return (n >> k) & 1;
    }

}
