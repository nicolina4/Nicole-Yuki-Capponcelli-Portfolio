package entity;

import DTO.StatisticheDTO;
import java.util.ArrayList;
import database.AutoreDAO;
import database.PoesiaDAO;
import database.RaccoltaDAO;
import java.time.LocalDateTime;
import javax.swing.*;

public class AutoreEntity extends UtenteEntity {
    private String nome;
    private String cognome;
    private String bio;
    private String immagineProfilo;
    private ArrayList<PoesiaEntity> poesiePubblicate;
    private ArrayList<RaccoltaEntity> raccolteAutore;

    // Costruttore di default
    public AutoreEntity() {
        super();
        this.poesiePubblicate = new ArrayList<PoesiaEntity>();
        this.raccolteAutore = new ArrayList<RaccoltaEntity>();
    }

    // Costruttore da superclasse
    public AutoreEntity(long ID, String email, String password, LocalDateTime dataRegistrazione, 
                       String nome, String cognome, String bio, String immagineProfilo) {
        super(ID, email, password, dataRegistrazione);
        this.nome = nome;
        this.cognome = cognome;
        this.bio = bio;
        this.immagineProfilo = immagineProfilo;
        this.poesiePubblicate = new ArrayList<PoesiaEntity>();
        this.raccolteAutore = new ArrayList<RaccoltaEntity>();
    }
    
    // Costruttore per inizializzare tramite chiave primaria
    public AutoreEntity(long ID) {
        super();
        this.poesiePubblicate = new ArrayList<PoesiaEntity>();
        this.raccolteAutore = new ArrayList<RaccoltaEntity>();
        this.IDUtenteE = ID;
        AutoreDAO ADAO = new AutoreDAO(ID);
        this.email = ADAO.getEmail();
        this.password = ADAO.getPassword();
        this.dataRegistrazione = ADAO.getDataRegistrazione();
        this.nome = ADAO.getNome();
        this.cognome = ADAO.getCognome();
        this.bio = ADAO.getBio();
        this.immagineProfilo = ADAO.getImmagineProfilo();
        
        caricaListaPoesiePubblicate(ADAO);
        caricaListaRaccolte(ADAO);
    }
    
    // Costruttore per inizializzare tramite mail
    public AutoreEntity(String email) {
        super();
        this.poesiePubblicate = new ArrayList<PoesiaEntity>();
        this.raccolteAutore = new ArrayList<RaccoltaEntity>();
        this.email = email;
        AutoreDAO ADAO = new AutoreDAO(email);
        
        this.IDUtenteE = ADAO.getIDUtenteD();
        this.email = ADAO.getEmail();
        this.password = ADAO.getPassword();
        this.dataRegistrazione = ADAO.getDataRegistrazione();
        this.nome = ADAO.getNome();
        this.cognome = ADAO.getCognome();
        this.bio = ADAO.getBio();
        this.immagineProfilo = ADAO.getImmagineProfilo();

        caricaListaPoesiePubblicate(ADAO);
        caricaListaRaccolte(ADAO);
    }
    
    // Costruttore per inizializzare tramite oggetto DAO già definito
    public AutoreEntity(AutoreDAO ADAO) {
        this.poesiePubblicate = new ArrayList<PoesiaEntity>();
        this.raccolteAutore = new ArrayList<RaccoltaEntity>();
        if (ADAO != null) {
            this.IDUtenteE = ADAO.getIDUtenteD();
            this.email = ADAO.getEmail();
            this.password = ADAO.getPassword();
            this.dataRegistrazione = ADAO.getDataRegistrazione();
            this.nome = ADAO.getNome();
            this.cognome = ADAO.getCognome();
            this.bio = ADAO.getBio();
            this.immagineProfilo = ADAO.getImmagineProfilo();

            caricaListaPoesiePubblicate(ADAO);
            caricaListaRaccolte(ADAO);
        }
    }

    // Metodi get
    public String getNome() { 
        return this.nome; 
    }
    
    public String getCognome() { 
        return this.cognome; 
    }
    
    public String getBio() { 
        return this.bio; 
    }
    
    public String getImmagineProfilo() { 
        return this.immagineProfilo; 
    }
    
    public ArrayList<PoesiaEntity> getPoesiePubblicate() { 
        return this.poesiePubblicate;
    }
    
    public ArrayList<RaccoltaEntity> getRaccolteAutore() { 
        return this.raccolteAutore; 
    }
    
    // Metodi set
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public void setCognome(String cognome) { 
        this.cognome = cognome; 
    }
    
    public void setBio(String bio) { 
        this.bio = bio; 
    }
    
    public void setImmagineProfilo(String immagineProfilo) { 
        this.immagineProfilo = immagineProfilo; 
    }
    
    public void setPoesiePubblicate(ArrayList<PoesiaEntity> poesiePubblicate) { 
        this.poesiePubblicate = poesiePubblicate; 
    }
    
    public void setRaccolteAutore(ArrayList<RaccoltaEntity> raccolteAutore) { 
        this.raccolteAutore = raccolteAutore; 
    }
    
    // Registrazione autore
    @Override
    public boolean registraUtente(String email, String password) {
        AutoreDAO ADAO = new AutoreDAO();
        // Verifica unicità email
        if (ADAO.checkEmailRegistrata(email)) {
            JOptionPane.showMessageDialog(null, 
                "Email già registrata", 
                "ERRORE REGISTRAZIONE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Inserimento dati
        this.inserimentoDatiUtente(email, password);
        ADAO.setEmail(email);
        ADAO.setPassword(password);
        ADAO.setNome(this.nome);
        ADAO.setCognome(this.cognome);
        ADAO.setBio(this.bio);
        ADAO.setImmagineProfilo(this.immagineProfilo);
        // Creazione nel database
        if (ADAO.createUtente(email, password)) {
            this.IDUtenteE = ADAO.getNuovoID() - 1;
            this.dataRegistrazione = LocalDateTime.now();
            
            JOptionPane.showMessageDialog(null, 
                "Benvenuto " + this.nome + "! Registrazione completata.", 
                "REGISTRAZIONE EFFETTUATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    // Login autore
    @Override
    public boolean login(String email, String password) {
        AutoreDAO ADAO = new AutoreDAO();
        ADAO.setEmail(email);
        ADAO.setPassword(password);         
  
        if (ADAO.readUtenteByAllData(email, password)) {
            ADAO.readUtenteByEmail();
            this.IDUtenteE = ADAO.getIDUtenteD();
            this.email = ADAO.getEmail();
            this.password = ADAO.getPassword();
            this.dataRegistrazione = ADAO.getDataRegistrazione();
            this.nome = ADAO.getNome();
            this.cognome = ADAO.getCognome();
            this.bio = ADAO.getBio();
            this.immagineProfilo = ADAO.getImmagineProfilo();
            
            caricaListaPoesiePubblicate(ADAO);
            caricaListaRaccolte(ADAO);
            
            return true;
        }
        return false;
    }
    
    // Aggiorna profilo autore
    public boolean aggiornaProfilo(String nuovaEmail, String nuovaPassword, 
                                  String nuovaNome, String nuovoCognome, 
                                  String nuovaBio, String nuovaImmagine) {
        AutoreDAO ADAO = new AutoreDAO(this.IDUtenteE);
        
        if (nuovaEmail != null && !nuovaEmail.isEmpty()) {
            ADAO.setEmail(nuovaEmail);
            this.email = nuovaEmail;
        }
        
        if (nuovaPassword != null && !nuovaPassword.isEmpty()) {
            ADAO.setPassword(nuovaPassword);
            this.password = nuovaPassword;
        }
        
        if (nuovaNome != null && !nuovaNome.isEmpty()) {
            ADAO.setNome(nuovaNome);
            this.nome = nuovaNome;
        }
        
        if (nuovoCognome != null && !nuovoCognome.isEmpty()) {
            ADAO.setCognome(nuovoCognome);
            this.cognome = nuovoCognome;
        }
        
        if (nuovaBio != null) {
            ADAO.setBio(nuovaBio);
            this.bio = nuovaBio;
        }
        
        if (nuovaImmagine != null) {
            ADAO.setImmagineProfilo(nuovaImmagine);
            this.immagineProfilo = nuovaImmagine;
        }
        
        if (ADAO.updateUtente()) {
            JOptionPane.showMessageDialog(null, 
                "Profilo aggiornato con successo", 
                "AGGIORNAMENTO COMPLETATO", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    /**
 * Verifica se esiste una raccolta con un dato titolo per questo autore
 * @param titoloRaccolta titolo da verificare
 * @return true se la raccolta esiste
 */
public boolean verificaRaccoltaPerTitolo(String titoloRaccolta) {
    for (RaccoltaEntity r : this.raccolteAutore) {
        if (r.getTitolo().equalsIgnoreCase(titoloRaccolta)) {
            return true;
        }
    }
    return false;
}


    
   

// Pubblica poesia
public long pubblicaPoesia(String titolo, String testo, ArrayList<String> tag,
                          boolean pubblica, String titoloRaccolta, String descrizioneRaccolta) {
    
    // Validazione: massimo 500 caratteri
    if (testo.length() > 500) {
        JOptionPane.showMessageDialog(null, 
            "Il testo della poesia supera i 500 caratteri consentiti", 
            "ERRORE PUBBLICAZIONE", 
            JOptionPane.ERROR_MESSAGE);
        return -1;
    }
    
    LocalDateTime dataPubblicazione = LocalDateTime.now();
    
    PoesiaEntity PENT = new PoesiaEntity();
    PoesiaDAO PDAO = new PoesiaDAO();
    
    PENT.setTitolo(titolo);
    PENT.setTesto(testo);
    PENT.setTag(tag);
    PENT.setPubblica(pubblica);
    PENT.setDataPubblicazione(dataPubblicazione);
    PENT.setAutore(this);
    
    // Gestione raccolta
    RaccoltaEntity RENT = null;
    Long idRaccolta = null;
    
    if (titoloRaccolta != null && !titoloRaccolta.isEmpty()) {
        // Cerca se esiste già una raccolta con quel titolo
        for (RaccoltaEntity r : this.raccolteAutore) {
            if (r.getTitolo().equalsIgnoreCase(titoloRaccolta)) {
                RENT = r;
                idRaccolta = RENT.getIdRaccolta();
                break;
            }
        }
        
        // Se non esiste, crea nuova raccolta
        if (RENT == null && descrizioneRaccolta != null) {
            RaccoltaDAO RDAO = new RaccoltaDAO();
            
            idRaccolta = RDAO.createRaccolta(titoloRaccolta, descrizioneRaccolta, this.IDUtenteE);
            
            
            
            if (idRaccolta > 0) {
                RENT = new RaccoltaEntity();
                RENT.setIdRaccolta(idRaccolta);
                RENT.setTitolo(titoloRaccolta);
                RENT.setDescrizione(descrizioneRaccolta);
                RENT.setAutoreRaccolta(this);
                this.raccolteAutore.add(RENT);
            }
        }
        PENT.setRaccolta(RENT);
    }
    
    // Configura il DAO con i dati della poesia
    PDAO.setTitolo(titolo);
    PDAO.setTesto(testo);
    PDAO.setTag(tag);
    PDAO.setPubblica(pubblica);
    PDAO.setDataPubblicazione(dataPubblicazione);
    PDAO.setAutore(new AutoreDAO(this.IDUtenteE));
    if (idRaccolta != null) {
        PDAO.setRaccolta(new RaccoltaDAO(idRaccolta));
    }
    
    // Crea la poesia nel database - USANDO LA FIRMA CORRETTA
    boolean success = PDAO.createPoesia(titolo, testo, tag, pubblica, 
                                       dataPubblicazione, this.IDUtenteE, idRaccolta);
    
    
    
    if (success) {
        PENT.setIdPoesia(PDAO.getIDPoesiaD()); // Imposta l'ID generato
        
        
        AutoreDAO ADAO = new AutoreDAO();
        ADAO.salvaTagPoesia(PDAO.getIDPoesiaD(),tag);
        
        this.poesiePubblicate.add(PENT);
        aggiornaRaccolte();
        
        JOptionPane.showMessageDialog(null,
            "Poesia pubblicata con successo!" + 
            (RENT != null && descrizioneRaccolta != null ? " (Nuova raccolta creata)" : ""),
            "PUBBLICAZIONE COMPLETATA",
            JOptionPane.INFORMATION_MESSAGE);
        
        
        ADAO.setEmail(email);
        ADAO.setPassword(password);
        
        ADAO.caricaPoesieDaDB();
        
        
        ADAO.caricaRaccolteDaDB();
        caricaListaPoesiePubblicate(ADAO);
        caricaListaRaccolte(ADAO);
        
        
        
        
        return PENT.getIdPoesia();
    } else {
        JOptionPane.showMessageDialog(null,
            "Errore durante la pubblicazione",
            "ERRORE",
            JOptionPane.ERROR_MESSAGE);
        return -1;
    }
    
        
}

    
    // Visualizza feed di poesie di altri autori
    public ArrayList<PoesiaEntity> visualizzaFeed() {
        ArrayList<PoesiaEntity> feed = new ArrayList<>();
        ArrayList<PoesiaDAO> feedDAO = PoesiaDAO.getFeedAutoriDiversi(this.IDUtenteE);
        
        for (PoesiaDAO PDAO : feedDAO) {
            PoesiaEntity PENT = new PoesiaEntity(PDAO);
            feed.add(PENT);
        }
        
        return feed;
    }
    
    // Metti cuore a una poesia
    public boolean mettiCuore(long IDPoesia) {
        PoesiaDAO PDAO = new PoesiaDAO(IDPoesia);
        PDAO.addCuore(this.IDUtenteE);
        
        JOptionPane.showMessageDialog(null, 
            "Cuore aggiunto alla poesia", 
            "APPREZZAMENTO REGISTRATO", 
            JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    public boolean commentaPoesia(long IDPoesia, String testoCommento) {
        // 1. Validazione (lasciamo la validazione di base per evitare l'uso di 'null' nel DB)
        if (testoCommento == null || testoCommento.trim().isEmpty()) {
            // Non mostrare JOptionPane, ritorna solo false.
            return false;
        }
        try {
            // 2. Mappatura diretta a DAO
            PoesiaDAO PDAO = new PoesiaDAO(IDPoesia);
            database.CommentoDAO CDAO = new database.CommentoDAO();
            CDAO.setTesto(testoCommento);
            CDAO.setDataPubblicazione(LocalDateTime.now());
            // Mappa l'AutoreEntity corrente in AutoreDAO per avere l'IDUtenteD
            CDAO.setAutoreCommento(new database.AutoreDAO(this.IDUtenteE)); 
            // 3. Persistenza: Chiama il DAO e ritorna il risultato.
            if (PDAO.addCommento(CDAO)) {
                // Ritorna SOLO il booleano di successo
                return true;
            }
        } catch (Exception e) {
            // Logga l'errore senza interrompere la Boundary
            System.err.println("❌ Errore imprevisto durante la persistenza del commento: " + e.getMessage());
            e.printStackTrace();
        }
        // Se la persistenza fallisce o c'è un'eccezione
        return false;
    }
    
    
    public StatisticheDTO getStatistiche() {
        long idAutore = this.IDUtenteE;      //(1)
        int totaleCuori = PoesiaDAO.getTotalCuoriByAutore(idAutore); // (2)
        int totaleCommenti = PoesiaDAO.getTotalCommentiByAutore(idAutore); //(3)
        PoesiaDAO topPoesiaDAO = PoesiaDAO.getTopPoesiaByAutore(idAutore); //(4)
        PoesiaEntity poesiaPiuApprezzata = null;  //(5)
        if (topPoesiaDAO != null) {                 // (6)
            poesiaPiuApprezzata = new PoesiaEntity();   //(7)
            poesiaPiuApprezzata.setIdPoesia(topPoesiaDAO.getIDPoesiaD());     //(8)
            poesiaPiuApprezzata.setTitolo(topPoesiaDAO.getTitolo());    //(9)
            poesiaPiuApprezzata.setCuore(topPoesiaDAO.getCuore());      //(10)
        }
     
        return new StatisticheDTO(      //(11)
            totaleCuori,
            totaleCommenti,
            poesiaPiuApprezzata
        );
    }
    
    
    
    
    
    // Carica lista delle poesie pubblicate
    private void caricaListaPoesiePubblicate(AutoreDAO ADAO) {
    this.poesiePubblicate = new ArrayList<PoesiaEntity>();
    
    if (ADAO.getListaPoesiePubblicate() != null) {
        
        
        for (PoesiaDAO PDAO : ADAO.getListaPoesiePubblicate()) {
            PoesiaEntity PENT = new PoesiaEntity();
            
            PENT.setIdPoesia(PDAO.getIDPoesiaD());
            PENT.setTitolo(PDAO.getTitolo());
            PENT.setTesto(PDAO.getTesto());
            PENT.setTag(PDAO.getTag());
            PENT.setPubblica(PDAO.getPubblica());
            PENT.setDataPubblicazione(PDAO.getDataPubblicazione());
            PENT.setCuore(PDAO.getCuore());
            
            PENT.setAutore(this);
            PENT.caricaCommenti(PDAO);
            
            this.poesiePubblicate.add(PENT);
            
            
        }
    }
    
    
}
    
    // Carica lista raccolte di un autore
    private void caricaListaRaccolte(AutoreDAO ADAO) {
        this.raccolteAutore = new ArrayList<RaccoltaEntity>();
        
        if (ADAO.getListaRaccolteAutore() != null) {
            for (RaccoltaDAO RDAO : ADAO.getListaRaccolteAutore()) {
                RaccoltaEntity RENT = new RaccoltaEntity(RDAO);
                
                
                RENT.setIdRaccolta(RDAO.getIDRaccoltaD());
                RENT.setTitolo(RDAO.getTitolo());
                RENT.setDescrizione(RDAO.getDescrizione());
                RENT.setAutoreRaccolta(this);
                
                RDAO.readListaPoesie();
                
                RENT.caricaPoesie(RDAO);
                
                
                
                this.raccolteAutore.add(RENT);
            }
        }
    }
    
    /**
 * Aggiorna la lista delle raccolte con i dati più recenti dal database per pubblica poesie
 */
public void aggiornaRaccolte() {
    AutoreDAO ADAO = new AutoreDAO(this.IDUtenteE);
    ADAO.caricaRaccolteDaDB(); // Ricarica dal database
    caricaListaRaccolte(ADAO); // Ricarica nella entity
    
    
    
}
    
    @Override
    public String toString() {
        String result = "\nNome: " + nome +
                       "\nCognome: " + cognome +
                       "\nBio: " + bio +
                       "\nImmagine profilo: " + immagineProfilo;
        
        if (!poesiePubblicate.isEmpty()) {
            result += "\n\nLista delle poesie pubblicate:\n";
            for (PoesiaEntity PENT : poesiePubblicate) {
                result += "\n- Titolo: " + PENT.getTitolo() +
                         "\n  Data: " + PENT.getDataPubblicazione() +
                         "\n  Cuori: " + PENT.getCuore();
            }
        } else {
            result += "\n\nL'autore non ha pubblicato poesie";
        }
        
        if (!raccolteAutore.isEmpty()) {
            result += "\n\nLista raccolte dell'autore:\n";
            for (RaccoltaEntity RENT : raccolteAutore) {
                result += "\n- " + RENT.getTitolo() + ": " + RENT.getDescrizione();
            }
        } else {
            result += "\n\nL'autore non ha creato raccolte";
        }
        
        return super.toString() + result;
    }
}
        
        
        
        
        

