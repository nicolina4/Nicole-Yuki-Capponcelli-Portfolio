package entity;

import DTO.AutoreAttivitaDTO;
import DTO.ReportIntervalloDTO;
import database.AmministratoreDAO;
import database.UtenteDAO;
import database.AutoreDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import java.sql.Timestamp;

public class AmministratoreEntity extends UtenteEntity {
    
    // Costruttore di default
    public AmministratoreEntity() {
        super();
    }

    // Costruttore
    public AmministratoreEntity(long ID, String email, String password, LocalDateTime dataRegistrazione) {
        super(ID, email, password, dataRegistrazione);
    }
    
    // Costruttore per inizializzare tramite ID
    public AmministratoreEntity(long ID) {
        super();
        this.IDUtenteE = ID;
        
    }
    
    // Costruttore per inizializzare tramite email
    public AmministratoreEntity(String email) {
        super();
        this.email = email;
        AmministratoreDAO ADAO = new AmministratoreDAO();
        ADAO.setEmail(email);
        ADAO.readUtenteByEmail();
        
        this.IDUtenteE = ADAO.getIDUtenteD();
        this.password = ADAO.getPassword();
        this.dataRegistrazione = ADAO.getDataRegistrazione();
    }
    
    // Costruttore per inizializzare tramite DAO
    public AmministratoreEntity(AmministratoreDAO ADAO) {
        if (ADAO != null) {
            this.IDUtenteE = ADAO.getIDUtenteD();
            this.email = ADAO.getEmail();
            this.password = ADAO.getPassword();
            this.dataRegistrazione = ADAO.getDataRegistrazione();
        }
    }
    
    // Registrazione amministratore
    @Override
    public boolean registraUtente(String email, String password) {
        AmministratoreDAO ADAO = new AmministratoreDAO();
        
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
        
        // Creazione nel database
        if (ADAO.createUtente(email, password)) {
            this.IDUtenteE = ADAO.getNuovoID() - 1;
            this.dataRegistrazione = LocalDateTime.now();
            
            JOptionPane.showMessageDialog(null, 
                "Amministratore registrato con successo", 
                "REGISTRAZIONE EFFETTUATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    // Login amministratore
    @Override
    public boolean login(String email, String password) {
        AmministratoreDAO ADAO = new AmministratoreDAO();
        ADAO.setEmail(email);
        ADAO.setPassword(password);
        
        if (ADAO.readUtenteByAllData(email, password)) {
            ADAO.readUtenteByEmail();
            this.IDUtenteE = ADAO.getIDUtenteD();
            this.email = ADAO.getEmail();
            this.password = ADAO.getPassword();
            this.dataRegistrazione = ADAO.getDataRegistrazione();
            
            return true;
        }
        return false;
    }
    
    // Visualizza tutti gli utenti registrati
    public ArrayList<UtenteEntity> visualizzaTuttiUtenti() {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        ArrayList<UtenteDAO> listaDAO = ADAO.readAllUtenti();
        ArrayList<UtenteEntity> listaEntity = new ArrayList<>();
        
        for (UtenteDAO UDAO : listaDAO) {
            UtenteEntity UENT = new UtenteEntity(UDAO);
            listaEntity.add(UENT);
        }
        
        JOptionPane.showMessageDialog(null, 
            "Trovati " + listaEntity.size() + " utenti nel sistema", 
            "LISTA UTENTI", 
            JOptionPane.INFORMATION_MESSAGE);
        
        return listaEntity;
    }
    
    // Visualizza tutti gli autori
    public ArrayList<AutoreEntity> visualizzaTuttiAutori() {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        ArrayList<AutoreDAO> listaDAO = ADAO.readAllAutori();
        ArrayList<AutoreEntity> listaEntity = new ArrayList<>();
        
        for (AutoreDAO AUTODAO : listaDAO) {
            AutoreEntity AENT = new AutoreEntity(AUTODAO);
            listaEntity.add(AENT);
        }
        
        JOptionPane.showMessageDialog(null, 
            "Trovati " + listaEntity.size() + " autori nel sistema", 
            "LISTA AUTORI", 
            JOptionPane.INFORMATION_MESSAGE);
        
        return listaEntity;
    }
    
    // Elimina un utente dal sistema
    public boolean eliminaUtente(long IDUtente) {
        int conferma = JOptionPane.showConfirmDialog(null, 
            "Sei sicuro di voler eliminare l'utente con ID " + IDUtente + "?\nQuesta operazione è irreversibile.", 
            "CONFERMA ELIMINAZIONE", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (conferma == JOptionPane.YES_OPTION) {
            UtenteDAO UDAO = new UtenteDAO(IDUtente);
            
            if (UDAO.deleteUtente()) {
                JOptionPane.showMessageDialog(null, 
                    "Utente eliminato con successo", 
                    "ELIMINAZIONE COMPLETATA", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Errore durante l'eliminazione dell'utente", 
                    "ERRORE", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    // Genera report - poesie pubblicate in un intervallo
    public ReportIntervalloDTO generaReportPoesieIntervallo(Timestamp dataInizio, Timestamp dataFine) {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        
        // 1. Delega al DAO per l'esecuzione della query
        ReportIntervalloDTO report = ADAO.generaReportPoesieInIntervallo(dataInizio, dataFine);
        
        // 2. DEBUG TEMPORANEO (se necessario, altrimenti la Boundary gestisce l'output)
        System.out.println("--- DEBUG: Report Poesie in Intervallo (Entity) ---");
        System.out.println("Totale Poesie Pubblicate: " + report.getTotalePoesie());
        System.out.println("---------------------------------------------------");
        
        // 3. Restituisce il DTO
        return report;
    }
     
    public ArrayList<AutoreAttivitaDTO> generaReportAutoriAttivi() {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        
        // 1. Delega al DAO per l'esecuzione della query e la mappatura iniziale
        ArrayList<AutoreAttivitaDTO> report = ADAO.generaReportAutoriPiuAttivi();
        
        // 2. DEBUG TEMPORANEO (Puoi rimuoverlo o lasciarlo per log di servizio)
        System.out.println("--- DEBUG: Report Autori Più Attivi (Entity) ---");
        if (report.isEmpty()) {
            System.out.println("Nessun autore trovato.");
        } else {
            for (AutoreAttivitaDTO autore : report) {
                System.out.println(autore.getNomeCompleto() + ": " + autore.getNumPoesie() + " poesie");
            }
        }
        System.out.println("----------------------------------------------");
        
        // 3. Restituisce la lista di DTOs
        return report;
    }
     
    public ArrayList<String> generaReportTagPiuUsati() {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
     // 1. Delega al DAO per l'esecuzione della query
        ArrayList<String> report = ADAO.generaReportTagPiuUsati();
        // 2. DEBUG TEMPORANEO 
        System.out.println("--- DEBUG: Report Tag Più Usati (Entity) ---");
        if (report.isEmpty()) {
            System.out.println("Nessun tag trovato.");
        } else {
            for (String tag : report) {
                System.out.println(tag);
            }
        }
        System.out.println("------------------------------------------");
        // 3. Restituisce la lista di DTOs
        return report;
    }
     
    public ArrayList<String> generaReportPoesiePiuInterazioni() {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        // 1. Delega al DAO per l'esecuzione della query
        ArrayList<String> report = ADAO.generaReportPoesiePiuInterazioni();
        // 2. DEBUG TEMPORANEO 
        System.out.println("--- DEBUG: Report Poesie Più Interagite (Entity) ---");
        if (report.isEmpty()) {
            System.out.println("Nessuna poesia con interazioni trovata.");
        } else {
            for (String poesia : report) {
                System.out.println(poesia);
            }
        }
        System.out.println("---------------------------------------------------");
        // 3. Restituisce la lista di DTOs
        return report;
    }
    
    // Genera report generico
    public void generaReport(String tipoReport, Timestamp dataInizio, Timestamp dataFine) {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        ADAO.generaReport(tipoReport, dataInizio, dataFine);
        
        JOptionPane.showMessageDialog(null, 
            "Report '" + tipoReport + "' generato con successo", 
            "REPORT GENERATO", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Visualizza statistiche generali della piattaforma
    public String visualizzaStatistichePiattaforma() {
        AmministratoreDAO ADAO = new AmministratoreDAO(this.IDUtenteE);
        
        ArrayList<UtenteDAO> listaUtenti = ADAO.readAllUtenti();
        ArrayList<AutoreDAO> listaAutori = ADAO.readAllAutori();
        
        String statistiche = "=== STATISTICHE PIATTAFORMA ===\n\n";
        statistiche += "Utenti totali registrati: " + listaUtenti.size() + "\n";
        statistiche += "Autori totali: " + listaAutori.size() + "\n";
        statistiche += "\nPer statistiche dettagliate, genera i report specifici.";
        
        JOptionPane.showMessageDialog(null, 
            statistiche, 
            "STATISTICHE PIATTAFORMA", 
            JOptionPane.INFORMATION_MESSAGE);
        
        return statistiche;
    }
    
    @Override
    public String toString() {
        String result = "\n=== AMMINISTRATORE ===";
        result += super.toString();
        result += "\nRuolo: Amministratore della piattaforma";
        return result;
    }
}