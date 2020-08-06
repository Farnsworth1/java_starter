import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Diese Klasse ist die Konsole fuer die Nutzerinteraktion.
 * Verwaltet die Eingaben und Ausgaben des Programms.
 * Filtert die Eingaben von Nutzer und zeigt Fehler an.
 * Saetze und Woerter werden differenziert.
 */
public class Konsole {
    private Woerterbuch bib; // fuer die Funktionalitaet von T9
    private Scanner sc = new Scanner(System.in); // fuer die Nutzereingabe
    private int maxLength = 15; // maximale Wortlaenge
    private int maxSentenceLength = 200; // maximale Satzlaenge
    private String satz = "";

    public Konsole(Woerterbuch b) {
        this.bib = b;
    }

    /**
     * Startet die Konsole
     */
    public void starten() {
        System.out.println("Programm T9 gestartet...");
        boolean result = this.bib.ladeBib();
        if (result) {
            System.out.println("Woerterbuch geladen");
        } else {
            System.out.println("kein Woerterbuch geladen");
        }
    }

    /**
     * Beendet die Konsole
     */
    public void beenden() {
        this.bib.speichereBib();
        System.out.println("Programmende");
    }

    /**
     * Die Hauptfunktion fuer die Nutzereingabe der Konsole
     *
     * @return
     */
    public boolean nutzerinteraktion() {
        while (true) {
            System.out.print("Ziffernfolge eingaben [0,9] >>  ");
            String key = sc.nextLine();
            if(key.length()+ this.satz.length() >= this.maxSentenceLength){
                System.out.println("Satzgrenze ist "+ this.maxSentenceLength);
                System.out.println("Neuer Satz");
                this.satz = "";
                return false;
            }
            if(key.equals("0") && this.satz.equals("")){
                // Programmende
                return true;
            }
            if (isValid1(key)) {
                // suche nach Wort ohne 1 oder 0 am Ende
                String suchergebnis = this.bib.searchFor(key.substring(0, key.length()-1));
                // das Wort liegt nicht im Woerterbuch
                if (suchergebnis.equals("-1")) {
                    System.out.println("Kein Passendes Wort gespeichert.");

                    boolean exit = explizitmode();
                    // Satzende
                    if(key.charAt(key.length()-1) == '0' || exit){
                        System.out.println("Der Satz lautet");
                        System.out.println(this.satz);
                        this.satz = "";
                        return false;
                    }
                    else{
                        return false;
                    }

                }
                // das Wort liegt im Woerterbuch
                else {
                    if(suchergebnis.length() + this.satz.length() >= this.maxSentenceLength){
                        System.out.println("Satzgrenze ist "+ this.maxSentenceLength);
                        System.out.println("Neuer Satz");
                        this.satz = "";
                        return false;
                    }
                    System.out.println(suchergebnis);
                    while (true) {
                        System.out.println("Wort ok?");
                        String input = sc.nextLine();
                        if (isValid2(input)) {
                            // falls ja im Satz speichern
                            if (input.equals("*")) {
                                this.satz += " " + suchergebnis;
                                // Wort updaten
                                String temp = this.bib.tree.getCode(key.substring(0, key.length()-1));
                                this.bib.add(temp, suchergebnis);
                                // Satzende
                                if(key.charAt(key.length()-1) == '0'){
                                    System.out.println("Der Satz lautet");
                                    System.out.println(this.satz);
                                    this.satz = "";
                                    return false;
                                }
                                else{
                                    return false;
                                }
                            }
                            // falls nein Explizitmodus
                            else {
                                boolean exit = explizitmode();
                                // Satzende
                                if(key.charAt(key.length()-1) == '0' || exit){
                                    System.out.println("Der Satz lautet");
                                    System.out.println(this.satz);
                                    this.satz = "";
                                    return false;
                                }
                                else{
                                    return false;
                                }
                            }
                        } else {
                            System.out.println("Falsche Eingabe");
                            System.out.println("keine ja(*) oder Nein(#) Anwort");
                        }
                    }
                }
            } else {
                System.out.println("Falsche Eingabe");
                System.out.println("keine Ziffernfolge oder Wort zu Lang");
            }
        }
    }

    /**
     * Explizite Eingabe von Ziffernfolgen
     */
    private boolean explizitmode() {
        while (true) {
            System.out.println("Eingabe im Explizitmodus:");
            System.out.print("Ziffernfolge eingaben [0,9] >>  ");
            String key = sc.nextLine();
            if(key.length()+ this.satz.length() >= this.maxSentenceLength){
                System.out.println("Satzgrenze ist "+ this.maxSentenceLength);
                System.out.println("Neuer Satz");
                this.satz = "";
            }
            if (isValidExplisite(key)) {
                String wort = this.bib.makeWord(key.substring(0, key.length()-1));
                System.out.println(wort);
                while (true) {
                    System.out.println("Wort ok?");
                    String input = sc.nextLine();
                    if (isValid2(input)) {
                        // falls ja im Satz und Woerterbuch speichern ohne 0 oder 1 am Ende
                        if (input.equals("*")) {
                            this.bib.add(key.substring(0, key.length()-1), wort);
                            this.satz += " " + wort;
                            System.out.println("Wort gespeichert");
                            if(key.charAt(key.length()-1) == '0'){
                                return true;
                            }
                            return false;
                        } else {
                            break;
                        }
                    } else {
                        System.out.println("Falsche Eingabe");
                        System.out.println("keine ja(*) oder Nein(#) Anwort");
                    }
                }
            } else {
                System.out.println("Falsche Eingabe");
                System.out.println("keine Ziffernfolge oder Wort zu Lang");
            }
        }

    }

    /**
     * Ueberprueft ob die Nutzereingabe eine Ziffernfolge ist.
     * Die moeglichen Eingaben sind E = {0,1,2,...,9}
     * Am Ende soll 0 oder 1 geben.
     * Anfangsziffer darf nicht 0 oder 1 sein
     * @param key liegt in der Menge E
     * @return Valide = true, Invalide = false
     */
    private boolean isValid1(String key) {
        boolean out = true;
        if(key.charAt(0) == '0' || key.charAt(0) == '1'){
            out = false;
        }
        if(!(key.charAt(key.length()-1) == '0' || key.charAt(key.length()-1) == '1')){
            out = false;
        }
        if (key.length() > this.maxLength || key.length() == 0) {
            out = false;
        }
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!(c >= 48 && c <= 57)) {
                out = false; // keine Ziffernfolge
            }
        }
        return out;
    }

    /**
     * Wie isValid1 aber in Explizitmodus. D.h. maximale Wortlaenge ist doppelt so groß, da
     * 2 Ziffern fuer eine Buchstabe benoetigt werden
     * @param key liegt in der Menge E
     * @return Valide = true, Invalide = false
     */
    private boolean isValidExplisite(String key) {
        boolean out = true;
        if((key.length()-1)%2 != 0){
            out = false;
        }
        if(key.charAt(0) == '0' || key.charAt(0) == '1'){
            out = false;
        }
        if(!(key.charAt(key.length()-1) == '0' || key.charAt(key.length()-1) == '1')){
            out = false;
        }
        if (key.length() > this.maxLength*2 || key.length() <= 2) {
            out = false;
        }
        for (int i = 0; i < key.length()-1; i++) {
            char c = key.charAt(i);
            if (!(c >= 48 && c <= 57)) {
                out = false; // keine Ziffernfolge
            }
            if(i % 2 == 0){
                char temp = key.charAt(i+1);
                if(c == 55 || c == 57){ // zahl 7 oder 9
                    if(temp < 49 || temp > 52){
                        out = false;
                    }
                }
                else{
                    if(temp < 49 || temp > 51){
                        out = false;
                    }
                }
            }
        }
        return out;
    }

    /**
     * Ueberprueft ob die Nutzereingabe eine der beiden Zeichen in E = {*,#}
     * ist
     * @param key liegt in der Menge E
     * @return Valide = true, Invalide = false
     */
    private boolean isValid2(String key) {
        boolean out = true;
        if (key.length() != 1) {
            out = false;
        }
        if (!(key.equals("*") || key.equals("#"))) {
            out = false;
        }
        return out;
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
