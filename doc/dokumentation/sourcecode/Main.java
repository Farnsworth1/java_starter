/**
 * Die Hauptklasse fuer das Hauptprogramm von T9
 */
public class Main {

    /**
     * Die main Funktion fuer das gesamte Programm
     * @param args ist willkuerlich
     */
    public static void main(String[] args) {
        boolean exit = false;
        // ein Woerterbuch mit Pfadangabe fuer die Textdatei mit Datensaetzen
        Woerterbuch wBuch = new Woerterbuch("bib.text");
        Konsole konsole = new Konsole(wBuch);
        konsole.starten();
        while (!exit){
            exit = konsole.nutzerinteraktion();
        }
        konsole.beenden();
    }
}
