package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import database.PoesiaDAO;
import database.CommentoDAO;
import javax.swing.JOptionPane;

public class PoesiaEntity {
    private long IDPoesiaE;
    private String titolo;
    private String testo;
    private ArrayList<String> tag;
    private boolean pubblica;
    private LocalDateTime dataPubblicazione;
    private AutoreEntity autore;
    private RaccoltaEntity raccolta;
    private int cuore;
    private ArrayList<CommentoEntity> commenti;

    // Costruttore di default
    public PoesiaEntity() {
        this.tag = new ArrayList<>();
        this.pubblica = false;
        this.commenti = new ArrayList<>();
    }

    // Costruttore tramite DAO
    public PoesiaEntity(PoesiaDAO PDAO) {
        this();
        if (PDAO != null) {
            this.IDPoesiaE = PDAO.getIDPoesiaD();
            this.titolo = PDAO.getTitolo();
            this.testo = PDAO.getTesto();
            this.tag = PDAO.getTag() != null ? new ArrayList<>(PDAO.getTag()) : new ArrayList<>();
            this.pubblica = PDAO.getPubblica();
            this.dataPubblicazione = PDAO.getDataPubblicazione();
            this.cuore = PDAO.getCuore();

            if (PDAO.getAutore() != null) {
                this.autore = new AutoreEntity(PDAO.getAutore());
            }

          

            if (PDAO.getCommenti() != null) {
                for (CommentoDAO CDAO : PDAO.getCommenti()) {
                    this.commenti.add(new CommentoEntity(CDAO));
                }
            }
        }
    }

    // Metodi get
    public long getIdPoesia() { 
        return this.IDPoesiaE; 
    }
    
    public String getTitolo() { 
        return this.titolo; 
    }
    
    public String getTesto() { 
        return this.testo; 
    }
    
    public ArrayList<String> getTag() { 
        return this.tag; 
    }
    
    public boolean getPubblica() { 
        return this.pubblica; 
    }
    
    public LocalDateTime getDataPubblicazione() { 
        return this.dataPubblicazione; 
    }
    
    public AutoreEntity getAutore() { 
        return this.autore; 
    }
    
    public RaccoltaEntity getRaccolta() { 
        return this.raccolta; 
    }
    
    public int getCuore() { 
        return this.cuore; 
    }
    
    public ArrayList<CommentoEntity> getCommenti() { 
        return this.commenti; 
    }
    
    // Metodi set
    public void setIdPoesia(long IDPoesia) { 
        this.IDPoesiaE = IDPoesia; 
    }
    
    public void setTitolo(String titolo) { 
        this.titolo = titolo; 
    }
    
    public void setTesto(String testo) {
        this.testo = testo; 
    }
    
    public void setTag(ArrayList<String> tag) { 
        this.tag = tag; 
    }
    
    public void setPubblica(boolean pubblica) { 
        this.pubblica = pubblica; 
    }
    
    public void setDataPubblicazione(LocalDateTime dataPubblicazione) { 
        this.dataPubblicazione = dataPubblicazione; 
    }
    
    public void setAutore(AutoreEntity autore) { 
        this.autore = autore; 
    }
    
    public void setRaccolta(RaccoltaEntity raccolta) { 
        this.raccolta = raccolta; 
    }
    
    public void setCuore(int cuore) { 
        this.cuore = cuore; 
    }
    
    public void setCommenti(ArrayList<CommentoEntity> commenti) { 
        this.commenti = commenti; 
    }
    
    // Carica raccolta associata
    public void caricaRaccolta(PoesiaDAO PDAO) {
        if (PDAO != null && PDAO.getRaccolta() != null) {
            RaccoltaEntity RENT = new RaccoltaEntity();
            
            RENT.setIdRaccolta(PDAO.getRaccolta().getIDRaccoltaD());
            RENT.setTitolo(PDAO.getRaccolta().getTitolo());
            RENT.setDescrizione(PDAO.getRaccolta().getDescrizione());
            
            this.raccolta = RENT;
        }
    }
    
    // Carica commenti associati
    public void caricaCommenti(PoesiaDAO PDAO) {
        this.commenti = new ArrayList<CommentoEntity>();
        
        if (PDAO != null && PDAO.getCommenti() != null) {
            for (CommentoDAO CDAO : PDAO.getCommenti()) {
                CommentoEntity CENT = new CommentoEntity();
                
                CENT.setIdCommento(CDAO.getIDCommentoD());
                CENT.setTesto(CDAO.getTesto());
                CENT.setDataPubblicazione(CDAO.getDataPubblicazione());
                
                if (CDAO.getAutoreCommento() != null) {
                    CENT.setAutoreCommento(new AutoreEntity(CDAO.getAutoreCommento()));
                }
                
                this.commenti.add(CENT);
            }
        }
    }
    
    // Aggiunge un cuore alla poesia
    public boolean aggiungiCuore(long IDUtente) {
        PoesiaDAO PDAO = new PoesiaDAO(this.IDPoesiaE);
        PDAO.addCuore(IDUtente);
        this.cuore++;
        
        return true;
    }
    
    // Aggiunge un commento alla poesia
    public boolean aggiungiCommento(CommentoEntity commentoEntity) {
        if (commentoEntity == null || commentoEntity.getTesto() == null || commentoEntity.getTesto().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Il commento non può essere vuoto", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        PoesiaDAO PDAO = new PoesiaDAO(this.IDPoesiaE);
        CommentoDAO CDAO = new CommentoDAO();
        
        CDAO.setTesto(commentoEntity.getTesto());
        CDAO.setDataPubblicazione(LocalDateTime.now());
        CDAO.setAutoreCommento(new database.AutoreDAO(commentoEntity.getAutoreCommento().getID()));
        
        if (PDAO.addCommento(CDAO)) {
            this.commenti.add(commentoEntity);
            return true;
        }
        return false;
    }
    
    // Ottiene gli ultimi tre commenti
    public ArrayList<CommentoEntity> getUltimiTreCommenti() {
        PoesiaDAO PDAO = new PoesiaDAO(this.IDPoesiaE);
        ArrayList<CommentoDAO> ultimiDAO = PDAO.getUltimiCommenti();
        ArrayList<CommentoEntity> ultimi = new ArrayList<>();
        
        for (CommentoDAO CDAO : ultimiDAO) {
            ultimi.add(new CommentoEntity(CDAO));
        }
        
        return ultimi;
    }
    
    // Modifica la poesia
    public boolean modificaPoesia(String nuovoTitolo, String nuovoTesto, 
                                 ArrayList<String> nuoviTag, boolean nuovaPubblica) {
        
        // Validazione: massimo 500 caratteri
        if (nuovoTesto != null && nuovoTesto.length() > 500) {
            JOptionPane.showMessageDialog(null, 
                "Il testo della poesia supera i 500 caratteri consentiti", 
                "ERRORE MODIFICA", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        PoesiaDAO PDAO = new PoesiaDAO(this.IDPoesiaE);
        
        if (nuovoTitolo != null && !nuovoTitolo.isEmpty()) {
            PDAO.setTitolo(nuovoTitolo);
            this.titolo = nuovoTitolo;
        }
        
        if (nuovoTesto != null && !nuovoTesto.isEmpty()) {
            PDAO.setTesto(nuovoTesto);
            this.testo = nuovoTesto;
        }
        
        if (nuoviTag != null) {
            PDAO.setTag(nuoviTag);
            this.tag = nuoviTag;
        }
        
        PDAO.setPubblica(nuovaPubblica);
        this.pubblica = nuovaPubblica;
        
        if (PDAO.updatePoesia()) {
            JOptionPane.showMessageDialog(null, 
                "Poesia modificata con successo", 
                "MODIFICA COMPLETATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, 
                "Errore durante la modifica della poesia", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Elimina la poesia
    public boolean eliminaPoesia() {
        int conferma = JOptionPane.showConfirmDialog(null, 
            "Sei sicuro di voler eliminare la poesia \"" + this.titolo + "\"?\nQuesta operazione è irreversibile.", 
            "CONFERMA ELIMINAZIONE", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (conferma == JOptionPane.YES_OPTION) {
            PoesiaDAO PDAO = new PoesiaDAO(this.IDPoesiaE);
            
            if (PDAO.deletePoesia()) {
                JOptionPane.showMessageDialog(null, 
                    "Poesia eliminata con successo", 
                    "ELIMINAZIONE COMPLETATA", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Errore durante l'eliminazione della poesia", 
                    "ERRORE", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    // Cambia stato pubblico/privato
    public boolean cambiaVisibilita() {
        this.pubblica = !this.pubblica;
        
        PoesiaDAO PDAO = new PoesiaDAO(this.IDPoesiaE);
        PDAO.setPubblica(this.pubblica);
        
        if (PDAO.updatePoesia()) {
            String stato = this.pubblica ? "pubblica" : "privata";
            JOptionPane.showMessageDialog(null, 
                "Poesia ora è " + stato, 
                "VISIBILITÀ MODIFICATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    // Verifica se la poesia ha un tag specifico
    public boolean hasTag(String tagCercato) {
        if (this.tag != null && tagCercato != null) {
            for (String t : this.tag) {
                if (t.equalsIgnoreCase(tagCercato)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Ottiene numero di commenti
    public int getNumeroCommenti() {
        return this.commenti != null ? this.commenti.size() : 0;
    }
    
    // Ottiene numero totale di interazioni (cuori + commenti)
    public int getNumeroInterazioni() {
        return this.cuore + getNumeroCommenti();
    }
    
    @Override
    public String toString() {
        String result = "\n=== POESIA ===\n";
        result += "Titolo: " + this.titolo + "\n";
        result += "Autore: " + (this.autore != null ? this.autore.getNome() + " " + this.autore.getCognome() : "Sconosciuto") + "\n";
        result += "Testo:\n" + this.testo + "\n";
        result += "\nTag: " + (this.tag != null && !this.tag.isEmpty() ? String.join(", ", this.tag) : "Nessun tag") + "\n";
        result += "Visibilità: " + (this.pubblica ? "Pubblica" : "Privata") + "\n";
        result += "Data pubblicazione: " + this.dataPubblicazione + "\n";
        result += "Cuori: " + this.cuore + "\n";
        result += "Commenti: " + getNumeroCommenti() + "\n";
        
        if (this.raccolta != null) {
            result += "Raccolta: " + this.raccolta.getTitolo() + "\n";
        }
        
        return result;
    }
}
