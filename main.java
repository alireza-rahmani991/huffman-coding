public class main {
    public static void main(String[] args){
        String textFilePath = "inputAndOutputs/text.txt";
        String encodedFilePath = "inputAndOutputs/encoded.hff";
        String decodedFilePath = "inputAndOutputs/decoded.txt";
        
        // huffman code = new huffman(textFilePath, encodedFilePath );
        huffman code = new huffman(encodedFilePath, decodedFilePath );


    }
}
