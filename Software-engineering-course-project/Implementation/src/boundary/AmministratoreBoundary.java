package boundary;

import DTO.AutoreAttivitaDTO;
import DTO.ReportIntervalloDTO;
import control.Controller;
import entity.*;
import database.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.sql.Timestamp;


/**
 AmministratoreBoundary - Interfaccia per gli amministratori
 Gestisce report, utenti, statistiche piattaforma
 */
public class AmministratoreBoundary extends UtenteBoundary {
    private static final long serialVersionUID = 1L;
    
    private AmministratoreEntity amministratoreCorrente;
    
    // Panel delle sezioni
    private JPanel homePanel;
    private JPanel utentiPanel;
    private JPanel autoriPanel;
    private JPanel reportPanel;
    private JPanel statistichePanel;
    private JTable utentiTable;
    private JTable autoriTable;
    
    // Menu laterale
    private JPanel menuPanel;
    
    private JTextArea outputArea;  // Serve per Report
    
    //Costruttore
    public AmministratoreBoundary() {
        super("PoesieOnline - Dashboard Amministratore");
        this.amministratoreCorrente = controller.getAmministratoreCorrente();
        
        // Inizializza le sezioni specifiche dell'amministratore
        inizializzaSezioni();
        aggiornaContenuto();
        
        setVisible(true);
    }
    
    
     // Inizializza le sezioni specifiche dell'amministratore
    
    @Override
    protected void inizializzaSezioni() {
        // Crea menu laterale
        creaMenuLaterale();
        
        // Crea le sezioni
        creaHomePanel();
        creaUtentiPanel();
        creaAutoriPanel();
        creaReportPanel();
        creaStatistichePanel();
        
        // Aggiungi sezioni al content panel
        contentPanel.add(homePanel, "HOME");
        contentPanel.add(utentiPanel, "UTENTI");
        contentPanel.add(autoriPanel, "AUTORI");
        contentPanel.add(reportPanel, "REPORT");
        contentPanel.add(statistichePanel, "STATISTICHE");
        
        // Mostra home di default
        mostraSezione("HOME");
    }
    
    
    // Crea il menu laterale
     
    private void creaMenuLaterale() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(70, 70, 90));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Pulsanti menu
        JButton homeBtn = creaBottoneMenu("🏠 Dashboard", "HOME");
        JButton utentiBtn = creaBottoneMenu("👥 Gestione Utenti", "UTENTI");
        JButton autoriBtn = creaBottoneMenu("✍ Gestione Autori", "AUTORI");
        JButton reportBtn = creaBottoneMenu("📊 Genera Report", "REPORT");
        JButton statsBtn = creaBottoneMenu("📈 Statistiche", "STATISTICHE");
        
        // Aggiungi al menu
        menuPanel.add(homeBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(utentiBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(autoriBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(reportBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(statsBtn);
        menuPanel.add(Box.createVerticalGlue());
        
        // Aggiungi menu al main panel
        mainPanel.add(menuPanel, BorderLayout.WEST);
    }
    
    /**
     * Crea un bottone per il menu laterale
     */
    private JButton creaBottoneMenu(String text, String sezione) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(new Color(90, 90, 110));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> mostraSezione(sezione));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(110, 110, 130));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 90, 110));
            }
        });
        
        return button;
    }
    
    /**
     * Crea il panel home
     */
    private void creaHomePanel() {
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Dashboard Amministratore");
        
        // Statistiche rapide piattaforma
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        
        // Placeholder per statistiche reali
        aggiungiStatisticaRapida(statsPanel, "Utenti Totali", "0");
        aggiungiStatisticaRapida(statsPanel, "Autori Registrati", "0");
        aggiungiStatisticaRapida(statsPanel, "Poesie Pubblicate", "0");
        aggiungiStatisticaRapida(statsPanel, "Raccolte Create", "0");
        aggiungiStatisticaRapida(statsPanel, "Commenti Totali", "0");
        aggiungiStatisticaRapida(statsPanel, "Cuori Totali", "0");
        
        content.add(statsPanel, BorderLayout.CENTER);
        
        // Attività recenti
        JPanel attivitaPanel = new JPanel(new BorderLayout());
        attivitaPanel.setBackground(Color.WHITE);
        attivitaPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel attivitaLabel = creaLabelTitolo("Attività Recenti");
        attivitaPanel.add(attivitaLabel, BorderLayout.NORTH);
        
        JTextArea attivitaArea = creaAreaTesto(8, 50);
        attivitaArea.setText("Nessuna attività recente da visualizzare...");
        attivitaArea.setEditable(false);
        attivitaPanel.add(new JScrollPane(attivitaArea), BorderLayout.CENTER);
        
        content.add(attivitaPanel, BorderLayout.SOUTH);
        
        homePanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Aggiunge una statistica rapida al panel
     */
    private void aggiungiStatisticaRapida(JPanel panel, String titolo, String valore) {
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.Y_AXIS));
        statPanel.setBackground(new Color(240, 245, 255));
        statPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 255), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titoloLabel = new JLabel(titolo);
        titoloLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titoloLabel.setForeground(TEXT_COLOR);
        titoloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valoreLabel = new JLabel(valore);
        valoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valoreLabel.setForeground(PRIMARY_COLOR);
        valoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        statPanel.add(titoloLabel);
        statPanel.add(Box.createVerticalStrut(5));
        statPanel.add(valoreLabel);
        
        panel.add(statPanel);
    }
    
    /**
     * Crea il panel gestione utenti
     */
    private void creaUtentiPanel() {
        utentiPanel = new JPanel(new BorderLayout());
        utentiPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Gestione Utenti");
        
        // Pulsanti azioni
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton caricaBtn = creaBottone("🔄 Carica Utenti", SECONDARY_COLOR);
        JButton esportaBtn = creaBottone("📤 Esporta Dati", new Color(150, 100, 200));
        
        caricaBtn.addActionListener(e -> caricaListaUtenti());
        esportaBtn.addActionListener(e -> mostraSuccesso("Funzionalità esporta dati in sviluppo"));
        
        topPanel.add(caricaBtn);
        topPanel.add(esportaBtn);
        content.add(topPanel, BorderLayout.NORTH);
        
        // Tabella utenti
        String[] colonne = {"ID", "Email", "Tipo", "Data Registrazione"};
        Object[][] dati = {}; // Dati vuoti iniziali
        
        utentiTable = new JTable(dati, colonne);
        utentiTable.setFillsViewportHeight(true);
        utentiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(utentiTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Panel azioni per utente selezionato
        JPanel azioniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        azioniPanel.setBackground(Color.WHITE);
        azioniPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton eliminaBtn = creaBottone("Elimina Utente", DANGER_COLOR);
        JButton dettagliBtn = creaBottone("Dettagli", SECONDARY_COLOR);
        
        eliminaBtn.addActionListener(e -> {
            int selectedRow = utentiTable.getSelectedRow();
            if (selectedRow != -1) {
                long idUtente = (Long) utentiTable.getValueAt(selectedRow, 0);
                eliminaUtente(idUtente);
            } else {
                mostraErrore("Seleziona un utente dalla tabella");
            }
        });
        
        dettagliBtn.addActionListener(e -> {
            int selectedRow = utentiTable.getSelectedRow();
            if (selectedRow != -1) {
                long idUtente = (Long) utentiTable.getValueAt(selectedRow, 0);
                mostraDettagliUtente(idUtente);
            } else {
                mostraErrore("Seleziona un utente dalla tabella");
            }
        });
        
        azioniPanel.add(dettagliBtn);
        azioniPanel.add(eliminaBtn);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(azioniPanel, BorderLayout.SOUTH);
        
        content.add(tablePanel, BorderLayout.CENTER);
        
        utentiPanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Carica lista utenti nel sistema
     */
    private void caricaListaUtenti() {
        // Placeholder - dovrebbe caricare dati reali dal controller
        Object[][] dati = {
            {1L, "admin@poesieonline.it", "Amministratore", "2024-01-15"},
            {2L, "mario.rossi@email.com", "Autore", "2024-02-20"},
            {3L, "laura.bianchi@email.com", "Autore", "2024-03-10"},
            {4L, "giuseppe.verdi@email.com", "Autore", "2024-03-15"}
        };
        
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(dati, 
            new String[]{"ID", "Email", "Tipo", "Data Registrazione"}
        );
        utentiTable.setModel(model);
        
        mostraSuccesso("Caricati " + dati.length + " utenti");
    }
    
    /**
     * Elimina utente
     */
    private void eliminaUtente(long idUtente) {
        if (mostraConferma("Sei sicuro di voler eliminare l'utente con ID " + idUtente + "?\nQuesta operazione è irreversibile.")) {
            if (controller.eliminaUtente(idUtente)) {
                mostraSuccesso("Utente eliminato con successo");
                caricaListaUtenti(); // Ricarica lista
            } else {
                mostraErrore("Errore durante l'eliminazione dell'utente");
            }
        }
    }
    
    /**
     * Mostra dettagli utente
     */
    private void mostraDettagliUtente(long idUtente) {
        JDialog dialog = new JDialog(this, "Dettagli Utente", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JTextArea dettagliArea = creaAreaTesto(12, 35);
        dettagliArea.setEditable(false);
        dettagliArea.setText("Dettagli utente ID: " + idUtente + "\n\n"
            + "Email: utente@esempio.com\n"
            + "Tipo: Autore\n"
            + "Data Registrazione: 2024-01-01\n"
            + "Poesie Pubblicate: 5\n"
            + "Raccolte Create: 2\n"
            + "Ultimo Accesso: 2024-03-20");
        
        JButton chiudiBtn = creaBottone("Chiudi", new Color(150, 150, 150));
        chiudiBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(new JScrollPane(dettagliArea), BorderLayout.CENTER);
        panel.add(chiudiBtn, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Crea il panel gestione autori
     */
    private void creaAutoriPanel() {
        autoriPanel = new JPanel(new BorderLayout());
        autoriPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Gestione Autori");
        
        // Pulsanti azioni
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton caricaBtn = creaBottone("🔄 Carica Autori", SECONDARY_COLOR);
        JButton statsBtn = creaBottone("📊 Statistiche Autori", PRIMARY_COLOR);
        
        caricaBtn.addActionListener(e -> caricaListaAutori());
        statsBtn.addActionListener(e -> generaReportAutoriAttivi());
        
        topPanel.add(caricaBtn);
        topPanel.add(statsBtn);
        content.add(topPanel, BorderLayout.NORTH);
        
        // Tabella autori
        String[] colonne = {"ID", "Nome", "Cognome", "Email", "Poesie", "Registrazione"};
        Object[][] dati = {}; // Dati vuoti iniziali
        
        autoriTable = new JTable(dati, colonne);
        autoriTable.setFillsViewportHeight(true);
        autoriTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(autoriTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Panel azioni per autore selezionato
        JPanel azioniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        azioniPanel.setBackground(Color.WHITE);
        azioniPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton bloccaBtn = creaBottone("Blocca Autore", DANGER_COLOR);
        JButton dettagliBtn = creaBottone("Dettagli Autore", SECONDARY_COLOR);
        JButton poesieBtn = creaBottone("Vedi Poesie", new Color(100, 180, 100));
        
        dettagliBtn.addActionListener(e -> {
            int selectedRow = autoriTable.getSelectedRow();
            if (selectedRow != -1) {
                long idAutore = (Long) autoriTable.getValueAt(selectedRow, 0);
                mostraDettagliAutore(idAutore);
            } else {
                mostraErrore("Seleziona un autore dalla tabella");
            }
        });
        
        poesieBtn.addActionListener(e -> {
            int selectedRow = autoriTable.getSelectedRow();
            if (selectedRow != -1) {
                String nomeAutore = autoriTable.getValueAt(selectedRow, 1) + " " + autoriTable.getValueAt(selectedRow, 2);
                mostraPoesieAutore(nomeAutore);
            } else {
                mostraErrore("Seleziona un autore dalla tabella");
            }
        });
        
        bloccaBtn.addActionListener(e -> mostraErrore("Funzionalità blocco autore in sviluppo"));
        
        azioniPanel.add(dettagliBtn);
        azioniPanel.add(poesieBtn);
        azioniPanel.add(bloccaBtn);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(azioniPanel, BorderLayout.SOUTH);
        
        content.add(tablePanel, BorderLayout.CENTER);
        
        autoriPanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Carica lista autori
     */
    private void caricaListaAutori() {
        // Placeholder - dovrebbe caricare dati reali dal controller
        Object[][] dati = {
            {2L, "Mario", "Rossi", "mario.rossi@email.com", 8, "2024-02-20"},
            {3L, "Laura", "Bianchi", "laura.bianchi@email.com", 12, "2024-03-10"},
            {4L, "Giuseppe", "Verdi", "giuseppe.verdi@email.com", 5, "2024-03-15"},
            {5L, "Anna", "Neri", "anna.neri@email.com", 3, "2024-03-18"}
        };
        
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(dati, 
                 new String[]{"ID", "Nome", "Cognome", "Email", "Poesie", "Registrazione"}
        );
        autoriTable.setModel(model);
    
        mostraSuccesso("Caricati " + dati.length + " autori");
    }
    
    /**
     * Mostra dettagli autore
     */
    private void mostraDettagliAutore(long idAutore) {
        JDialog dialog = new JDialog(this, "Dettagli Autore", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JTextArea dettagliArea = creaAreaTesto(15, 40);
        dettagliArea.setEditable(false);
        dettagliArea.setText("DETTAGLI AUTORE\n\n"
            + "ID: " + idAutore + "\n"
            + "Nome: Mario Rossi\n"
            + "Email: mario.rossi@email.com\n"
            + "Data Registrazione: 2024-02-20\n\n"
            + "STATISTICHE:\n"
            + "• Poesie Pubblicate: 8\n"
            + "• Raccolte Create: 2\n"
            + "• Cuori Totali Ricevuti: 45\n"
            + "• Commenti Totali Ricevuti: 12\n"
            + "• Poesia Più Apprezzata: 'Il Tramonto' (15 cuori)\n\n"
            + "ULTIME ATTIVITÀ:\n"
            + "• Pubblicata 'La Notte Stellata' - 2 giorni fa\n"
            + "• Ricevuto commento su 'Il Mare' - 5 giorni fa");
        
        JButton chiudiBtn = creaBottone("Chiudi", new Color(150, 150, 150));
        chiudiBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(new JScrollPane(dettagliArea), BorderLayout.CENTER);
        panel.add(chiudiBtn, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Mostra poesie autore
     */
    private void mostraPoesieAutore(String nomeAutore) {
        JDialog dialog = new JDialog(this, "Poesie di " + nomeAutore, true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JTextArea poesieArea = creaAreaTesto(20, 50);
        poesieArea.setEditable(false);
        poesieArea.setText("POESIE DI " + nomeAutore.toUpperCase() + "\n\n"
            + "1. Il Tramonto (15 cuori, 3 commenti)\n"
            + "   Pubblicata: 2024-03-18\n"
            + "   Tag: natura, sera\n\n"
            + "2. La Notte Stellata (12 cuori, 2 commenti)\n"
            + "   Pubblicata: 2024-03-15\n"
            + "   Tag: notte, stelle\n\n"
            + "3. Il Mare Infinito (8 cuori, 4 commenti)\n"
            + "   Pubblicata: 2024-03-10\n"
            + "   Tag: mare, infinito\n\n"
            + "4. Il Vento tra gli Alberi (6 cuori, 1 commento)\n"
            + "   Pubblicata: 2024-03-05\n"
            + "   Tag: natura, vento\n\n"
            + "5. Luci della Città (4 cuori, 0 commenti)\n"
            + "   Pubblicata: 2024-03-01\n"
            + "   Tag: città, notte");
        
        JButton chiudiBtn = creaBottone("Chiudi", new Color(150, 150, 150));
        chiudiBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(new JScrollPane(poesieArea), BorderLayout.CENTER);
        panel.add(chiudiBtn, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Crea il panel report
     */
    private void creaReportPanel() {
        reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(BACKGROUND_COLOR);
        JPanel content = creaPannelloConTitolo("Genera Report");
        // Tipi di report disponibili
        JPanel reportTypesPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        reportTypesPanel.setBackground(Color.WHITE);
        reportTypesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Pulsanti report
        JButton reportPoesieBtn = creaBottoneReport("📈 Poesie per Intervallo", "Genera report sulle poesie pubblicate in un intervallo di tempo");
        JButton reportAutoriBtn = creaBottoneReport("👥 Autori Più Attivi", "Visualizza gli autori con più poesie pubblicate");
        JButton reportTagBtn = creaBottoneReport("🏷️ Tag Più Utilizzati", "Mostra i tag più popolari tra le poesie");
        JButton reportInterazioniBtn = creaBottoneReport("💝 Poesie Più Interagite", "Report sulle poesie con più cuori e commenti");
        // Assegnazione degli ActionListener per eseguire le chiamate al Controller
        reportPoesieBtn.addActionListener(e -> mostraDialogReportPoesie());
        reportAutoriBtn.addActionListener(e -> generaReportAutoriAttivi());
        reportTagBtn.addActionListener(e -> generaReportTagPiuUsati());
        reportInterazioniBtn.addActionListener(e -> generaReportPoesiePiuInterazioni());
        reportTypesPanel.add(reportPoesieBtn);
        reportTypesPanel.add(reportAutoriBtn);
        reportTypesPanel.add(reportTagBtn);
        reportTypesPanel.add(reportInterazioniBtn);
        content.add(reportTypesPanel, BorderLayout.NORTH); // Posiziona i pulsanti in alto
        // Area output report (Dove appare il risultato)
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(Color.WHITE);
        outputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel outputLabel = creaLabelTitolo("Output Report");
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        // ✅ Inizializzazione e configurazione dell'area di output
        outputArea = creaAreaTesto(10, 50); 
        outputArea.setText("Seleziona un tipo di report per visualizzare i dati...");
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Usa font monospaced per tabelle
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        content.add(outputPanel, BorderLayout.CENTER); // Posiziona l'area di output al centro
        reportPanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Crea bottone per tipo di report
     */
    private JButton creaBottoneReport(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(100, 150, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(120, 170, 220));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }
        });
        
        return button;
    }
    
    private void mostraDialogReportPoesie() {
        JDialog dialog = new JDialog(this, "Report Poesie per Intervallo", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
     
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
     
        JLabel titoloLabel = new JLabel("Seleziona Intervallo Date");
        titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
     
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
     
        // Campi data
        formPanel.add(creaLabel("Data Inizio (YYYY-MM-DD):"));
        JTextField dataInizioField = creaCampoTesto(15);
        dataInizioField.setText("2024-01-01");
        formPanel.add(dataInizioField);
     
        formPanel.add(creaLabel("Data Fine (YYYY-MM-DD):"));
        JTextField dataFineField = creaCampoTesto(15);
        dataFineField.setText(LocalDate.now().toString()); // Data corrente come default
        formPanel.add(dataFineField);
     
        formPanel.add(creaLabel(""));
        formPanel.add(creaLabel(""));
     
        JButton generaBtn = creaBottone("Genera Report", PRIMARY_COLOR);
        JButton annullaBtn = creaBottone("Annulla", new Color(150, 150, 150));
     
        // Formatter per parsing sicuro
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     
        generaBtn.addActionListener(e -> {
            try {
                // 1. Validazione e Parsing dell'Input Data
                LocalDate dataInizioLD = LocalDate.parse(dataInizioField.getText().trim(), formatter);
                LocalDate dataFineLD = LocalDate.parse(dataFineField.getText().trim(), formatter);
     
                Timestamp dataInizio = Timestamp.valueOf(dataInizioLD.atStartOfDay());
                Timestamp dataFine = Timestamp.valueOf(dataFineLD.atStartOfDay());
     
                if (dataFine.before(dataInizio)) {
                    mostraErrore("La data fine non può essere precedente alla data inizio.");
                    return;
                }
     
                // 2. Chiamata al Controllo e ricezione del DTO
                ReportIntervalloDTO reportDTO = controller.generaReportPoesieIntervallo(dataInizio, dataFine);
                dialog.dispose();
     
                // 3. Presentazione del DTO nell'Area di Output (outputArea)
                String output = "=== REPORT POESIE PUBBLICATE PER INTERVALLO ===\n\n";
                output += String.format("Periodo: %s a %s\n", dataInizioLD.toString(), dataFineLD.toString());
                output += String.format("Totale Poesie Pubblicate: %d\n", reportDTO.getTotalePoesie());
                output += "=================================================\n";
                // ✅ Aggiorna l'area di output del pannello principale
                outputArea.setText(output);
                outputArea.setCaretPosition(0); // Scorre all'inizio
                mostraSuccesso("Report generato e visualizzato nell'applicazione.");
     
            } catch (DateTimeParseException ex) {
                mostraErrore("Formato data non valido. Usa il formato YYYY-MM-DD.");
            } catch (Exception ex) {
                mostraErrore("Errore durante la generazione del report: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
     
        annullaBtn.addActionListener(e -> dialog.dispose());
     
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(annullaBtn);
        buttonPanel.add(generaBtn);
     
        panel.add(titoloLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
     
        dialog.add(panel);
        dialog.setVisible(true);
    }

    
    /**
     * Genera report autori attivi
     */
    private void generaReportAutoriAttivi() {
        try {
            // 1. Chiamata al Controllo per ottenere la lista di DTO
            ArrayList<AutoreAttivitaDTO> report = controller.generaReportAutoriAttivi();
            // 2. Formattazione dell'output
            StringBuilder sb = new StringBuilder("=== REPORT AUTORI PIÙ ATTIVI ===\n\n");
            if (report.isEmpty()) {
                sb.append("Nessun autore trovato o dati insufficienti per la classifica.");
            } else {
                sb.append(String.format("%-5s %-30s %-10s\n", "RANK", "AUTORE", "POESIE PUBB."));
                sb.append("--------------------------------------------------\n");
                int rank = 1;
                for (AutoreAttivitaDTO autore : report) {
                    sb.append(String.format("%-5d %-30s %-10d\n", 
                        rank++, 
                        autore.getNomeCompleto(), 
                        autore.getNumPoesie()
                    ));
                }
            }
            // 3. Visualizzazione nell'area di output
            outputArea.setText(sb.toString());
            outputArea.setCaretPosition(0); // Scorre all'inizio
            mostraSuccesso("Report autori attivi generato con successo.");
     
        } catch (Exception e) {
            mostraErrore("Errore durante la generazione del report autori attivi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Genera report tag più usati
     */
    private void generaReportTagPiuUsati() {
        try {
            // 1. Chiamata al Controllo per ottenere la lista di stringhe (Tag + Conteggio)
            ArrayList<String> report = controller.generaReportTagPiuUsati();
            // 2. Formattazione dell'output
            StringBuilder sb = new StringBuilder("=== REPORT TAG PIÙ UTILIZZATI ===\n\n");
            if (report.isEmpty()) {
                sb.append("Nessun tag trovato.");
            } else {
                sb.append(String.format("%-5s %-35s\n", "RANK", "TAG E UTILIZZI"));
                sb.append("------------------------------------------\n");
                int rank = 1;
                for (String tagDettaglio : report) {
                    // Ipotizziamo che la stringa restituita dal DAO sia già formattata come "Tag (X utilizzi)"
                    sb.append(String.format("%-5d %-35s\n", 
                        rank++, 
                        tagDettaglio
                    ));
                }
            }
            // 3. Visualizzazione nell'area di output
            outputArea.setText(sb.toString());
            outputArea.setCaretPosition(0); // Scorre all'inizio
            mostraSuccesso("Report tag più utilizzati generato con successo.");
     
        } catch (Exception e) {
            mostraErrore("Errore durante la generazione del report tag più utilizzati: " + e.getMessage());
            e.printStackTrace();
        }
    }    
    /**
     * Genera report poesie più interagite
     */
    private void generaReportPoesiePiuInterazioni() {
        try {
            // 1. Chiamata al Controllo per ottenere la lista di stringhe (Poesia + Dettagli Interazione)
            ArrayList<String> report = controller.generaReportPoesiePiuInterazioni();
            // 2. Formattazione dell'output
            StringBuilder sb = new StringBuilder("=== REPORT POESIE CON PIÙ INTERAZIONI ===\n\n");
            if (report.isEmpty()) {
                sb.append("Nessuna poesia trovata con interazioni significative.");
            } else {
                sb.append(String.format("%-5s %-50s\n", "RANK", "DETTAGLI INTERAZIONE"));
                sb.append("------------------------------------------------------------------\n");
                int rank = 1;
                for (String poesiaDettaglio : report) {
                    // La stringa restituita dal DAO/Entity è già formattata come:
                    // "Titolo → X interazioni (Y cuori, Z commenti)"
                    sb.append(String.format("%-5d %-50s\n", 
                        rank++, 
                        poesiaDettaglio
                    ));
                }
            }
            // 3. Visualizzazione nell'area di output
            outputArea.setText(sb.toString());
            outputArea.setCaretPosition(0); // Scorre all'inizio
            mostraSuccesso("Report poesie più interagite generato con successo.");
     
        } catch (Exception e) {
            mostraErrore("Errore durante la generazione del report poesie più interagite: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private JTextArea trovaAreaOutputReport() {
        
        return new JTextArea(); 
    }
    
    /**
     * Crea il panel statistiche
     */
    private void creaStatistichePanel() {
        statistichePanel = new JPanel(new BorderLayout());
        statistichePanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Statistiche Piattaforma");
        
        // Statistiche dettagliate
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        aggiungiStatisticaDettagliata(statsPanel, "Utenti Totali", "150");
        aggiungiStatisticaDettagliata(statsPanel, "Autori Attivi", "45");
        aggiungiStatisticaDettagliata(statsPanel, "Poesie Pubblicate", "320");
        aggiungiStatisticaDettagliata(statsPanel, "Raccolte Create", "85");
        aggiungiStatisticaDettagliata(statsPanel, "Interazioni Totali", "1,250");
        aggiungiStatisticaDettagliata(statsPanel, "Media Poesie/Autore", "7.1");
        
        content.add(statsPanel, BorderLayout.CENTER);
        
        // Pulsante aggiorna statistiche
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton aggiornaBtn = creaBottone("🔄 Aggiorna Statistiche", SECONDARY_COLOR);
        JButton reportBtn = creaBottone("📊 Report Completo", PRIMARY_COLOR);
        
        aggiornaBtn.addActionListener(e -> aggiornaStatistiche());
        reportBtn.addActionListener(e -> amministratoreCorrente.visualizzaStatistichePiattaforma());
        
        bottomPanel.add(aggiornaBtn);
        bottomPanel.add(reportBtn);
        
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        statistichePanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Aggiunge statistica dettagliata
     */
    private void aggiungiStatisticaDettagliata(JPanel panel, String titolo, String valore) {
        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.setBackground(new Color(245, 248, 255));
        statPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 255), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titoloLabel = new JLabel(titolo);
        titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titoloLabel.setForeground(TEXT_COLOR);
        
        JLabel valoreLabel = new JLabel(valore);
        valoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valoreLabel.setForeground(PRIMARY_COLOR);
        valoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        statPanel.add(titoloLabel, BorderLayout.NORTH);
        statPanel.add(valoreLabel, BorderLayout.CENTER);
        
        panel.add(statPanel);
    }
    
    /**
     * Aggiorna statistiche
     */
    private void aggiornaStatistiche() {
        // Placeholder - dovrebbe aggiornare statistiche reali
        mostraSuccesso("Statistiche aggiornate!");
    }
    
    /**
     * Aggiorna il contenuto di tutte le sezioni
     */
    @Override
    protected void aggiornaContenuto() {
        // Ricarica dati correnti
        this.amministratoreCorrente = controller.getAmministratoreCorrente();
        
        // Aggiorna eventuali componenti che mostrano dati dinamici
        // Per ora è un placeholder
    }
}