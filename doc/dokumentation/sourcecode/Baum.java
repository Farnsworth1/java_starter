import java.util.ArrayList;

/**
 * Beinhaltet alle Einträge des Wörterbuchs im Form von Baumstruktur
 */
public class Baum {
    /**
     * Eine Liste mit Allen Wurzelknoten
     */
    private ArrayList<Node> root = new ArrayList<>();

    public Baum(){
        for(int i = 2; i <= 9; i++){
            Node n = new Node(i);
            this.root.add(n);
        }
    }

    public String getCode(String key){
        int num = key.charAt(0) - 48;
        String output = "";
        for(Node n : this.root){
            if(n.index == num){
                ArrayList<Node> out = search1(key.substring(1), n, true);
                if(out.isEmpty()){
                    output = "-1";
                    break;
                }
                // compare output
                int max = 0;
                Node maxNode = null;
                for(Node node : out){
                    if(node.n >= max){
                        max = node.n;
                        maxNode = node;
                    }
                }
                while(maxNode != null){
                    output += maxNode.index;
                    maxNode = maxNode.parent;
                }
                output = new StringBuffer(output).reverse().toString();
            }
        }
        return output;
    }

    /**
     * Suche mit einem einfachen Ziffernfolge nach einem Wort im Baum.
     * Falls zwei Woerter gleiche Ziffernfolge haben, wird das haeufigste Wort zurueckgegben.
     * Falls kein Treffer existiert wird "-1" zurueckgegben
     * @param key einfache Ziffernfolge
     * @return Ergebniss
     */
    public String search(String key) {
        int num = key.charAt(0) - 48;
        String output = "";
        for(Node n : this.root){
            if(n.index == num){
                ArrayList<Node> out = search1(key.substring(1), n, true);
                if(out.isEmpty()){
                    output = "-1";
                    break;
                }
                // compare output
                int max = 0;
                Node maxNode = null;
                for(Node node : out){
                    if(node.n >= max){
                        max = node.n;
                        maxNode = node;
                    }
                }
                output =  constructWord(maxNode);
            }
        }
        return output;
    }
    private ArrayList<Node> search1(String key, Node node, boolean explicit){
        ArrayList<Node> output = new ArrayList<>();
        // if explicit search all child nodes
        if(explicit){
            if(key.length() == 0){
                for(Node n : node.child){
                    if(n.n != 0){
                        output.add(n);
                    }
                }
            }
            else{
                for(Node n : node.child){
                    ArrayList<Node> temp = search1(key, n, false);
                    output.addAll(temp);
                }
            }
        }
        // or find the node with the index from key
        else{
            int num = key.charAt(0) - 48;
            for(Node n : node.child){
                if(num == n.index){
                    // search in explicit mode
                    ArrayList<Node> temp = search1(key.substring(1), n, true);
                    output.addAll(temp);
                }
            }
        }
        return output;
    }

    /**
     * Konstruiert eine Ziffernfolge Rueckwaerts aus einem Knoten und verwandelt dies zu einem Wort
     * @param n Knoten
     * @return Wort
     */
    private String constructWord(Node n){
        String output = "";
        while(n != null){
            output += n.index;
            n = n.parent;
        }
        output = new StringBuffer(output).reverse().toString();
        return Woerterbuch.makeWord(output);
    }

    /**
     * Fuegt ein Wort zum Baum hinzu
     * @param key Das Wort als explizite Ziffernfolge
     * @param repeat die Haeufigkeit des Wortes
     */
    public void add(String key, int repeat) {
        int num = key.charAt(0) - 48;
        for(Node n : this.root){
            if(n.index == num){
                add1(key.substring(1), repeat, n);
            }
        }
    }

    private void add1(String key, int repeat, Node node) {
        if(key.length()==0){
            return;
        }
        int index = key.charAt(0) - 48;
        Node child = null;
        // if node dont exist make new one
        if(!node.has(index)){
            child = new Node(index, node);
            node.child.add(child);
        }
        // otherwise get that node
        else{
            child = node.get(index);
        }
        // if last character in the word, add or increment frequency
        if(key.length() == 1){
            if(repeat == 0){
                child.n += 1;
            }
            else{
                child.n = repeat;
            }
        }
        add1(key.substring(1), repeat, child);
    }

    public static void main(String[] args){
        Baum b = new Baum();
        b.add("9163", 1);
        b.add("9463", 2);
        System.out.println(b.search("965"));
        int temp = 0;
    }
}
