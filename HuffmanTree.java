import java.util.Map;

public class HuffmanTree {
    private  Node root;

    public HuffmanTree(Map<String, Integer> counts){
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
        root = queue.dequeue();
    }

    public Node getRoot(){
        return root;
    }
}
