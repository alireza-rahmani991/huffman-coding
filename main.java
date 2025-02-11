

public class main {
    public static void main(String[] args){
        String textFilePath = "inputAndOutputs/text.txt";
        String encodedFilePath = "inputAndOutputs/encoded.hff";
        String decodedFilePath = "inputAndOutputs/decoded.txt";
        
        // huffman encode = new huffman(textFilePath, encodedFilePath );
        huffman decode = new huffman(encodedFilePath, decodedFilePath );


    }
}
