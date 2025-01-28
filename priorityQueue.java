import java.util.ArrayList;

public class priorityQueue {
    private ArrayList<Node> queue;
    
    public priorityQueue(){
        queue = new ArrayList<>();
    }

    public void enqueue(Node node){
        queue.add(node);
    }

    public Node dequeue(){
        if(queue.size() == 0){
            System.out.println("queue is empty");
            return null;
        }
        Node min = queue.get(0);
        for(Node node : queue){
            if(node.getCount() < min.getCount()){
                min = node;
            }
        }
        queue.remove(min);
        return min;
    }

    public void print(){
        for(Node node : queue){
            System.out.println(node.getCharecter() + " : " + node.getCount());
        }
    }

    public int getSize(){
        return queue.size();
    }
}
