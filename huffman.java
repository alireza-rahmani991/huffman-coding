import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.BitSet;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;

public class huffman {
    public Node root;
    String path;
    String outputPath;
    public Map<String, Integer> charCountMap;
    public Map<String, String> charCodes;

    public huffman(String path, String outputPath){
        charCountMap = new HashMap<>();
        this.path = path;
        this.outputPath = outputPath;
        charCodes = new HashMap<>();
        String fileFormatInput = path.substring(path.length() - 3);
        String fileFormatOutput = outputPath.substring(outputPath.length() - 3);

        if(fileFormatInput.equals("txt") && fileFormatOutput.equals("hff")){
            encodeFile();
            for (Map.Entry<String, Integer> entry : charCountMap.entrySet()) {
                if(entry.getKey() == "\n"){
                    System.out.println("Character: new line" + ", occurence: " + entry.getValue());
                }
                else{
                    System.out.println("Character: " + entry.getKey() + ", occurence: " + entry.getValue());
                }
                
            }
            System.out.println();
            for (Map.Entry<String, String> entry : charCodes.entrySet()) {
                if(entry.getKey().equals("\n")){
                    System.out.println("Character: new line" + ", code: " + entry.getValue());
                }else{
                    System.out.println("Character: " + entry.getKey() + ", code: " + entry.getValue());
                }
                
            }
        }


        if(fileFormatInput.equals("hff") && fileFormatOutput.equals("txt")){

            decodeFile();

            for (Map.Entry<String, String> entry : charCodes.entrySet()) {
                if(entry.getKey().equals("\n")){
                    System.out.println("Character: new line" + ", code: " + entry.getValue());
                }else{
                    System.out.println("Character: " + entry.getKey() + ", code: " + entry.getValue());
                }
                
            }
        }

        

    }

    private void encodeFile(){
        countCharecters();
        
        root = createTree(charCountMap);
        
        charCodes = codeCharecters(root,charCodes, "" );


        encodeText();
        
    }



    private void countCharecters(){
        
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
            String line = bufferedReader.readLine();
            while(line  != null){
                for (int i = 0; i < line.length(); i++) {
                    String charAsString = String.valueOf(line.charAt(i));
                    charCountMap.put(charAsString, charCountMap.getOrDefault(charAsString, 0) + 1);
                }
                line = bufferedReader.readLine();
                if(line != null){
                    charCountMap.put("\n", charCountMap.getOrDefault("\n", 0) + 1);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        

    }


    private Node createTree(Map<String, Integer> counts){
        priorityQueue queue = new priorityQueue();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            queue.enqueue(new Node(entry.getValue(), entry.getKey()));
        }
        

        while(queue.getSize() > 1){
            Node node1 = queue.dequeue();
            Node node2 = queue.dequeue();

            Node res = new Node(node1.getCount() + node2.getCount(), node1.getCharecter() + node2.getCharecter());
            if(node1.getCount() > node2.getCount()){
                res.setLeft(node2);
                res.setRight(node1);
            }
            else{
                res.setLeft(node1);
                res.setRight(node2);
            }
            queue.enqueue(res);
        }
        Node rootNode = queue.dequeue();
        return rootNode;
    }

    private Map<String, String> codeCharecters(Node root, Map<String, String> codes, String code){
        if(root.getRight() == null && root.getLeft() == null){
            codes.put(root.getCharecter(), code);
            return codes;
        }

        codes = codeCharecters(root.getLeft(), codes, code + "0");
        codes = codeCharecters(root.getRight(), codes, code + "1");
        return codes;
    }

    private void encodeText(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path)); 
        FileOutputStream fileOutputStream = new FileOutputStream(outputPath)){

            writeHeaders(fileOutputStream);

            BitOutputStream bits = new BitOutputStream(fileOutputStream);
            String line = bufferedReader.readLine();
            while (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    String ch = String.valueOf(line.charAt(i));
                    String code = charCodes.get(ch);
                    bits.writeMultipleBits(code);
                }
                line = bufferedReader.readLine();
                if(line != null){
                    String code = charCodes.get("\n");
                    bits.writeMultipleBits(code);
                }
            }
            bits.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    private void writeHeaders(OutputStream outputStream){
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try{
            dataOutputStream.writeInt(charCodes.size());
            for(Map.Entry<String, String> entry : charCodes.entrySet()){
                String charecter = entry.getKey();
                String code = entry.getValue();
                dataOutputStream.writeChar(charecter.charAt(0));

                dataOutputStream.writeInt(code.length());

                dataOutputStream.writeUTF(code);
            }
            dataOutputStream.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }

    

    private void decodeFile(){
        try(FileInputStream fileInputStream = new FileInputStream(path)){
            
            charCodes = new HashMap<>();

            DataInputStream dataInputStream = new DataInputStream(fileInputStream);

            int numEntry = dataInputStream.readInt();
            for(int i = 0;i < numEntry; i++){
                char character = dataInputStream.readChar();
                int codeLen = dataInputStream.readInt();
                String code = dataInputStream.readUTF();
                charCodes.put(String.valueOf(character), code);
            }

            
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            
            Map<String, String> invertedHasMap = new HashMap<>();
            for (Map.Entry<String, String> entry : charCodes.entrySet()) {
                invertedHasMap.put(entry.getValue(), entry.getKey());
            }

            BitInputStream bits = new BitInputStream(dataInputStream);
            
            StringBuilder currentCode = new StringBuilder();
            int bit;
            while((bit = bits.readBit()) != -1){
                currentCode.append(bit);

                if(invertedHasMap.containsKey(currentCode.toString())){
                    String character = invertedHasMap.get(currentCode.toString());

                    writer.write(character);

                    currentCode.setLength(0);
                }
            }
            bits.close();
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
