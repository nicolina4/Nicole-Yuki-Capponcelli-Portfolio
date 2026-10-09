package control;

import DTO.StatisticheDTO;
import DTO.AutoreAttivitaDTO;
import DTO.ReportIntervalloDTO;
import entity.*;

import database.*;
import boundary.*;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;
import java.util.*;

import java.sql.Timestamp;

/**
 * Controller singleton per la gestione della logica di business
 * della piattaforma di poesie online - Pattern BCED
 */
public class Controller {
    private static Controller instance = null;
    
    // Utente attualmente loggato
    private UtenteEntity utenteCorrente = null;
    private AutoreEntity autoreCorrente = null;
    private AmministratoreEntity amministratoreCorrente = null;
    
    /**
     * Enum per definire i contesti di validazione delle stringhe
     */
    public enum Contesto {
        NOME,
        COGNOME,
        EMAIL,
        PASSWORD,
        BIO,
        TITOLOP,      // Titolo Poesia
        TESTOP,
        TITOLOR,      // Titolo Raccolta
        DESCRIZIONER, // Descrizione Raccolta
        TAG,
        COMMENTO
    }
    
    /**
     * Costruttore privato per pattern Singleton
     */
    private Controller() {
        super();
    }
    
    /**
     * Metodo per ottenere l'istanza unica del Controller
     * @return istanza del Controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }
    
    /**
     * Valida una stringa in base al contesto specificato
     * @param input stringa da validare
     * @param contesto contesto di validazione
     * @return true se la validazione ha successo
     * @throws IllegalArgumentException se la validazione fallisce
     */
    public boolean validazioneStringa(String input, Contesto contesto) 
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException, UnknownHostException {
        
        if (input == null) {
            throw new IllegalArgumentException("Il campo non può essere nullo!");
        }
        
        boolean check = false;
        String trimmedInput = input.trim();
        
        switch(contesto) {
        
        case NOME:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il nome non può essere un campo vuoto!");
            }
            if (input.matches("^[a-zA-Zàèéìòùäëïöü\\s']+$")) {
                if (input.length() < 1) {
                    throw new IllegalArgumentException("Non hai inserito un nome! Inserisci almeno un carattere.");
                } else if (input.length() > 40) {
                    throw new IllegalArgumentException("Il nome è troppo lungo! Inserisci un massimo di 40 caratteri.");
                } else {
                    check = true;
                }
            } else {
                throw new IllegalArgumentException("Il valore inserito non è un nome valido. "
                        + "Riprova inserendo solo caratteri alfabetici.");
            }
            break;

        case COGNOME:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il cognome non può essere un campo vuoto!");
            }
            if (input.matches("^[a-zA-Zàèéìòùäëïöü\\s']+$")) {
                if (input.length() < 1) {
                    throw new IllegalArgumentException("Non hai inserito un cognome! Inserisci almeno un carattere.");
                } else if (input.length() > 40) {
                    throw new IllegalArgumentException("Il cognome è troppo lungo! Inserisci un massimo di 40 caratteri.");
                } else {
                    check = true;
                }
            } else {
                throw new IllegalArgumentException("Il valore inserito non è un cognome valido. "
                        + "Riprova inserendo solo caratteri alfabetici.");
            }
            break;
            
        case EMAIL:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("L'email non può essere un campo vuoto!");
            }
            if (input.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                String[] parts = input.split("@");
                try {
                    String domain = parts[1];
                    try {
                        InetAddress.getByName(domain);
                        check = true;
                    } catch (UnknownHostException e) {
                        throw new UnknownHostException("L'indirizzo e-mail non è valido: il dominio inserito è inesistente.");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Il testo inserito non corrisponde a una mail: "
                            + "è necessario inserire una @ e un dominio.");
                }
            } else {
                throw new IllegalArgumentException("Il testo inserito non corrisponde a una mail valida.");
            }
            break;

        case PASSWORD:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("La password non può essere un campo vuoto!");
            }
            if (input.length() < 8) {
                throw new IllegalArgumentException("La password è troppo corta! Inserisci almeno 8 caratteri.");
            } else if (input.length() > 30) {
                throw new IllegalArgumentException("La password è troppo lunga! Inserisci un massimo di 30 caratteri.");
            } else if (!input.matches(".*[A-Z].*")) {
                throw new IllegalArgumentException("La password deve contenere almeno una lettera maiuscola.");
            } else if (!input.matches(".*[a-z].*")) {
                throw new IllegalArgumentException("La password deve contenere almeno una lettera minuscola.");
            } else if (!input.matches(".*[0-9].*")) {
                throw new IllegalArgumentException("La password deve contenere almeno un numero.");
            } else {
                check = true;
            }
            break;

        case BIO:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("La biografia non può essere vuota!");
            }
            if (input.length() < 10) {
                throw new IllegalArgumentException("La biografia è troppo corta! Inserisci almeno 10 caratteri.");
            } else if (input.length() > 300) {
                throw new IllegalArgumentException("La biografia è troppo lunga! Inserisci un massimo di 300 caratteri.");
            } else {
                check = true;
            }
            break;

        case TITOLOP:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il titolo della poesia non può essere vuoto!");
            }
            if (!input.matches("^[a-zA-Zàèéìòùäëïöü0-9\\s'.,!?:;-]+$")) {
                throw new IllegalArgumentException("Il titolo della poesia contiene caratteri non validi!");
            }
            if (input.length() > 100) {
                throw new IllegalArgumentException("Il titolo della poesia è troppo lungo! Inserisci un massimo di 100 caratteri.");
            }
            check = true;
            break;
            
        case TESTOP:
            if(trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il testo della poesia non può essere vuoto!");
            }
            
            if (input.length() > 500) {
                throw new IllegalArgumentException("Il testo della poesia è troppo lungo! Inserisci un massimo di 500 caratteri.");
            }
            check = true;
            break;

        case TITOLOR:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il titolo della raccolta non può essere vuoto!");
            }
            if (!input.matches("^[a-zA-Zàèéìòùäëïöü0-9\\s'.,!?:;-]+$")) {
                throw new IllegalArgumentException("Il titolo della raccolta contiene caratteri non validi!");
            }
            if (input.length() > 80) {
                throw new IllegalArgumentException("Il titolo della raccolta è troppo lungo! Inserisci un massimo di 80 caratteri.");
            }
            check = true;
            break;

        case DESCRIZIONER:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("La descrizione della raccolta non può essere vuota!");
            }
            if (input.length() < 5) {
                throw new IllegalArgumentException("La descrizione è troppo corta! Inserisci almeno 5 caratteri.");
            } else if (input.length() > 300) {
                throw new IllegalArgumentException("La descrizione è troppo lunga! Inserisci un massimo di 300 caratteri.");
            } else {
                check = true;
            }
            break;

        case TAG:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il tag non può essere vuoto!");
            }
            if (!input.matches("^[a-zA-Zàèéìòùäëïöü0-9_-]+$")) {
                throw new IllegalArgumentException("Il tag contiene caratteri non validi! Usa solo lettere, numeri, trattini e underscore.");
            }
            if (input.length() > 30) {
                throw new IllegalArgumentException("Il tag è troppo lungo! Inserisci un massimo di 30 caratteri.");
            }
            check = true;
            break;
            
        case COMMENTO:
            if (trimmedInput.isEmpty()) {
                throw new IllegalArgumentException("Il commento non può essere vuoto!");
            }
            if (input.length() > 500) {
                throw new IllegalArgumentException("Il commento è troppo lungo! Massimo 500 caratteri.");
            }
            check = true;
            break;
        }

        return check;
    }

    /**
     * Capitalizza la prima lettera di ogni parola in una stringa
     * @param input stringa da capitalizzare
     * @return stringa capitalizzata
     */
    public String capitalizeString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        String[] words = input.split(" ");
        StringBuilder output = new StringBuilder();
        
        try {
            for (String word : words) {
                if (word.length() > 0) {
                    Character firstChar = Character.toUpperCase(word.charAt(0));
                    String restOfWord = word.substring(1).toLowerCase();
                    output.append(firstChar).append(restOfWord).append(" ");
                }
            }
        } catch(IllegalArgumentException e) {
            System.err.println("Non è stata inserita nessuna stringa di cui effettuare la conversione");
        }
    
        return output.toString().trim();
    }
    
    // ==================== GESTIONE UTENTI ====================
    
    /**
     * Registra un nuovo autore nel sistema
     * @param nome nome dell'autore
     * @param cognome cognome dell'autore
     * @param email email dell'autore
     * @param password password dell'autore
     * @param confermaPassword conferma della password
     * @param bio biografia dell'autore
     * @param immagineProfilo path immagine profilo
     * @return true se la registrazione ha successo
     */
    public boolean registraAutore(String nome, String cognome, String email, 
                                  String password, String confermaPassword, 
                                  String bio, String immagineProfilo) {
        try {
            // Validazione campi
            validazioneStringa(nome, Contesto.NOME);
            validazioneStringa(cognome, Contesto.COGNOME);
            validazioneStringa(email, Contesto.EMAIL);
            validazioneStringa(password, Contesto.PASSWORD);
            validazioneStringa(confermaPassword, Contesto.PASSWORD);
            if (bio != null && !bio.trim().isEmpty()) {
                validazioneStringa(bio, Contesto.BIO);
            }
            
            // Verifica password coincidano
            if (!password.equals(confermaPassword)) {
                throw new IllegalArgumentException("Le password non coincidono. Riprova.");
            }

            // Capitalizza nome e cognome
            nome = capitalizeString(nome);
            cognome = capitalizeString(cognome);
            email = email.toLowerCase();

            // Crea entity autore
            AutoreEntity autore = new AutoreEntity();
            autore.setNome(nome);
            autore.setCognome(cognome);
            autore.setBio(bio != null ? bio : "");
            autore.setImmagineProfilo(immagineProfilo != null ? immagineProfilo : "");
            
            // Registrazione
            if (autore.registraUtente(email, password)) {
                this.autoreCorrente = autore;
                return true;
            }
            
            return false;

        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | UnknownHostException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Login autore
     * @param email email dell'autore
     * @param password password dell'autore
     * @return true se il login ha successo
     */
    public boolean loginAutore(String email, String password) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("L'email non può essere vuota.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("La password non può essere vuota.");
            }

            email = email.toLowerCase();
            
            AutoreEntity autore = new AutoreEntity();
            if (autore.login(email, password)) {
                this.autoreCorrente = autore;
                this.utenteCorrente = autore;
                
                JOptionPane.showMessageDialog(null,
                    "Benvenuto " + autore.getNome() + " " + autore.getCognome() + "!"+ autore.getPoesiePubblicate(),
                    "LOGIN EFFETTUATO", JOptionPane.INFORMATION_MESSAGE);
                
                return true;
            } else {
                throw new IllegalArgumentException("Credenziali non valide. Riprova.");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE LOGIN", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Login amministratore
     * @param email email dell'amministratore
     * @param password password dell'amministratore
     * @return true se il login ha successo
     */
    public boolean loginAmministratore(String email, String password) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("L'email non può essere vuota.");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("La password non può essere vuota.");
            }

            email = email.toLowerCase();
            
            AmministratoreEntity admin = new AmministratoreEntity();
            if (admin.login(email, password)) {
                this.amministratoreCorrente = admin;
                this.utenteCorrente = admin;
                
                JOptionPane.showMessageDialog(null,
                    "Benvenuto Amministratore!",
                    "LOGIN EFFETTUATO", JOptionPane.INFORMATION_MESSAGE);
                
                return true;
            } else {
                throw new IllegalArgumentException("Credenziali non valide. Riprova.");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE LOGIN", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Logout
     */
    public void logout() {
        this.utenteCorrente = null;
        this.autoreCorrente = null;
        this.amministratoreCorrente = null;
        
        JOptionPane.showMessageDialog(null,
            "Logout effettuato con successo!",
            "LOGOUT", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Aggiorna profilo autore
     * @param nuovaEmail nuova email (opzionale)
     * @param nuovaPassword nuova password (opzionale)
     * @param nuovoNome nuovo nome (opzionale)
     * @param nuovoCognome nuovo cognome (opzionale)
     * @param nuovaBio nuova bio (opzionale)
     * @param nuovaImmagine nuova immagine (opzionale)
     * @return true se l'aggiornamento ha successo
     */
    public boolean aggiornaProfilo(String nuovaEmail, String nuovaPassword, 
                                  String nuovoNome, String nuovoCognome, 
                                  String nuovaBio, String nuovaImmagine) {
        if (autoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato come autore per aggiornare il profilo!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            // Validazione campi non nulli
            if (nuovaEmail != null && !nuovaEmail.trim().isEmpty()) {
                validazioneStringa(nuovaEmail, Contesto.EMAIL);
                nuovaEmail = nuovaEmail.toLowerCase();
            }
            
            if (nuovaPassword != null && !nuovaPassword.trim().isEmpty()) {
                validazioneStringa(nuovaPassword, Contesto.PASSWORD);
            }
            
            if (nuovoNome != null && !nuovoNome.trim().isEmpty()) {
                validazioneStringa(nuovoNome, Contesto.NOME);
                nuovoNome = capitalizeString(nuovoNome);
            }
            
            if (nuovoCognome != null && !nuovoCognome.trim().isEmpty()) {
                validazioneStringa(nuovoCognome, Contesto.COGNOME);
                nuovoCognome = capitalizeString(nuovoCognome);
            }
            
            if (nuovaBio != null && !nuovaBio.trim().isEmpty()) {
                validazioneStringa(nuovaBio, Contesto.BIO);
            }
            
            return autoreCorrente.aggiornaProfilo(nuovaEmail, nuovaPassword, 
                                                 nuovoNome, nuovoCognome, 
                                                 nuovaBio, nuovaImmagine);
            
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | UnknownHostException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    
    
    public boolean aggiornaProfiloAutore(String nuovoNome, String nuovoCognome, String nuovaEmail, String nuovaBiografia) {
        try {
            AutoreEntity autore = getAutoreCorrente();
            autore.setNome(nuovoNome);
            autore.setCognome(nuovoCognome);
            autore.setEmail(nuovaEmail);
            autore.setBio(nuovaBiografia);
            
            // Qui dovresti salvare le modifiche nel database
            // return autoreDAO.aggiornaAutore(autore);
            return true; // Placeholder
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    // ==================== GESTIONE POESIE ====================
    
   
    
   /**
 * Pubblica una nuova poesia
 * @param titolo titolo della poesia
 * @param testo testo della poesia
 * @param tags array di tag
 * @param pubblica true se la poesia è pubblica
 * @param titoloRaccolta titolo della raccolta (esistente o nuova)
 * @param descrizioneRaccolta descrizione della raccolta (richiesta solo per nuove raccolte)
 * @param creaRaccoltaNuova true se si vuole creare una nuova raccolta, false per usarne una esistente
 * @return ID della poesia pubblicata, -1 se errore
 */
public long pubblicaPoesia(String titolo, String testo, ArrayList<String> tags, 
                          boolean pubblica, String titoloRaccolta, 
                          String descrizioneRaccolta, boolean creaRaccoltaNuova) {
    if (autoreCorrente == null) {       // (1)
        JOptionPane.showMessageDialog(null,"Devi essere loggato come autore per pubblicare una poesia!","ERRORE", JOptionPane.ERROR_MESSAGE); // (2)
        return -1;  //(3)
    }
    
    try { //(4)
        // Validazione campi base
        validazioneStringa(titolo, Contesto.TITOLOP); //(5)
        validazioneStringa(testo, Contesto.TESTOP);  // (6)
        
        if (tags == null || tags.isEmpty()) { // (7)
            throw new IllegalArgumentException("Devi inserire almeno un tag!");  // (8)
        }
        
        for (String tag : tags) {   // (9)
            validazioneStringa(tag, Contesto.TAG); //(10)
        }
        
        // Validazione raccolta
        if (titoloRaccolta == null || titoloRaccolta.trim().isEmpty()) {   //(11)
            throw new IllegalArgumentException("Devi specificare una raccolta!");  //(12)
        }
        validazioneStringa(titoloRaccolta, Contesto.TITOLOR);  //(13)
        
        // Se vuole creare una nuova raccolta, valida anche la descrizione
        if (creaRaccoltaNuova) {     //(14)
            if (descrizioneRaccolta == null || descrizioneRaccolta.trim().isEmpty()) {  //(15)
                throw new IllegalArgumentException("Devi specificare la descrizione per la nuova raccolta!");   //(16)
            }
            validazioneStringa(descrizioneRaccolta, Contesto.DESCRIZIONER); //(17)
            
            // Verifica che non esista già una raccolta con quel titolo
            if (raccoltaEsiste(titoloRaccolta)) {  //(18)
                throw new IllegalArgumentException("Esiste già una raccolta con questo titolo! Scegli un altro nome o seleziona la raccolta esistente."); //(19)
            }
        } else { //(20)
            // Se vuole usare una esistente, verifica che esista
            if (!raccoltaEsiste(titoloRaccolta)) {  //(21)
                throw new IllegalArgumentException("La raccolta '" + titoloRaccolta + "' non esiste! Creala prima o scegli un'altra raccolta."); //(22)
            }
        }
        
        // Normalizza tag (lowercase)
        ArrayList<String> tagsNormalizzati = new ArrayList<>();  //(23)
        for (String tag : tags) {  //(24)
            tagsNormalizzati.add(tag.trim().toLowerCase());  //(25)
        }
        
        // Salva l'ID corrente prima della pubblicazione
        long idAutoreCorrente = autoreCorrente.getID();  //(26)
        
        // Pubblica la poesia (usa il metodo esistente di AutoreEntity)
        try {  //(27)
            long idPoesia = autoreCorrente.pubblicaPoesia( titolo, testo, tagsNormalizzati, pubblica, titoloRaccolta, creaRaccoltaNuova ? descrizioneRaccolta : null
            ); //(28)
            
            // AGGIUNGI: Se la pubblicazione ha successo, ricarica l'autore corrente
            if (idPoesia != -1) {    //(29)
                // Ricrea l'AutoreEntity con i dati aggiornati dal database
                this.autoreCorrente = new AutoreEntity(idAutoreCorrente);  //(30)
                this.utenteCorrente = this.autoreCorrente;  //(31)
                
                
            }
            
            return idPoesia;   //(32)   EXIT
            
        } catch(Exception e) {  //(33)
            JOptionPane.showMessageDialog(null,
                "Errore durante la pubblicazione: " + e.getMessage(),
                "ERRORE DATABASE", JOptionPane.ERROR_MESSAGE);
            return -1; //(34)  EXIT
        }
        
    } catch (IllegalArgumentException | UnknownHostException e) {  //(35)
        JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
        return -1;  //(36)  EXIT
    }
}

/**
 * Verifica se una raccolta con un dato titolo esiste per l'autore corrente
 * @param titoloRaccolta titolo della raccolta da verificare
 * @return true se la raccolta esiste
 */
private boolean raccoltaEsiste(String titoloRaccolta) {
    if (autoreCorrente == null) {
        return false;
    }
    
    return autoreCorrente.verificaRaccoltaPerTitolo(titoloRaccolta);
}

/**
 * Recupera tutte le raccolte dell'autore corrente
 * @return lista di raccolte dell'autore
 */
public ArrayList<RaccoltaEntity> getRaccoltaAutoreCorrente() {
    if (autoreCorrente == null) {
        JOptionPane.showMessageDialog(null,
            "Devi essere loggato come autore!",
            "ERRORE", JOptionPane.ERROR_MESSAGE);
        return new ArrayList<>();
    }
    
    return autoreCorrente.getRaccolteAutore();
}

/**
 * Recupera i titoli di tutte le raccolte dell'autore corrente
 * @return lista di titoli delle raccolte
 */
public ArrayList<String> getTitoliRaccolte() {
    if (autoreCorrente == null) {
        return new ArrayList<>();
    }
    
    ArrayList<String> titoli = new ArrayList<>();
    for (RaccoltaEntity raccolta : autoreCorrente.getRaccolteAutore()) {
        titoli.add(raccolta.getTitolo());
    }
    
    return titoli;
}

/**
 * Verifica se una raccolta esiste e appartiene all'autore corrente
 * @param idRaccolta ID della raccolta da verificare
 * @return true se la raccolta esiste ed è dell'autore corrente
 */
/*
private boolean verificaRaccoltaEsistente(long idRaccolta) {
    if (autoreCorrente == null) {
        return false;
    }
    
    // Delega la verifica all'entity AutoreEntity
    return autoreCorrente.verificaRaccoltaAppartenenza(idRaccolta);
}
*/
/**
 * Recupera tutte le raccolte dell'autore corrente
 * @return lista di raccolte dell'autore
 */

    /**
     * Visualizza il feed personale (ultime 5 poesie di altri autori)
     * @return lista di poesie
     */
    public ArrayList<PoesiaEntity> visualizzaFeed() {
        if (autoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato per visualizzare il feed!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
        
        return autoreCorrente.visualizzaFeed();
    }
    
    /**
     * Metti cuore a una poesia
     * @param idPoesia ID della poesia
     * @return true se il cuore è stato aggiunto
     */
    public boolean mettiCuore(long idPoesia) {
        if (autoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato per mettere un cuore!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return autoreCorrente.mettiCuore(idPoesia);
    }
    
    /**
     * Commenta una poesia
     * @param idPoesia ID della poesia
     * @param testoCommento testo del commento
     * @return true se il commento è stato aggiunto
     */
    public boolean commentaPoesia(long idPoesia, String testoCommento) {
        if (autoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato per commentare!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            validazioneStringa(testoCommento, Contesto.COMMENTO);
            return autoreCorrente.commentaPoesia(idPoesia, testoCommento);
            
        } catch (IllegalArgumentException|UnknownHostException  e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    
    // ==================== GESTIONE AMMINISTRATORE ====================
    
    /**
     * Genera report per un periodo
     * @param dataInizio data inizio periodo
     * @param dataFine data fine periodo
     */
    public ReportIntervalloDTO generaReportPoesieIntervallo(Timestamp dataInizio, Timestamp dataFine) {
        if (amministratoreCorrente == null) { /* ... errore ... */ return new ReportIntervalloDTO(0); }
        return amministratoreCorrente.generaReportPoesieIntervallo(dataInizio, dataFine);
    }
     
    public ArrayList<AutoreAttivitaDTO> generaReportAutoriAttivi() {
        if (amministratoreCorrente == null) { /* ... errore ... */ return new ArrayList<>(); }
        return amministratoreCorrente.generaReportAutoriAttivi();
    }
     
    public ArrayList<String> generaReportTagPiuUsati() {
        if (amministratoreCorrente == null) { /* ... errore ... */ return new ArrayList<>(); }
        return amministratoreCorrente.generaReportTagPiuUsati();
    }
     
    public ArrayList<String> generaReportPoesiePiuInterazioni() {
        if (amministratoreCorrente == null) { /* ... errore ... */ return new ArrayList<>(); }
        return amministratoreCorrente.generaReportPoesiePiuInterazioni();
    }
    
    /**
     * Visualizza tutti gli utenti
     * @return lista di utenti
     */
    public ArrayList<UtenteEntity> visualizzaTuttiUtenti() {
        if (amministratoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato come amministratore!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
        
        return amministratoreCorrente.visualizzaTuttiUtenti();
    }
    
    /**
     * Visualizza tutti gli autori
     * @return lista di autori
     */
    public ArrayList<AutoreEntity> visualizzaTuttiAutori() {
        if (amministratoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato come amministratore!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
        
        return amministratoreCorrente.visualizzaTuttiAutori();
    }
    
    /**
     * Elimina un utente
     * @param idUtente ID dell'utente da eliminare
     * @return true se l'eliminazione ha successo
     */
    public boolean eliminaUtente(long idUtente) {
        if (amministratoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato come amministratore!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return amministratoreCorrente.eliminaUtente(idUtente);
    }
    
    // ==================== GETTER ====================
    
    public UtenteEntity getUtenteCorrente() {
        return this.utenteCorrente;
    }
    
    public AutoreEntity getAutoreCorrente() {
        return this.autoreCorrente;
    }
    
    public AmministratoreEntity getAmministratoreCorrente() {
        return this.amministratoreCorrente;
    }
    
    public boolean isAutoreLoggato() {
        return this.autoreCorrente != null;
    }
    
    public boolean isAmministratoreLoggato() {
        return this.amministratoreCorrente != null;
    }
    
 // Aggiungi questi metodi al tuo Controller.java

    /**
     * Metodo per gestire i like con notifica
     */
    public boolean mettiCuoreConNotifica(long idPoesia, String nomeAutoreLike, String titoloPoesia) {
        if (mettiCuore(idPoesia)) {
            // Cerca l'autore della poesia per inviare la notifica
            PoesiaDAO poesiaDAO = new PoesiaDAO(idPoesia);
            long idAutorePoesia = poesiaDAO.getAutore().getIDUtenteD();
            
            // Se l'autore della poesia è online e diverso da chi mette il like
            if (idAutorePoesia != autoreCorrente.getID()) {
                NotificationManager.showLikeNotification(nomeAutoreLike, titoloPoesia);
            }
            return true;
        }
        return false;
    }
    
    public StatisticheDTO visualizzaStatistiche() { 
        if (autoreCorrente == null) {
            JOptionPane.showMessageDialog(null,
                "Devi essere loggato come autore!",
                "ERRORE", JOptionPane.ERROR_MESSAGE);
            return null; //  Ritorna null in caso di Autore non loggato
        }
        return autoreCorrente.getStatistiche(); 
    }
    /**
     * Metodo per gestire i commenti con notifica
     */
    public boolean commentaPoesiaConNotifica(long idPoesia, String testoCommento, String nomeAutoreCommento, String titoloPoesia) {
        if (commentaPoesia(idPoesia, testoCommento)) {
            // Cerca l'autore della poesia per inviare la notifica
            PoesiaDAO poesiaDAO = new PoesiaDAO(idPoesia);
            long idAutorePoesia = poesiaDAO.getAutore().getIDUtenteD();
            
            // Se l'autore della poesia è online e diverso da chi commenta
            if (idAutorePoesia != autoreCorrente.getID()) {
                NotificationManager.showCommentNotification(nomeAutoreCommento, titoloPoesia);
            }
            return true;
        }
        return false;
    }
    
    
    
}