import java.util.ArrayList;
import java.util.HashMap;


class Node {
    /**
     * Die Haeufigkeit eines Wortes.
     * Default Wert ist 0 (falls nicht Ende des Wortes)
     */
    public int n;
    public int index;
    /**
     * Eine Liste mit allen Kinderknoten
     */
    public ArrayList<Node> child = new ArrayList<>();
    /**
     * Elternknoten fuer Satzbildung am Ende der Suche
     */
    public Node parent = null;
    public Node(int idx){
        this.index = idx;
        this.n = 0;
    }
    public Node(int idx, Node parent){
        this.index = idx;
        this.n = 0;
        this.parent = parent;
    }

    public boolean has(int index){
        for(Node n : child){
            if(n == null){
                continue;
            }
            if(n.index == index){
                return true;
            }
        }
        return false;
    }

    public Node get(int index){
        for(Node n : child){
            if(n == null){
                continue;
            }
            if(n.index == index){
                return n;
            }
        }
        return null;
    }

}
