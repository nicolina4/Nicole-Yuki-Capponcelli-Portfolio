package entity;

import java.util.ArrayList;
import database.RaccoltaDAO;
import database.PoesiaDAO;
import javax.swing.JOptionPane;

public class RaccoltaEntity {
    private long IDRaccoltaE;
    private String titolo;
    private String descrizione;
    private ArrayList<PoesiaEntity> listaPoesie;
    private AutoreEntity AutoreRaccolta;

    // Costruttore di default
    public RaccoltaEntity() {
        this.listaPoesie = new ArrayList<>();
    }

    // Costruttore tramite DAO
    public RaccoltaEntity(RaccoltaDAO RDAO) {
        this();
        this.listaPoesie = new ArrayList<>();
        if (RDAO != null) {
            this.IDRaccoltaE = RDAO.getIDRaccoltaD();
            this.titolo = RDAO.getTitolo();
            this.descrizione = RDAO.getDescrizione();

            
            
            if (RDAO.getListaPoesie() != null) {
                for (PoesiaDAO PDAO : RDAO.getListaPoesie()) {
                    PoesiaEntity poesia = new PoesiaEntity(PDAO);
                    poesia.setRaccolta(this); // 🔥 IMPOSTA IL RIFERIMENTO ALLA RACCOLTA
                    this.listaPoesie.add(poesia);
                    
                   
                }
            }
            
            if (RDAO.getAutoreRaccolta() != null) {
                this.AutoreRaccolta = new AutoreEntity(RDAO.getAutoreRaccolta());
            }
            caricaPoesieDaDAO(RDAO);
        }
    }

    // Metodi get
    public long getIdRaccolta() { 
        return this.IDRaccoltaE; 
    }
    
    public String getTitolo() { 
        return this.titolo; 
    }
    
    public String getDescrizione() { 
        return this.descrizione; 
    }
    
    public ArrayList<PoesiaEntity> getPoesie() { 
        return this.listaPoesie; 
    }
    
    public AutoreEntity getAutoreRaccolta() {
        return this.AutoreRaccolta;
    }
    
    // Metodi set
    public void setIdRaccolta(long IDRaccolta) { 
        this.IDRaccoltaE = IDRaccolta; 
    }
    
    public void setTitolo(String titolo) { 
        this.titolo = titolo; 
    }
    
    public void setDescrizione(String descrizione) { 
        this.descrizione = descrizione; 
    }
    
    public void setPoesie(ArrayList<PoesiaEntity> poesie) { 
        this.listaPoesie = poesie; 
    }
    
    public void setAutoreRaccolta(AutoreEntity AENT) {
        this.AutoreRaccolta = AENT;
    }
    
    // Carica poesie dalla raccolta
    public void caricaPoesie(RaccoltaDAO RDAO) {
        this.listaPoesie = new ArrayList<PoesiaEntity>();
        
        if (RDAO != null && RDAO.getListaPoesie() != null) {
            for (PoesiaDAO PDAO : RDAO.getListaPoesie()) {
                PoesiaEntity PENT = new PoesiaEntity();
                
                PENT.setIdPoesia(PDAO.getIDPoesiaD());
                PENT.setTitolo(PDAO.getTitolo());
                PENT.setTesto(PDAO.getTesto());
                PENT.setTag(PDAO.getTag());
                PENT.setPubblica(PDAO.getPubblica());
                PENT.setDataPubblicazione(PDAO.getDataPubblicazione());
                PENT.setCuore(PDAO.getCuore());
                
                PENT.setRaccolta(this);
                PENT.setAutore(AutoreRaccolta);
                
                this.listaPoesie.add(PENT);
            }
        }
    }
    
    // Aggiungi una poesia alla raccolta
    public boolean aggiungiPoesia(PoesiaEntity poesia) {
        if (poesia == null) {
            JOptionPane.showMessageDialog(null, 
                "Impossibile aggiungere una poesia nulla", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Verifica che la poesia non sia già nella raccolta
        for (PoesiaEntity p : this.listaPoesie) {
            if (p.getIdPoesia() == poesia.getIdPoesia()) {
                JOptionPane.showMessageDialog(null, 
                    "La poesia è già presente nella raccolta", 
                    "AVVISO", 
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        // Aggiorna la raccolta della poesia
        poesia.setRaccolta(this);
        
        // Aggiorna nel database
        PoesiaDAO PDAO = new PoesiaDAO(poesia.getIdPoesia());
        PDAO.setRaccolta(new RaccoltaDAO(this.IDRaccoltaE));
        
        if (PDAO.updatePoesia()) {
            this.listaPoesie.add(poesia);
            JOptionPane.showMessageDialog(null, 
                "Poesia aggiunta alla raccolta con successo", 
                "OPERAZIONE COMPLETATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    // Rimuovi una poesia dalla raccolta
    public boolean rimuoviPoesia(long IDPoesia) {
        PoesiaEntity poesiaDaRimuovere = null;
        
        for (PoesiaEntity p : this.listaPoesie) {
            if (p.getIdPoesia() == IDPoesia) {
                poesiaDaRimuovere = p;
                break;
            }
        }
        
        if (poesiaDaRimuovere == null) {
            JOptionPane.showMessageDialog(null, 
                "Poesia non trovata nella raccolta", 
                "ERRORE", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        int conferma = JOptionPane.showConfirmDialog(null, 
            "Rimuovere la poesia \"" + poesiaDaRimuovere.getTitolo() + "\" dalla raccolta?", 
            "CONFERMA RIMOZIONE", 
            JOptionPane.YES_NO_OPTION);
        
        if (conferma == JOptionPane.YES_OPTION) {
            // Aggiorna nel database (imposta raccolta a null)
            PoesiaDAO PDAO = new PoesiaDAO(IDPoesia);
            PDAO.setRaccolta(null);
            
            if (PDAO.updatePoesia()) {
                this.listaPoesie.remove(poesiaDaRimuovere);
                JOptionPane.showMessageDialog(null, 
                    "Poesia rimossa dalla raccolta", 
                    "RIMOZIONE COMPLETATA", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }
    
    // Modifica titolo e descrizione della raccolta
    public boolean modificaRaccolta(String nuovoTitolo, String nuovaDescrizione) {
        RaccoltaDAO RDAO = new RaccoltaDAO(this.IDRaccoltaE);
        
        if (nuovoTitolo != null && !nuovoTitolo.isEmpty()) {
            RDAO.setTitolo(nuovoTitolo);
            this.titolo = nuovoTitolo;
        }
        
        if (nuovaDescrizione != null) {
            RDAO.setDescrizione(nuovaDescrizione);
            this.descrizione = nuovaDescrizione;
        }
        
        RDAO.updateRaccolta();
        
        JOptionPane.showMessageDialog(null, 
            "Raccolta modificata con successo", 
            "MODIFICA COMPLETATA", 
            JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
    
    // Elimina la raccolta
    public boolean eliminaRaccolta() {
        int conferma = JOptionPane.showConfirmDialog(null, 
            "Sei sicuro di voler eliminare la raccolta \"" + this.titolo + "\"?\nQuesta operazione è irreversibile.", 
            "CONFERMA ELIMINAZIONE", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (conferma == JOptionPane.YES_OPTION) {
            RaccoltaDAO RDAO = new RaccoltaDAO(this.IDRaccoltaE);
            RDAO.deleteRaccolta();
            
            JOptionPane.showMessageDialog(null, 
                "Raccolta eliminata con successo", 
                "ELIMINAZIONE COMPLETATA", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }
    
    // Ottiene numero di poesie nella raccolta
    public int getNumeroPoesie() {
        return this.listaPoesie != null ? this.listaPoesie.size() : 0;
    }
    
    // Ottiene la poesia più apprezzata della raccolta
    public PoesiaEntity getPoesiaPiuApprezzata() {
        if (this.listaPoesie == null || this.listaPoesie.isEmpty()) {
            return null;
        }
        
        PoesiaEntity best = null;
        int maxCuori = -1;
        
        for (PoesiaEntity p : this.listaPoesie) {
            if (p.getCuore() > maxCuori) {
                maxCuori = p.getCuore();
                best = p;
            }
        }
        
        return best;
    }
    
    // Ottiene poesie per tag specifico
    public ArrayList<PoesiaEntity> getPoesiePerTag(String tag) {
        ArrayList<PoesiaEntity> result = new ArrayList<>();
        
        if (tag != null && this.listaPoesie != null) {
            for (PoesiaEntity p : this.listaPoesie) {
                if (p.hasTag(tag)) {
                    result.add(p);
                }
            }
        }
        
        return result;
    }
    
    // Ottiene solo le poesie pubbliche ordinate per data decrescente
    public ArrayList<PoesiaEntity> getPoesiePubblicheOrdinate() {
        ArrayList<PoesiaEntity> pubbliche = new ArrayList<>();
        
        if (this.listaPoesie != null) {
            for (PoesiaEntity p : this.listaPoesie) {
                if (p.getPubblica()) {
                    pubbliche.add(p);
                }
            }
        }
        
        // Ordina per data decrescente
        pubbliche.sort((p1, p2) -> {
            if (p1.getDataPubblicazione() == null) return 1;
            if (p2.getDataPubblicazione() == null) return -1;
            return p2.getDataPubblicazione().compareTo(p1.getDataPubblicazione());
        });
        
        return pubbliche;
    }
    
    // Cerca poesie per titolo all'interno della raccolta
    public ArrayList<PoesiaEntity> cercaPoesiePerTitolo(String titoloCercato) {
        ArrayList<PoesiaEntity> risultati = new ArrayList<>();
        
        if (titoloCercato != null && this.listaPoesie != null) {
            for (PoesiaEntity p : this.listaPoesie) {
                if (p.getTitolo() != null && 
                    p.getTitolo().toLowerCase().contains(titoloCercato.toLowerCase())) {
                    risultati.add(p);
                }
            }
        }
        
        return risultati;
    }
    
    // Metodo separato per caricare le poesie
private void caricaPoesieDaDAO(RaccoltaDAO RDAO) {
    if (RDAO.getListaPoesie() != null && !RDAO.getListaPoesie().isEmpty()) {
        
        
        for (PoesiaDAO PDAO : RDAO.getListaPoesie()) {
            // Crea PoesiaEntity senza passare per il costruttore completo
            PoesiaEntity poesia = new PoesiaEntity();
            poesia.setIdPoesia(PDAO.getIDPoesiaD());
            poesia.setTitolo(PDAO.getTitolo());
            poesia.setTesto(PDAO.getTesto());
            poesia.setTag(PDAO.getTag());
            poesia.setPubblica(PDAO.getPubblica());
            poesia.setDataPubblicazione(PDAO.getDataPubblicazione());
            poesia.setCuore(PDAO.getCuore());
            poesia.setRaccolta(this); // Imposta il riferimento
            
            this.listaPoesie.add(poesia);
        }
    }
}
    
    // Ottiene statistiche della raccolta
    public String getStatisticheRaccolta() {
        int totPoesie = getNumeroPoesie();
        int totCuori = 0;
        int totCommenti = 0;
        int poesiePubbliche = 0;
        int poesiePrivate = 0;
        
        if (this.listaPoesie != null) {
            for (PoesiaEntity p : this.listaPoesie) {
                totCuori += p.getCuore();
                totCommenti += p.getNumeroCommenti();
                
                if (p.getPubblica()) {
                    poesiePubbliche++;
                } else {
                    poesiePrivate++;
                }
            }
        }
        
        String stats = "\n=== STATISTICHE RACCOLTA ===\n";
        stats += "Titolo: " + this.titolo + "\n";
        stats += "Numero poesie: " + totPoesie + "\n";
        stats += "Poesie pubbliche: " + poesiePubbliche + "\n";
        stats += "Poesie private: " + poesiePrivate + "\n";
        stats += "Totale cuori: " + totCuori + "\n";
        stats += "Totale commenti: " + totCommenti + "\n";
        
        PoesiaEntity piuApprezzata = getPoesiaPiuApprezzata();
        if (piuApprezzata != null) {
            stats += "Poesia più apprezzata: \"" + piuApprezzata.getTitolo() + 
                    "\" (" + piuApprezzata.getCuore() + " cuori)\n";
        }
        
        return stats;
    }
    
    // Visualizza raccolta con tutte le poesie
    public void visualizzaRaccolta() {
        String output = "\n========================================\n";
        output += "RACCOLTA: " + this.titolo + "\n";
        output += "========================================\n";
        output += "Descrizione: " + (this.descrizione != null ? this.descrizione : "Nessuna descrizione") + "\n";
        output += "Autore: " + (this.AutoreRaccolta != null ? 
                    this.AutoreRaccolta.getNome() + " " + this.AutoreRaccolta.getCognome() : 
                    "Sconosciuto") + "\n";
        output += "Numero poesie: " + getNumeroPoesie() + "\n";
        output += "========================================\n\n";
        
        if (this.listaPoesie != null && !this.listaPoesie.isEmpty()) {
            int count = 1;
            for (PoesiaEntity p : this.listaPoesie) {
                output += count + ". " + p.getTitolo() + 
                         " (" + p.getCuore() + " cuori, " + 
                         p.getNumeroCommenti() + " commenti)\n";
                count++;
            }
        } else {
            output += "Nessuna poesia presente nella raccolta.\n";
        }
        
        JOptionPane.showMessageDialog(null, 
            output, 
            "VISUALIZZA RACCOLTA", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public String toString() {
        String result = "\n=== RACCOLTA ===\n";
        result += "Titolo: " + this.titolo + "\n";
        result += "Descrizione: " + (this.descrizione != null ? this.descrizione : "Nessuna descrizione") + "\n";
        result += "Autore: " + (this.AutoreRaccolta != null ? 
                   this.AutoreRaccolta.getNome() + " " + this.AutoreRaccolta.getCognome() : 
                   "Sconosciuto") + "\n";
        result += "Numero poesie: " + getNumeroPoesie() + "\n";
        
        return result;
    }
}