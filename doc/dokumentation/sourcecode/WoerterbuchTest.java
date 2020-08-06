import static org.junit.jupiter.api.Assertions.*;

class WoerterbuchTest {
    Woerterbuch wBuch;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        wBuch = new Woerterbuch("bib.text");
    }

    @org.junit.jupiter.api.Test
    void ladeBib() {
        // file dont exist yet
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\notRealBib.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\truebib.text");
        assertTrue(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falsebib1.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falsebib2.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falsebib3.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falsebib4.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falsebib5.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falsebib6.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\emptybib.text");
        assertFalse(wBuch.ladeBib());
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\falseemptybib.text");
        assertFalse(wBuch.ladeBib());
    }

    @org.junit.jupiter.api.Test
    void speichereBib() {
        // overrite original file
        wBuch.setPath("bib.text");
        wBuch.ladeBib();
        assertTrue(wBuch.speichereBib());
        wBuch.setPath("Beispiele\\Testfaelle\\noneTextFile.o");
        assertFalse(wBuch.speichereBib());
        wBuch.setPath("Beispiele\\Testfaelle\\randomName.text");
        assertTrue(wBuch.speichereBib());
    }

    @org.junit.jupiter.api.Test
    /**
     * Teste alle moeglichen Ergebnisse
     */
    void searchFor() {
        wBuch.setPath("Beispiele\\Testfaelle\\testbib1.text");
        wBuch.ladeBib();
        assertEquals("ZNRF", wBuch.searchFor("9673"));
        assertEquals("ZO", wBuch.searchFor("96"));
        assertEquals("ZARA", wBuch.searchFor("9272"));
        assertEquals("W", wBuch.searchFor("9")); // einfache Suche
        assertEquals("-1",wBuch.searchFor("967"));
        wBuch = new Woerterbuch("Beispiele\\Testfaelle\\testbib2.text");
        wBuch.ladeBib();
        assertEquals("WORF", wBuch.searchFor("9673"));
    }

    @org.junit.jupiter.api.Test
    void add() {
        
    }

    @org.junit.jupiter.api.Test
    void makeWord() {
    }
}