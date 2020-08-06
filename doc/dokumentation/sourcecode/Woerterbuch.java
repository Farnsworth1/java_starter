import java.io.*;
import java.util.*;

/**
 * Diese Klasse repräsentiert das Wörterbuch des Programms T9
 */
public class Woerterbuch {
    private HashMap<String, String> bib = new HashMap<>();
    public Baum tree = new Baum();
    private int size = 1000;
    private int maxLength = 15; // maximale Wortlaenge
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public Woerterbuch(String path){
        this.path = path;
    }

    /**
     * Laedt das Woerterbuch aus der Textdatei.
     * Keine Doppelte Eintraege sind erlaubt
     * @return Erfolgreich : true, Gescheitert : false
     */
    public boolean ladeBib(){
        File temp = new File(this.path);
        if (temp.length() == 0){ // file is empty
            return false;
        }
        Scanner input = null;
        try {
            FileReader Info=new FileReader(this.path);
            input=new Scanner(Info).useDelimiter("\n");
        }
        catch(FileNotFoundException noFile) {
            return false;
        }

        try {
            if(!input.hasNext()){
                makeTree();
                return false;
            }
            while(input.hasNext()) {
                String line = input.next();
                String[] elements = line.split("\\s");
                if(elements.length != 3){
                    throw new NoSuchElementException("Flasche Eingabe");
                }
                String key = elements[0];
                if(key.length() > this.maxLength*2){
                    throw new NoSuchElementException("Flasche Eingabe");
                }

                String value = elements[1];
                try{
                    String word = makeWord(key);
                    if(value.length() > maxLength || !value.equals(word)){
                        throw new Exception("falsche Eingabe");
                    }
                }
                catch(Exception e){
                    throw new NoSuchElementException("Flasche Eingabe");
                }
                int frequency = Integer.parseInt(elements[2]);

                // falls key schon vorhanden
                if (this.bib.containsKey(key)) {
                    throw new NoSuchElementException("Flasche Eingabe"); // keine doppelte Eintraege sind erlaubt
                } else {
                    this.bib.put(key, value + frequency);
                }
            }
            makeTree();
        }
        catch(NoSuchElementException e) {
            // Wrong type of file
            makeTree();
            return false;
        }
        input.close();
        makeTree();
        return true;
    }

    /**
     * speichert das Woerterbuch in der Textdatei mit path als Pfadangabe
     */
    public boolean speichereBib() {

        try {
            // check if path is correct
            String fileName = this.path;
            String ext = "";
            if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
                ext = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            else{
                ext =  "";
            }
            if(!ext.equals("text")){
                throw new IOException("wrong file Path");
            }
            // read file now
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.path));
            for (Map.Entry me : this.bib.entrySet()) {
                String key = me.getKey().toString();
                String value =  me.getValue().toString();
                String frequency = "" +  value.charAt(value.length()-1);
                value = value.substring(0, value.length()-1);
                String str = key + " " + value + " " + frequency + "\n";
                writer.write(str);
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Woerterbuch wurde nicht gespeichert");
            return false;
        }

    }

    /**
     * Sucht im Baum nach einem treffer
     * @param key einface Ziffernfolge
     * @return -1 falls kein Treffer gefunden ist
     */
    public String searchFor(String key) {
        return this.tree.search(key);
    }

    /**
     * Fuegt ein Eintrag in das Woerterbuch hinzu
     * Falls Eintrag schon existiert wird die Frequenz des Wortes um 1 erhoeht
     * @param key explizite Ziffernfolge
     * @param wort Das korrespondierende Wort
     */
    public void add(String key, String wort) {
        // falls das Wort schon existiert update
        if(this.bib.containsKey(key)){
            String value = this.bib.get(key);
            int frequency = Integer.parseInt(""+value.charAt(value.length()-1));
            frequency += 1; // Frequenz erhoehen
            value = value.substring(0, value.length()-1) + frequency;
            this.bib.put(key, value);
        }
        else{
            this.bib.put(key, wort + "1");
        }
        addToTree(key, 0);
    }

    /**
     * erstellt ein Baum fuer eine Vereinfachte Suche.
     * Alle Eintraege im Woerterbuch werden gelesen und in einem Baum gespeichert
     */
    private void makeTree(){
        for (Map.Entry me : this.bib.entrySet()) {
            String key = me.getKey().toString();
            String value = (String) me.getValue();
            int frequency = value.charAt(value.length()-1) - 48;
            value = value.substring(0, value.length()-1);
            addToTree(key,frequency);
        }
    }

    /**
     * Fuegt ein Wort im Baum hinzu
     * @param key explizite Ziffernfolge
     * @param frequency Haeufigkeit eines Wortes. 0 falls keine Eingabe.
     */
    private void addToTree(String key, int frequency){
        this.tree.add(key, frequency);
    }

    /**
     * verwandelt eine explizite Ziffernfolge in einem Wort im Alphabet zu Grossbuchstaben
     * @param key explizite Ziffernfolge
     * @return ergebniss ist großgeschrieben
     */
    public static String makeWord(String key) {
        String out = "";
        for (int i = 0; i < key.length(); i+=2){
            if(key.charAt(i) == 50){
                if(key.charAt(i+1) == 49){
                    out +="A";
                }
                else if(key.charAt(i+1) == 50){
                    out += "B";
                }
                else{
                    out += "C";
                }
            }
            else if(key.charAt(i) == 51){
                if(key.charAt(i+1) == 49){
                    out+="D";
                }
                else if(key.charAt(i+1) == 50){
                    out += "E";
                }
                else{
                    out += "F";
                }
            }
            else if(key.charAt(i) == 52){
                if(key.charAt(i+1) == 49){
                    out+= "G";
                }
                else if(key.charAt(i+1) == 50){
                    out += "H";
                }
                else{
                    out += "I";
                }
            }
            else if(key.charAt(i) == 53){
                if(key.charAt(i+1) == 49){
                    out+="J";
                }
                else if(key.charAt(i+1) == 50){
                    out += "K";
                }
                else{
                    out += "L";
                }
            }
            else if(key.charAt(i) == 54){
                if(key.charAt(i+1) == 49){
                    out+="M";
                }
                else if(key.charAt(i+1) == 50){
                    out += "N";
                }
                else{
                    out += "O";
                }
            }
            else if(key.charAt(i) == 55){
                if(key.charAt(i+1) == 49){
                    out+="P";
                }
                else if(key.charAt(i+1) == 50){
                    out += "Q";
                }
                else if(key.charAt(i+1) == 51){
                    out += "R";
                }
                else{
                    out += "S";
                }
            }
            else if(key.charAt(i) == 56){
                if(key.charAt(i+1) == 49){
                    out+="T";
                }
                else if(key.charAt(i+1) == 50){
                    out += "U";
                }
                else{
                    out += "V";
                }
            }
            else{
                if(key.charAt(i+1) == 49){
                    out+="W";
                }
                else if(key.charAt(i+1) == 50){
                    out += "X";
                }
                else if(key.charAt(i+1) == 51){
                    out += "Y";
                }
                else{
                    out += "Z";
                }
            }
        }
        return out;
    }

    public static void main(String[] args){
        Woerterbuch w = new Woerterbuch("Beispiele\\Testfaelle\\truebib.text");
        System.out.println(w.ladeBib());
        System.out.println(w.searchFor("337"));
    }
}
