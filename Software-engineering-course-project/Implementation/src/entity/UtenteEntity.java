package entity;

import database.UtenteDAO;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

public class UtenteEntity {
    protected long IDUtenteE;
    protected String email;
    protected String password;
    protected LocalDateTime dataRegistrazione;

    // Costruttore di default
    public UtenteEntity() {
        super();
    }
    
    // Costruttore per sottoclassi
    public UtenteEntity(long IDUtenteE, String email, String password, LocalDateTime dataRegistrazione) {
        this.IDUtenteE = IDUtenteE;
        this.email = email;
        this.password = password;
        this.dataRegistrazione = dataRegistrazione;
    }

    // Costruttore per inizializzare tramite chiave primaria
    public UtenteEntity(long ID) {
        super();
        UtenteDAO UDAO = new UtenteDAO(ID);
        this.IDUtenteE = UDAO.getIDUtenteD();
        this.email = UDAO.getEmail();
        this.password = UDAO.getPassword();
        this.dataRegistrazione = UDAO.getDataRegistrazione();
    }
    
    // Costruttore email
    public UtenteEntity(String email) {
        super();
        this.email = email;
        UtenteDAO UDAO = new UtenteDAO(email);
        
        this.IDUtenteE = UDAO.getIDUtenteD();
        this.email = UDAO.getEmail();
        this.password = UDAO.getPassword();
        this.dataRegistrazione = UDAO.getDataRegistrazione();
    }

    // Costruttore per inizializzare tramite oggetto DAO già definito
    public UtenteEntity(UtenteDAO UDAO) {
        if (UDAO != null) {
            this.IDUtenteE = UDAO.getIDUtenteD();
            this.email = UDAO.getEmail();
            this.password = UDAO.getPassword();
            this.dataRegistrazione = UDAO.getDataRegistrazione();
        }
    }

    // Equals basato su email
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        UtenteEntity other = (UtenteEntity) obj;
        return this.email.equalsIgnoreCase(other.email);
    }

    // Metodi get
    public long getID() { 
        return this.IDUtenteE; 
    }
    
    public String getEmail() { 
        return this.email; 
    }
    
    public String getPassword() { 
        return this.password; 
    }
    
    public LocalDateTime getDataRegistrazione() {
        return this.dataRegistrazione;
    }

    // Metodi set
    public void setID(long ID) {
        this.IDUtenteE = ID; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setPassword(String password) {
        this.password = password; 
    }
    
    public void setDataRegistrazione(LocalDateTime dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    // Inserimento dati utente
    public void inserimentoDatiUtente(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }
    
    // Registrazione nuovo utente
    public boolean registraUtente(String email, String password) {
        UtenteDAO UDAO = new UtenteDAO();
        
        // Verifica se l'email esiste già
        if (UDAO.checkEmailRegistrata(email)) {
            JOptionPane.showMessageDialog(null, 
                "Email già registrata nel sistema", 
                "ERRORE REGISTRAZIONE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Inserisce i dati
        this.inserimentoDatiUtente(email, password);
        UDAO.setEmail(email);
        UDAO.setPassword(password);
        
        // Crea l'utente nel database
        if (UDAO.createUtente(email, password)) {
            this.IDUtenteE = UDAO.getNuovoID() - 1; // L'ID appena creato
            this.dataRegistrazione = LocalDateTime.now();
            
            JOptionPane.showMessageDialog(null, 
                "Registrazione effettuata con successo!", 
                "REGISTRAZIONE COMPLETATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                "Errore durante la registrazione", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Login utente
    public boolean login(String email, String password) {
        UtenteDAO UDAO = new UtenteDAO();
        UDAO.setEmail(email);
        UDAO.setPassword(password);
        
        // Verifica credenziali
        if (UDAO.readUtenteByAllData(email, password)) {
            UDAO.readUtenteByEmail();
            this.IDUtenteE = UDAO.getIDUtenteD();
            this.email = UDAO.getEmail();
            this.password = UDAO.getPassword();
            this.dataRegistrazione = UDAO.getDataRegistrazione();
            
            return true;
        }
        return false;
    }
    
    // Aggiorna dati utente
    public boolean aggiornaProfilo(String nuovaEmail, String nuovaPassword) {
        UtenteDAO UDAO = new UtenteDAO(this.IDUtenteE);
        
        if (nuovaEmail != null && !nuovaEmail.isEmpty()) {
            UDAO.setEmail(nuovaEmail);
            this.email = nuovaEmail;
        }
        
        if (nuovaPassword != null && !nuovaPassword.isEmpty()) {
            UDAO.setPassword(nuovaPassword);
            this.password = nuovaPassword;
        }
        
        if (UDAO.updateUtente()) {
            JOptionPane.showMessageDialog(null, 
                "Profilo aggiornato con successo", 
                "AGGIORNAMENTO COMPLETATO", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                "Errore durante l'aggiornamento del profilo", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Elimina account utente
    public boolean eliminaAccount() {
        UtenteDAO UDAO = new UtenteDAO(this.IDUtenteE);
        
        int conferma = JOptionPane.showConfirmDialog(null, 
            "Sei sicuro di voler eliminare il tuo account?\nQuesta operazione è irreversibile.", 
            "CONFERMA ELIMINAZIONE", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (conferma == JOptionPane.YES_OPTION) {
            if (UDAO.deleteUtente()) {
                JOptionPane.showMessageDialog(null, 
                    "Account eliminato con successo", 
                    "ELIMINAZIONE COMPLETATA", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Errore durante l'eliminazione dell'account", 
                    "ERRORE", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        String result = "\nEmail: " + this.email +
                       "\nData registrazione: " + this.dataRegistrazione;
        return result;
    }
}