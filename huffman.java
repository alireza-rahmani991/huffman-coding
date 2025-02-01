import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.BitSet;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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



        encodeFile();



        for (Map.Entry<String, Integer> entry : charCountMap.entrySet()) {

            System.out.println("Character: " + entry.getKey() + ", occurence: " + entry.getValue());
            
        }
        System.out.println();
        for (Map.Entry<String, String> entry : charCodes.entrySet()) {
            System.out.println("Character: " + entry.getKey() + ", code: " + entry.getValue());
        }

    }


    public huffman(String pathEncodedFile){
        outputPath = "decoded.txt";
        try(FileInputStream fileInputStream = new FileInputStream(pathEncodedFile);  ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
            decodeFile(objectInputStream);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void encodeFile(){
        countCharecters();
        
        root = createTree(charCountMap);
        
        charCodes = codeCharecters(root,charCodes, "" );


        try(FileOutputStream fileOutputStream = new FileOutputStream(outputPath); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
            objectOutputStream.writeObject(charCodes);
            encodeText(objectOutputStream);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
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

    private void encodeText(ObjectOutputStream objectOutputStream){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path)); BufferedWriter writer = new BufferedWriter(new FileWriter("huffmanText.txt"))){
            String line;
            while((line = bufferedReader.readLine()) != null){
                BitSet bitSet = new BitSet(line.length());
                int bitIndex = 0;
                for(int i = 0; i < line.length(); i++) {
                    String charAsString = String.valueOf(line.charAt(i));
                    String code = charCodes.get(charAsString);
                    for(char bit : code.toCharArray()) {
                        if (bit == '1') {
                            bitSet.set(bitIndex); 
                        }
                        bitIndex++;
                    }
                }
                for(int i = 0; i < bitIndex; i++){
                    writer.write(bitSet.get(i) ? '1' : '0');
                }
                writer.write("\n");
                objectOutputStream.writeObject(bitSet);
                objectOutputStream.writeInt(bitIndex);
            }
            objectOutputStream.writeObject(null);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }

    private void decodeFile(ObjectInputStream objectInputStream){
        try{
            Map<String , String> decodingChars = new HashMap<>();
            decodingChars = (Map<String, String> )objectInputStream.readObject();
            Map<String , String> invertedHashMap = new HashMap<>();
            for (Map.Entry<String, String> entry : decodingChars.entrySet()) {
                invertedHashMap.put(entry.getValue(), entry.getKey());
            }
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))){
                while(true){
                    BitSet bitSet = (BitSet) objectInputStream.readObject();
                    if (bitSet == null){
                        break;
                    }  

                    int bitLength = objectInputStream.readInt();
                    StringBuilder binaryString = new StringBuilder();
                    for (int i = 0; i < bitLength; i++) {
                        binaryString.append(bitSet.get(i) ? "1" : "0");
                    }
        
                    
                    StringBuilder decodedLine = new StringBuilder();
                    StringBuilder currentCode = new StringBuilder();
        
                    for (char bit : binaryString.toString().toCharArray()) {
                        currentCode.append(bit);
        
                        
                        if (invertedHashMap.containsKey(currentCode.toString())) {
                            decodedLine.append(invertedHashMap.get(currentCode.toString()));
                            currentCode.setLength(0);
                        }
                    }
                
                
                    writer.write(decodedLine.toString());
                    writer.write('\n');
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }

        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
