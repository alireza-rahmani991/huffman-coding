// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;

public class main {
    public static void main(String[] args){
        huffman code = new huffman("aaaaaabbxxdsa");
        System.out.println(code.root.getCharecter() + " : " + code.root.getCount());
        
    }
}
