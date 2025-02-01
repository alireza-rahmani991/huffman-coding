import java.io.*;

public class main {
    public static void main(String[] args){
        String textFilePath = "text.txt";
        String encodedFilePath = "encoded.hff";
        String decodedFilePath = "decoded.txt";
        
        // huffman code = new huffman(textFilePath, encodedFilePath );
        huffman code = new huffman(encodedFilePath, decodedFilePath );


    }
}
