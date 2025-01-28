public class Node {
    private Node left;
    private Node right;
    private int count;
    private String charecter;

    public Node(int count, String charecter){
        this.charecter = charecter;
        this.count = count;

        left = null;
        right = null;
    }

    public void setLeft(Node node){
        left = node;
    }

    public void setRight(Node node){
        right = node;
    }

    public Node getLeft(){
        return left;
    }

    public Node getRight(){
        return right;
    }


    public int getCount(){
        return count;
    }

    public String getCharecter(){
        return charecter;
    }
}
