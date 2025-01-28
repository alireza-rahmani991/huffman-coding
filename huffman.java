import java.util.HashMap;
import java.util.Map;

public class huffman {
    public Node root;
    String text;
    Map<String, Integer> charCountMap;
    priorityQueue queue;

    public huffman(String text){
        this.text = text;
        queue = new priorityQueue();
        charCountMap = countCharecters(this.text);


        for (Map.Entry<String, Integer> entry : charCountMap.entrySet()) {
            queue.enqueue(new Node(entry.getValue(), entry.getKey()));
        }
        // queue.print();

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
    }

    private Map<String, Integer> countCharecters(String textToCount){
        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < textToCount.length(); i++) {
            String charAsString = String.valueOf(text.charAt(i));
            count.put(charAsString, count.getOrDefault(charAsString, 0) + 1);
        }
        return count;
    }
}
