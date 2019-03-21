import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Testing {
    public static void main(String[] args) throws IOException {
        String[] compress = new String[3];
        String[] decompress = new String[2];

        String fileName = "pav3.png";
        Path path = Paths.get("C:/Users/Tomas/IdeaProjects/Huffman", fileName);
        byte[] f1 = Files.readAllBytes(path);
        compress[1] = fileName;
        for(int k = 2; k < 24; ++k){
            String compressedName = "compressed" + Integer.toString(k);
            String decompressedName = "decompressed" + Integer.toString(k) + ".png";

            compress[0] = Integer.toString(k);
            compress[2] = compressedName;
            HuffmanCompress.main(compress);

            Path path2 = Paths.get("C:/Users/Tomas/IdeaProjects/Huffman", compressedName);
            byte[] f2 = Files.readAllBytes(path2);

            decompress[0] = compressedName;
            decompress[1] = decompressedName;
            HuffmanDecompress.main(decompress);
            //Check if files are same
            Path path3 = Paths.get("C:/Users/Tomas/IdeaProjects/Huffman", decompressedName);
            byte[] f3 = Files.readAllBytes(path3);
            if(Arrays.equals(f1, f3)){
                System.out.println("With " + k + " bits " + "Pre-compressed/compressed " + f1.length + "/" + f2.length + " Passed");
            }
            else System.out.println("With " + k + " bits " + "Pre-compressed/compressed " + f1.length + "/" + f2.length + " FAILED");
        }
    }
}