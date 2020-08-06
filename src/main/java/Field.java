public class Field {
    public int time;
    public int x;
    public int y;
    public Field up;
    public Field down;
    public Field left;
    public Field right;

    public Field(int t, int x, int y){
        this.time = t;
        this.x = x;
        this.y = y;
    }

    public void setConnections(Field u, Field d, Field l, Field r){
        this.up = u;
        this.down = d;
        this.left = l;
        this.right = r;
    }
}
