package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import database.CommentoDAO;
import javax.swing.JOptionPane;

public class CommentoEntity {
    private long IDCommentoE;
    private String testo;
    private LocalDateTime dataPubblicazione;
    private AutoreEntity autoreCommento;

    // Costruttore di default
    public CommentoEntity() {
    }

    // Costruttore tramite DAO
    public CommentoEntity(CommentoDAO CDAO) {
        if (CDAO != null) {
            this.IDCommentoE = CDAO.getIDCommentoD();
            this.testo = CDAO.getTesto();
            this.dataPubblicazione = CDAO.getDataPubblicazione();
            
            if (CDAO.getAutoreCommento() != null) {
                this.autoreCommento = new AutoreEntity(CDAO.getAutoreCommento());
            }
        }
    }
    
    // Costruttore completo
    public CommentoEntity(String testo, LocalDateTime dataPubblicazione, AutoreEntity autore) {
        this.testo = testo;
        this.dataPubblicazione = dataPubblicazione;
        this.autoreCommento = autore;
    }

    // Metodi get
    public long getIdCommento() { 
        return this.IDCommentoE; 
    }
    
    public String getTesto() { 
        return this.testo; 
    }
    
    public LocalDateTime getDataPubblicazione() { 
        return this.dataPubblicazione; 
    }
    
    public AutoreEntity getAutoreCommento() { 
        return this.autoreCommento; 
    }
    
    // Metodi set
    public void setIdCommento(long IDCommento) { 
        this.IDCommentoE = IDCommento; 
    }
    
    public void setTesto(String testo) { 
        this.testo = testo; 
    }
    
    public void setDataPubblicazione(LocalDateTime dataPubblicazione) { 
        this.dataPubblicazione = dataPubblicazione; 
    }
    
    public void setAutoreCommento(AutoreEntity autoreCommento) { 
        this.autoreCommento = autoreCommento; 
    }
    
    // Crea un nuovo commento per una poesia
    public boolean creaCommento(long IDPoesia) {
        // Validazione
        if (this.testo == null || this.testo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Il testo del commento non può essere vuoto", 
                "ERRORE VALIDAZIONE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (this.autoreCommento == null) {
            JOptionPane.showMessageDialog(null, 
                "Autore del commento non specificato", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        CommentoDAO CDAO = new CommentoDAO();
        CDAO.setTesto(this.testo);
        CDAO.setDataPubblicazione(LocalDateTime.now());
        CDAO.setAutoreCommento(new database.AutoreDAO(this.autoreCommento.getID()));
        
        CDAO.createCommento(IDPoesia);
        this.dataPubblicazione = LocalDateTime.now();
        
        JOptionPane.showMessageDialog(null, 
            "Commento pubblicato con successo", 
            "COMMENTO CREATO", 
            JOptionPane.INFORMATION_MESSAGE);
        
        return true;
    }
    
    // Modifica il testo del commento
    public boolean modificaCommento(String nuovoTesto) {
        if (nuovoTesto == null || nuovoTesto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Il nuovo testo non può essere vuoto", 
                "ERRORE VALIDAZIONE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        CommentoDAO CDAO = new CommentoDAO(this.IDCommentoE);
        CDAO.setTesto(nuovoTesto);
        CDAO.updateCommento();
        
        this.testo = nuovoTesto;
        
        JOptionPane.showMessageDialog(null, 
            "Commento modificato con successo", 
            "MODIFICA COMPLETATA", 
            JOptionPane.INFORMATION_MESSAGE);
        
        return true;
    }
    
    // Elimina il commento
    public boolean eliminaCommento() {
        int conferma = JOptionPane.showConfirmDialog(null, 
            "Sei sicuro di voler eliminare questo commento?", 
            "CONFERMA ELIMINAZIONE", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (conferma == JOptionPane.YES_OPTION) {
            CommentoDAO CDAO = new CommentoDAO(this.IDCommentoE);
            CDAO.deleteCommento();
            
            JOptionPane.showMessageDialog(null, 
                "Commento eliminato con successo", 
                "ELIMINAZIONE COMPLETATA", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
        }
        return false;
    }
    
    // Formatta la data per la visualizzazione
    public String getDataFormattata() {
        if (this.dataPubblicazione != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return this.dataPubblicazione.format(formatter);
        }
        return "Data non disponibile";
    }
    
    // Ottiene il tempo trascorso dalla pubblicazione
    public String getTempoTrascorso() {
        if (this.dataPubblicazione == null) {
            return "Data non disponibile";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long secondi = java.time.Duration.between(this.dataPubblicazione, now).getSeconds();
        
        if (secondi < 60) {
            return "Pochi secondi fa";
        } else if (secondi < 3600) {
            long minuti = secondi / 60;
            return minuti + (minuti == 1 ? " minuto fa" : " minuti fa");
        } else if (secondi < 86400) {
            long ore = secondi / 3600;
            return ore + (ore == 1 ? " ora fa" : " ore fa");
        } else if (secondi < 604800) {
            long giorni = secondi / 86400;
            return giorni + (giorni == 1 ? " giorno fa" : " giorni fa");
        } else if (secondi < 2592000) {
            long settimane = secondi / 604800;
            return settimane + (settimane == 1 ? " settimana fa" : " settimane fa");
        } else if (secondi < 31536000) {
            long mesi = secondi / 2592000;
            return mesi + (mesi == 1 ? " mese fa" : " mesi fa");
        } else {
            long anni = secondi / 31536000;
            return anni + (anni == 1 ? " anno fa" : " anni fa");
        }
    }
    
    // Verifica se il commento appartiene a un determinato autore
    public boolean appartienteA(long IDAutore) {
        return this.autoreCommento != null && this.autoreCommento.getID() == IDAutore;
    }
    
    // Visualizza il commento in formato completo
    public String visualizzaCommento() {
        String output = "\n--- COMMENTO ---\n";
        
        if (this.autoreCommento != null) {
            output += "Autore: " + this.autoreCommento.getNome() + " " + 
                     this.autoreCommento.getCognome() + "\n";
        } else {
            output += "Autore: Sconosciuto\n";
        }
        
        output += "Data: " + getDataFormattata() + " (" + getTempoTrascorso() + ")\n";
        output += "Testo: " + this.testo + "\n";
        output += "----------------\n";
        
        return output;
    }
    
    @Override
    public String toString() {
        String result = "";
        
        if (this.autoreCommento != null) {
            result += this.autoreCommento.getNome() + " " + this.autoreCommento.getCognome();
        } else {
            result += "Autore sconosciuto";
        }
        
        result += " - " + getTempoTrascorso() + ":\n";
        result += this.testo;
        
        return result;
    }
    
    // Equals basato su ID
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
        
        CommentoEntity other = (CommentoEntity) obj;
        return this.IDCommentoE == other.IDCommentoE;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(this.IDCommentoE);
    }
}