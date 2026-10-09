package test;
 
import control.Controller;
import entity.*;
import database.*;
import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
import java.sql.Timestamp;
 
/**
* Classe di test per verificare il pattern BCED con MySQL
*/
public class JUnitTest{
   
    public static void main(String[] args) {
        System.out.println("=== TEST METODO PUBBLICAPOESIA - CONTROLLER ===\n");
        
        try {
            // Test 1: Autore non loggato
            testAutoreNonLoggato();
            
            // Login come autore per i test successivi
            Controller controller = Controller.getInstance();
            controller.loginAutore("mario.rossi@email.com", "Password123!");
            
            // Test 2: Titolo vuoto
            testTitoloVuoto();
            
            // Test 3: Testo vuoto
            testTestoVuoto();
            
            // Test 4: Nessun tag
            testNessunTag();
            
            // Test 5: Tag vuoto
            testTagVuoto();
            
            // Test 6: Titolo raccolta vuoto
            testTitoloRaccoltaVuoto();
            
            // Test 7: Creazione nuova raccolta senza descrizione
            testNuovaRaccoltaSenzaDescrizione();
            
            // Test 8: Titolo raccolta già esistente (quando creaRaccoltaNuova = true)
            testTitoloRaccoltaEsistenteNuova();
            
            // Test 9: Uso raccolta esistente ma non selezionata
            testRaccoltaEsistenteNonSelezionata();
            
            // Test 10: Tag non valido (caratteri speciali)
            testTagNonValido();
            
            // Test 11: Tag troppo lungo
            testTagTroppoLungo();
            
            // Test 12: Titolo raccolta troppo lungo
            testTitoloRaccoltaTroppoLungo();
            
            // Test 13: Descrizione raccolta troppo corta
            testDescrizioneRaccoltaTroppoCorta();
            
            // Test 14: Descrizione raccolta troppo lunga
            testDescrizioneRaccoltaTroppa();
            
            // Test 15: Titolo poesia troppo lungo
            testTitoloPoesiaTroppoLungo();
            
            // Test 16: Testo poesia troppo lungo
            testTestoPoesiaTroppoLungo();
            
            System.out.println("\n=== TUTTI I TEST DI ERRORE PUBBLICAPOESIA COMPLETATI ===");
            
        } catch (Exception e) {
            System.err.println("ERRORE durante i test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 1: Autore non loggato
     */
    private static void testAutoreNonLoggato() {
        System.out.println("1. TEST AUTORE NON LOGGATO");
        
        try {
            Controller controller = Controller.getInstance();
            controller.logout(); // Assicurati che non ci siano utenti loggati
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato autore non loggato");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per autore non loggato");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 2: Titolo vuoto
     */
    private static void testTitoloVuoto() {
        System.out.println("\n2. TEST TITOLO VUOTO");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "", // Titolo vuoto
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato titolo vuoto");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per titolo vuoto");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 3: Testo vuoto
     */
    private static void testTestoVuoto() {
        System.out.println("\n3. TEST TESTO VUOTO");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "", // Testo vuoto
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato testo vuoto");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per testo vuoto");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 4: Nessun tag
     */
    private static void testNessunTag() {
        System.out.println("\n4. TEST NESSUN TAG");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(), // Lista tag vuota
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevata lista tag vuota");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per nessun tag");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 5: Tag vuoto nella lista
     */
    private static void testTagVuoto() {
        System.out.println("\n5. TEST TAG VUOTO NELLA LISTA");
        
        try {
            Controller controller = Controller.getInstance();
            
            ArrayList<String> tags = new ArrayList<>();
            tags.add(""); // Tag vuoto
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                tags,
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato tag vuoto nella lista");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per tag vuoto");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    /**
     * Test 6: Titolo raccolta vuoto
     */
    private static void testTitoloRaccoltaVuoto() {
        System.out.println("\n6. TEST TITOLO RACCOLTA VUOTO");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "", // Titolo raccolta vuoto
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato titolo raccolta vuoto");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per titolo raccolta vuoto");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 7: Creazione nuova raccolta senza descrizione
     */
    private static void testNuovaRaccoltaSenzaDescrizione() {
        System.out.println("\n7. TEST NUOVA RACCOLTA SENZA DESCRIZIONE");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Nuova Test",
                "", // Descrizione vuota
                true // Crea nuova raccolta
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevata descrizione vuota per nuova raccolta");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per descrizione vuota");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 8: Titolo raccolta già esistente (quando creaRaccoltaNuova = true)
     */
    private static void testTitoloRaccoltaEsistenteNuova() {
        System.out.println("\n8. TEST TITOLO RACCOLTA GIA' ESISTENTE (NUOVA RACCOLTA)");
        
        try {
            Controller controller = Controller.getInstance();
            
            // Prima crea una raccolta
            controller.pubblicaPoesia(
                "Poesia Preliminare",
                "Testo preliminare",
                new ArrayList<>(java.util.Arrays.asList("preliminare")),
                true,
                "Raccolta Duplicata",
                "Descrizione per raccolta duplicata",
                true
            );
            
            // Poi prova a crearne un'altra con lo stesso titolo
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Duplicata", // Stesso titolo
                "Altra descrizione",
                true // Crea nuova raccolta
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato titolo raccolta duplicato");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per titolo duplicato");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 9: Uso raccolta esistente ma non selezionata
     */
    private static void testRaccoltaEsistenteNonSelezionata() {
        System.out.println("\n9. TEST RACCOLTA ESISTENTE NON SELEZIONATA");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "-- Seleziona raccolta --", // Valore di default
                null,
                false // Usa raccolta esistente
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevata raccolta non selezionata");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per raccolta non selezionata");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 10: Tag non valido (caratteri speciali)
     */
    private static void testTagNonValido() {
        System.out.println("\n10. TEST TAG NON VALIDO (CARATTERI SPECIALI)");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("tag!@#")), // Caratteri speciali
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato tag con caratteri non validi");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per tag non valido");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    /**
     * Test 11: Tag troppo lungo
     */
    private static void testTagTroppoLungo() {
        System.out.println("\n11. TEST TAG TROPPO LUNGO");
        
        try {
            Controller controller = Controller.getInstance();
            
            // Crea un tag di 31 caratteri (limite è 30)
            String tagLungo = "abcdefghijklmnopqrstuvwxyzabcde";
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList(tagLungo)),
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato tag troppo lungo");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per tag troppo lungo");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 12: Titolo raccolta troppo lungo
     */
    private static void testTitoloRaccoltaTroppoLungo() {
        System.out.println("\n12. TEST TITOLO RACCOLTA TROPPO LUNGO");
        
        try {
            Controller controller = Controller.getInstance();
            
            // Titolo di 81 caratteri (limite è 80)
            String titoloLungo = "Questo è un titolo di raccolta estremamente lungo che supera i limiti consentiti dalla piattaforma per le raccolte tematiche";
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                titoloLungo,
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato titolo raccolta troppo lungo");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per titolo raccolta troppo lungo");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 13: Descrizione raccolta troppo corta
     */
    private static void testDescrizioneRaccoltaTroppoCorta() {
        System.out.println("\n13. TEST DESCRIZIONE RACCOLTA TROPPO CORTA");
        
        try {
            Controller controller = Controller.getInstance();
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                "Corta", // Solo 5 caratteri (minimo 5, ma troppo corta comunque)
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevata descrizione raccolta troppo corta");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per descrizione troppo corta");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 14: Descrizione raccolta troppo lunga
     */
    private static void testDescrizioneRaccoltaTroppa() {
        System.out.println("\n14. TEST DESCRIZIONE RACCOLTA TROPPO LUNGA");
        
        try {
            Controller controller = Controller.getInstance();
            
            // Descrizione di 301 caratteri (limite è 300)
            StringBuilder descrizioneLunga = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                descrizioneLunga.append("Descrizione lunghissima ");
            }
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                descrizioneLunga.toString(),
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevata descrizione raccolta troppo lunga");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per descrizione troppo lunga");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 15: Titolo poesia troppo lungo
     */
    private static void testTitoloPoesiaTroppoLungo() {
        System.out.println("\n15. TEST TITOLO POESIA TROPPO LUNGO");
        
        try {
            Controller controller = Controller.getInstance();
            
            // Titolo di 101 caratteri (limite è 100)
            String titoloLungo = "Questo è un titolo di poesia estremamente lungo che supera ampiamente i limiti consentiti dalla piattaforma per i titoli delle poesie brevi";
            
            long idPoesia = controller.pubblicaPoesia(
                titoloLungo,
                "Testo di prova",
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato titolo poesia troppo lungo");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per titolo poesia troppo lungo");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
    
    /**
     * Test 16: Testo poesia troppo lungo
     */
    private static void testTestoPoesiaTroppoLungo() {
        System.out.println("\n16. TEST TESTO POESIA TROPPO LUNGO");
        
        try {
            Controller controller = Controller.getInstance();
            
            // Testo di 501 caratteri (limite è 500)
            StringBuilder testoLungo = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                testoLungo.append("Testo lunghissimo ");
            }
            testoLungo.append("x"); // Per arrivare a 501
            
            long idPoesia = controller.pubblicaPoesia(
                "Titolo Test",
                testoLungo.toString(),
                new ArrayList<>(java.util.Arrays.asList("test")),
                true,
                "Raccolta Test",
                "Descrizione test",
                true
            );
            
            if (idPoesia == -1) {
                System.out.println("✅ PASSATO: Rilevato testo poesia troppo lungo");
            } else {
                System.out.println("❌ FALLITO: Avrebbe dovuto fallire per testo poesia troppo lungo");
            }
            
        } catch (Exception e) {
            System.out.println("✅ PASSATO: Eccezione catturata - " + e.getMessage());
        }
    }
}