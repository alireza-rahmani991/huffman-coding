import java.util.HashMap;
import java.util.Map;

public class huffman {
    public Node root;
    public String text;
    public Map<String, Integer> charCountMap;
    public Map<String, String> charCodes;
    public priorityQueue queue;
    public String codedText;

    public huffman(String text){
        this.text = text;
        queue = new priorityQueue();
        charCountMap = countCharecters(this.text);
        charCodes = new HashMap<>();

        for (Map.Entry<String, Integer> entry : charCountMap.entrySet()) {
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
        root = queue.dequeue();
        
        
        charCodes = codeCharecters(root,charCodes, "" );

        codedText = "";
        encodeText();
        System.out.println(codedText);
        for (Map.Entry<String, String> entry : charCodes.entrySet()) {
            System.out.println("Character: " + entry.getKey() + ", Count: " + entry.getValue());
        }

        decodeText();
    }

    private Map<String, Integer> countCharecters(String textToCount){
        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < textToCount.length(); i++) {
            String charAsString = String.valueOf(text.charAt(i));
            count.put(charAsString, count.getOrDefault(charAsString, 0) + 1);
        }
        return count;
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
        for (int i = 0; i < text.length(); i++) {
            String charAsString = String.valueOf(text.charAt(i));
            codedText = codedText + charCodes.get(charAsString);
        }
    }

    private void decodeText(){
        Node temp = root;
        
        for (int i = 0; i < codedText.length(); i++) {
            String charAsString = String.valueOf(codedText.charAt(i));
            if(charAsString.equals("0") && temp.getLeft() != null){
                temp = temp.getLeft();
            }
            else if(charAsString.equals("1") && temp.getRight() != null){
                temp = temp.getRight();
            }
            if(temp.getLeft() == null && temp.getRight() == null){
                System.out.print(temp.getCharecter());
                temp = root;
            }
        }
    }
}
