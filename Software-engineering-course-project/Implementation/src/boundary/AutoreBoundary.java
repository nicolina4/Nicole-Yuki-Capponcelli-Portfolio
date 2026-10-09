 
package boundary;

import DTO.StatisticheDTO;
import control.Controller;
import entity.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 AutoreBoundary - Interfaccia per gli autori
  Gestisce pubblicazione poesie, feed, statistiche, profilo
 */
public class AutoreBoundary extends UtenteBoundary {
    private static final long serialVersionUID = 1L;
    
    private AutoreEntity autoreCorrente;
    
    // Panel delle sezioni
    private JPanel homePanel;
    private JPanel feedPanel;
    private JPanel pubblicaPanel;
    private JPanel poesiePanel;
    private JPanel raccoltePanel;
    private JPanel profiloPanel;
    private JPanel statistichePanel;
    private JPanel feedPoesiePanel; //per aggiornaFeed
    
    // Menu laterale
    private JPanel menuPanel;
    
    /**
     * Costruttore
     */
    public AutoreBoundary() {
        super("Poesiamo - Dashboard Autore");
        this.autoreCorrente = controller.getAutoreCorrente();
        
        // Inizializza le sezioni specifiche dell'autore
        inizializzaSezioni();
        aggiornaContenuto();
        
        setVisible(true);
    }
    
    /**
     * Inizializza le sezioni specifiche dell'autore
     */
    @Override
    protected void inizializzaSezioni() {
        // Crea menu laterale
        creaMenuLaterale();
        
        // Crea le sezioni
        creaHomePanel();
        creaFeedPanel();
        creaPubblicaPanel();
        creaPoesiePanel();
        creaRaccoltePanel();
        creaProfiloPanel();
        creaStatistichePanel();
        
        // Aggiungi sezioni al content panel
        contentPanel.add(homePanel, "HOME");
        contentPanel.add(feedPanel, "FEED");
        contentPanel.add(pubblicaPanel, "PUBBLICA");
        contentPanel.add(poesiePanel, "POESIE");
        contentPanel.add(raccoltePanel, "RACCOLTE");
        contentPanel.add(profiloPanel, "PROFILO");
        contentPanel.add(statistichePanel, "STATISTICHE");
        
        // Mostra home di default
        mostraSezione("HOME");
    }
    
    /**
     * Crea il menu laterale
     */
    private void creaMenuLaterale() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(164, 235, 217));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Pulsanti menu
        JButton homeBtn = creaBottoneMenu("🏠 Home", "HOME");
        JButton feedBtn = creaBottoneMenu("📰 Feed", "FEED");
        JButton pubblicaBtn = creaBottoneMenu("✍ Pubblica", "PUBBLICA");
        JButton poesieBtn = creaBottoneMenu("📖 Mie Poesie", "POESIE");
        JButton raccolteBtn = creaBottoneMenu("📚 Raccolte", "RACCOLTE");
        JButton profiloBtn = creaBottoneMenu("👤 Profilo", "PROFILO");
        JButton statsBtn = creaBottoneMenu("📊 Statistiche", "STATISTICHE");
        
        // Aggiungi al menu
        menuPanel.add(homeBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(feedBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(pubblicaBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(poesieBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(raccolteBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(profiloBtn);
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
        button.setBackground(new Color(80, 80, 100));
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
                button.setBackground(new Color(100, 100, 120));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 80, 100));
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
        
        JPanel content = creaPannelloConTitolo("Benvenuto, " + autoreCorrente.getNome() + "!");
        
        // Statistiche rapide
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        
        // Statistiche reali
        int totalePoesie = autoreCorrente.getPoesiePubblicate().size();
        int totaleRaccolte = autoreCorrente.getRaccolteAutore().size();
        
        // Calcola cuori totali (placeholder - dovrebbe venire dal controller)
        int cuoriTotali = 0;
        for (PoesiaEntity poesia : autoreCorrente.getPoesiePubblicate()) {
            cuoriTotali += poesia.getCuore();
        }
        
        // Calcola commenti totali (placeholder)
        int commentiTotali = 0;
        for (PoesiaEntity poesia : autoreCorrente.getPoesiePubblicate()) {
            commentiTotali += poesia.getCommenti().size();
        }
        
        aggiungiStatisticaRapida(statsPanel, "Poesie Pubblicate", String.valueOf(totalePoesie));
        aggiungiStatisticaRapida(statsPanel, "Raccolte Create", String.valueOf(totaleRaccolte));
        aggiungiStatisticaRapida(statsPanel, "Cuori Totali", String.valueOf(cuoriTotali));
        aggiungiStatisticaRapida(statsPanel, "Commenti Ricevuti", String.valueOf(commentiTotali));
        
        content.add(statsPanel, BorderLayout.CENTER);
        
        // Ultime attività
        JPanel attivitaPanel = new JPanel(new BorderLayout());
        attivitaPanel.setBackground(Color.WHITE);
        attivitaPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel attivitaLabel = creaLabelTitolo("Ultime Attività");
        attivitaPanel.add(attivitaLabel, BorderLayout.NORTH);
        
        JTextArea attivitaArea = creaAreaTesto(8, 50);
        attivitaArea.setText(generaTestoAttivitaRecenti());
        attivitaArea.setEditable(false);
        attivitaPanel.add(new JScrollPane(attivitaArea), BorderLayout.CENTER);
        
        content.add(attivitaPanel, BorderLayout.SOUTH);
        
        homePanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Genera testo attività recenti
     */
    private String generaTestoAttivitaRecenti() {
        StringBuilder sb = new StringBuilder();
        ArrayList<PoesiaEntity> poesie = autoreCorrente.getPoesiePubblicate();
        
        if (poesie.isEmpty()) {
            return "Nessuna attività recente...\nInizia pubblicando la tua prima poesia!";
        }
        
        // Mostra ultime 5 poesie
        int count = Math.min(5, poesie.size());
        for (int i = 0; i < count; i++) {
            PoesiaEntity poesia = poesie.get(i);
            sb.append("• ").append(poesia.getTitolo())
              .append(" - ").append(poesia.getDataPubblicazione().toLocalDate())
              .append(" (").append(poesia.getCuore()).append(" cuori)\n");
        }
        
        return sb.toString();
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
     * Crea il panel feed
     */
    private void creaFeedPanel() {
        feedPanel = new JPanel(new BorderLayout());
        feedPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Feed - Ultime Poesie Pubbliche");
        
        // Panel per le poesie del feed
        poesiePanel = new JPanel();
        poesiePanel.setLayout(new BoxLayout(poesiePanel, BoxLayout.Y_AXIS));
        poesiePanel.setBackground(Color.WHITE);
        
        // Pulsante aggiorna
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(Color.WHITE);
        JButton aggiornaBtn = creaBottonePiccolo("🔄 Aggiorna", SECONDARY_COLOR);
        aggiornaBtn.addActionListener(e -> aggiornaFeed());
        topPanel.add(aggiornaBtn);
        content.add(topPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(poesiePanel);
        scrollPane.setBorder(null);
        content.add(scrollPane, BorderLayout.CENTER);
        
        // Carica feed
        aggiornaFeed();
        
        
        
        feedPanel.add(content, BorderLayout.CENTER);
    }
    
    /**
 * Aggiorna il feed
 */
private void aggiornaFeed() {
    // Pulisci il panel
    poesiePanel.removeAll();
    
    ArrayList<PoesiaEntity> feed = controller.visualizzaFeed();
    
    if (feed.isEmpty()) {
        JLabel nessunaLabel = new JLabel("Nessuna poesia nel feed al momento.");
        nessunaLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        nessunaLabel.setForeground(TEXT_COLOR);
        nessunaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        poesiePanel.add(nessunaLabel);
    } else {
        for (PoesiaEntity poesia : feed) {
            poesiePanel.add(creaCardPoesia(poesia));
            poesiePanel.add(Box.createVerticalStrut(15));
        }
    }
    
    poesiePanel.revalidate();
    poesiePanel.repaint();
}
    /**
     * Crea una card per visualizzare una poesia
     */
    private JPanel creaCardPoesia(PoesiaEntity poesia) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(800, 300));
        
        // Header con autore e data
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel autoreLabel = new JLabel(poesia.getAutore().getNome() + " " + 
                                       poesia.getAutore().getCognome());
        autoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel dataLabel = new JLabel(poesia.getDataPubblicazione().toString());
        dataLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dataLabel.setForeground(Color.GRAY);
        
        headerPanel.add(autoreLabel, BorderLayout.WEST);
        headerPanel.add(dataLabel, BorderLayout.EAST);
        
        // Titolo
        JLabel titoloLabel = new JLabel(poesia.getTitolo());
        titoloLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titoloLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        //panel per i tag nel feed
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
    tagPanel.setBackground(Color.WHITE);
    tagPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
    
    ArrayList<String> tags = poesia.getTag();
    if (tags != null && !tags.isEmpty()) {
        for (String tag : tags) {
            JLabel tagLabel = new JLabel("#" + tag);
            tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            tagLabel.setBackground(new Color(200, 230, 255));
            tagLabel.setForeground(new Color(50, 100, 150));
            tagLabel.setOpaque(true);
            tagLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 190, 230), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            tagPanel.add(tagLabel);
        }
    }
        
        // Testo poesia
        JTextArea testoArea = new JTextArea(poesia.getTesto());
        testoArea.setFont(new Font("Serif", Font.PLAIN, 14));
        testoArea.setLineWrap(true);
        testoArea.setWrapStyleWord(true);
        testoArea.setEditable(false);
        testoArea.setBackground(new Color(250, 250, 255));
        testoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Footer con interazioni
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        footerPanel.setBackground(Color.WHITE);
        
        JButton cuoreBtn = creaBottonePiccolo("❤ " + poesia.getCuore(), new Color(220, 100, 100));
        JButton commentaBtn = creaBottonePiccolo("💬 Commenta", SECONDARY_COLOR);
        
        cuoreBtn.addActionListener(e -> {
            if (controller.mettiCuoreConNotifica(poesia.getIdPoesia(),autoreCorrente.getNome()+" "+autoreCorrente.getCognome(),poesia.getTitolo() )) {
                mostraSuccesso("Cuore aggiunto alla poesia!");
                aggiornaFeed();
            }
        });
        
        commentaBtn.addActionListener(e -> mostraDialogCommento(poesia));
        
        footerPanel.add(cuoreBtn);
        footerPanel.add(commentaBtn);
        
        // Assembla card
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(titoloLabel, BorderLayout.CENTER);
        card.add(tagPanel, BorderLayout.CENTER);
        card.add(new JScrollPane(testoArea), BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Mostra dialog per commentare una poesia
     */
    private void mostraDialogCommento(PoesiaEntity poesia) {
        JDialog dialog = new JDialog(this, "Commenta Poesia", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel titoloLabel = new JLabel("Commenta: \"" + poesia.getTitolo() + "\"");
        titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JTextArea commentoArea = creaAreaTesto(8, 40);
        commentoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton inviaBtn = creaBottone("Invia Commento", SECONDARY_COLOR);
        JButton annullaBtn = creaBottone("Annulla", new Color(150, 150, 150));
        inviaBtn.addActionListener(e -> {
            String commento = commentoArea.getText();
            // Verifica preliminare del testo (se il campo è lasciato vuoto)
            if (commento.trim().isEmpty()) {
                mostraErrore("Il commento non può essere vuoto.");
                return;
            }
     
            // 1. Chiama il Controller per eseguire l'azione e la persistenza
            boolean successo = controller.commentaPoesiaConNotifica(
                poesia.getIdPoesia(), 
                commento,
                autoreCorrente.getNome() + " " + autoreCorrente.getCognome(), // Autore che commenta
                poesia.getTitolo()
            );
     
            // 2. Gestione del Feedback (Boundary)
            if (successo) {
                // Se il Controller ritorna TRUE (successo persistenza)
                dialog.dispose();
                aggiornaFeed();
            } else {
                // Se il Controller ritorna FALSE (fallimento validazione/persistenza)
                // L'errore specifico (ad es. "troppo lungo") è già mostrato dal Controller
                // ma mostriamo un messaggio generico di fallback in caso di errore non catturato.
                mostraErrore("Impossibile inviare il commento. Controlla la lunghezza e riprova.");
            }
        });
        annullaBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(annullaBtn);
        buttonPanel.add(inviaBtn);
        panel.add(titoloLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(commentoArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
 * Crea il panel pubblica poesia
 */
private void creaPubblicaPanel() {
    pubblicaPanel = new JPanel(new BorderLayout());
    pubblicaPanel.setBackground(BACKGROUND_COLOR);
    
    JPanel content = creaPannelloConTitolo("Pubblica Nuova Poesia");
    
    // Form pubblicazione
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);
    
    // Titolo
    gbc.gridx = 0; gbc.gridy = 0;
    formPanel.add(creaLabel("Titolo:"), gbc);
    gbc.gridx = 1;
    JTextField titoloField = creaCampoTesto(30);
    formPanel.add(titoloField, gbc);
    
    // Testo poesia
    gbc.gridx = 0; gbc.gridy = 1;
    formPanel.add(creaLabel("Testo Poesia:"), gbc);
    gbc.gridx = 1;
    JTextArea testoArea = creaAreaTesto(10, 30);
    JScrollPane testoScroll = new JScrollPane(testoArea);
    formPanel.add(testoScroll, gbc);
    
    // Tags
    gbc.gridx = 0; gbc.gridy = 2;
    formPanel.add(creaLabel("Tags (separati da virgola):"), gbc);
    gbc.gridx = 1;
    JTextField tagsField = creaCampoTesto(30);
    formPanel.add(tagsField, gbc);
    
    // === SEZIONE RACCOLTA CON SCELTA ===
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
    JPanel raccoltaChoicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    raccoltaChoicePanel.setBackground(Color.WHITE);
    
    ButtonGroup raccoltaGroup = new ButtonGroup();
    JRadioButton nuovaRaccoltaRadio = new JRadioButton("Crea nuova raccolta");
    JRadioButton raccoltaEsistenteRadio = new JRadioButton("Usa raccolta esistente");
    nuovaRaccoltaRadio.setSelected(true); // Default: crea nuova
    nuovaRaccoltaRadio.setBackground(Color.WHITE);
    raccoltaEsistenteRadio.setBackground(Color.WHITE);
    
    raccoltaGroup.add(nuovaRaccoltaRadio);
    raccoltaGroup.add(raccoltaEsistenteRadio);
    raccoltaChoicePanel.add(nuovaRaccoltaRadio);
    raccoltaChoicePanel.add(raccoltaEsistenteRadio);
    formPanel.add(raccoltaChoicePanel, gbc);
    
    // Titolo raccolta (campo testo O combobox)
    gbc.gridwidth = 1;
    gbc.gridx = 0; gbc.gridy = 4;
    formPanel.add(creaLabel("Titolo Raccolta:"), gbc);
    gbc.gridx = 1;
    
    JTextField nuovaRaccoltaField = creaCampoTesto(30);
    JComboBox<String> raccoltaCombo = new JComboBox<>();
    raccoltaCombo.addItem("-- Seleziona raccolta --");
    
    // Carica raccolte esistenti
    ArrayList<String> titoliRaccolte = controller.getTitoliRaccolte();
    for (String titolo : titoliRaccolte) {
        raccoltaCombo.addItem(titolo);
    }
    
    // Pannello che contiene entrambi i componenti
    JPanel raccoltaInputPanel = new JPanel(new CardLayout());
    raccoltaInputPanel.add(nuovaRaccoltaField, "NUOVA");
    raccoltaInputPanel.add(raccoltaCombo, "ESISTENTE");
    formPanel.add(raccoltaInputPanel, gbc);
    
    // Descrizione raccolta (visibile solo per nuova raccolta)
    gbc.gridx = 0; gbc.gridy = 5;
    JLabel descrizioneLabel = creaLabel("Descrizione Raccolta:");
    formPanel.add(descrizioneLabel, gbc);
    gbc.gridx = 1;
    JTextArea descrizioneArea = creaAreaTesto(3, 30);
    JScrollPane descrizioneScroll = new JScrollPane(descrizioneArea);
    formPanel.add(descrizioneScroll, gbc);
    
    // Logica cambio tra nuova/esistente
    nuovaRaccoltaRadio.addActionListener(e -> {
        CardLayout cl = (CardLayout) raccoltaInputPanel.getLayout();
        cl.show(raccoltaInputPanel, "NUOVA");
        descrizioneLabel.setEnabled(true);
        descrizioneArea.setEnabled(true);
        descrizioneArea.setBackground(Color.WHITE);
    });
    
    raccoltaEsistenteRadio.addActionListener(e -> {
        CardLayout cl = (CardLayout) raccoltaInputPanel.getLayout();
        cl.show(raccoltaInputPanel, "ESISTENTE");
        descrizioneLabel.setEnabled(false);
        descrizioneArea.setEnabled(false);
        descrizioneArea.setBackground(Color.LIGHT_GRAY);
    });
    
    // Opzioni
    gbc.gridx = 0; gbc.gridy = 6;
    JCheckBox pubblicaCheck = new JCheckBox("Rendi pubblica");
    pubblicaCheck.setSelected(true);
    pubblicaCheck.setBackground(Color.WHITE);
    formPanel.add(pubblicaCheck, gbc);
    
    // Pulsante pubblica
    gbc.gridx = 1; gbc.gridy = 7;
    gbc.anchor = GridBagConstraints.EAST;
    JButton pubblicaBtn = creaBottone("Pubblica Poesia", PRIMARY_COLOR);
    formPanel.add(pubblicaBtn, gbc);
    
    pubblicaBtn.addActionListener(e -> {
        String titolo = titoloField.getText();
        String testo = testoArea.getText();
        String tagsStr = tagsField.getText();
        boolean pubblica = pubblicaCheck.isSelected();
        
        // Determina quale raccolta usare
        boolean creaRaccoltaNuova = nuovaRaccoltaRadio.isSelected();
        String titoloRaccolta;
        String descrizioneRaccolta = null;
        
        if (creaRaccoltaNuova) {
            // Nuova raccolta
            titoloRaccolta = nuovaRaccoltaField.getText();
            descrizioneRaccolta = descrizioneArea.getText();
            
            if (titoloRaccolta.isEmpty() || descrizioneRaccolta.isEmpty()) {
                mostraErrore("Inserisci titolo e descrizione per la nuova raccolta!");
                return;
            }
        } else {
            // Raccolta esistente
            titoloRaccolta = (String) raccoltaCombo.getSelectedItem();
            
            if (titoloRaccolta == null || titoloRaccolta.equals("-- Seleziona raccolta --")) {
                mostraErrore("Seleziona una raccolta esistente!");
                return;
            }
        }
        
        // Validazione base
        if (titolo.isEmpty() || testo.isEmpty()) {
            mostraErrore("Compila titolo e testo della poesia!");
            return;
        }
        
        // Processa tags
        ArrayList<String> tags = new ArrayList<>();
        if (!tagsStr.trim().isEmpty()) {
            String[] tagsArray = tagsStr.split(",");
            for (String tag : tagsArray) {
                String trimmedTag = tag.trim();
                if (!trimmedTag.isEmpty()) {
                    tags.add(trimmedTag);
                }
            }
        }
        
        if (tags.isEmpty()) {
            mostraErrore("Inserisci almeno un tag!");
            return;
        }
        
        // Pubblica la poesia
        long idPoesia = controller.pubblicaPoesia(
            titolo, 
            testo, 
            tags, 
            pubblica, 
            titoloRaccolta, 
            descrizioneRaccolta,
            creaRaccoltaNuova  
        );
        
        if (idPoesia != -1) {
            // Reset form
            titoloField.setText("");
            testoArea.setText("");
            tagsField.setText("");
            nuovaRaccoltaField.setText("");
            descrizioneArea.setText("");
            raccoltaCombo.setSelectedIndex(0);
            nuovaRaccoltaRadio.setSelected(true);
            
            mostraSuccesso("Poesia pubblicata con successo!");
            
            aggiornaContenuto();
            
            Timer timer = new Timer(100, evt -> {
        mostraSezione("POESIE");
        
    });
    
    timer.setRepeats(false);
    timer.start();
            
            // Ricarica raccolte nel combo (se ne è stata creata una nuova)
            if (creaRaccoltaNuova) {
                raccoltaCombo.removeAllItems();
                raccoltaCombo.addItem("-- Seleziona raccolta --");
                ArrayList<String> nuoviTitoli = controller.getTitoliRaccolte();
                for (String t : nuoviTitoli) {
                    raccoltaCombo.addItem(t);
                }
            }
            
            
        }
    });
    
    content.add(formPanel, BorderLayout.CENTER);
    pubblicaPanel.add(content, BorderLayout.CENTER);
}
    /**
     * Crea il panel mie poesie
     */
    private void creaPoesiePanel() {
        poesiePanel = new JPanel(new BorderLayout());
        poesiePanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Le Mie Poesie");
        
        // Lista poesie
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(Color.WHITE);
        
        
        ArrayList<PoesiaEntity> poesie = autoreCorrente.getPoesiePubblicate();
        
        if (poesie.isEmpty()) {
            JLabel nessunaLabel = new JLabel("Non hai ancora pubblicato poesie.");
            nessunaLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            nessunaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaPanel.add(nessunaLabel);
        } else {
            for (PoesiaEntity poesia : poesie) {
                listaPanel.add(creaCardMiaPoesia(poesia));
                listaPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setBorder(null);
        content.add(scrollPane, BorderLayout.CENTER);
        
        poesiePanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Crea card per poesia personale
     */
    private JPanel creaCardMiaPoesia(PoesiaEntity poesia) {
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(800, 150));
        
        // Info poesia
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        
        JLabel titoloLabel = new JLabel(poesia.getTitolo());
        titoloLabel.setFont(new Font("Serif", Font.BOLD, 16));
        
        JLabel dettagliLabel = new JLabel(
            "Data: " + poesia.getDataPubblicazione().toLocalDate() + 
            " | Cuori: " + poesia.getCuore() +
            " | Commenti: " + poesia.getCommenti().size() +
            " | " + (poesia.getPubblica() ? "Pubblica" : "Privata")
        );
        dettagliLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dettagliLabel.setForeground(Color.GRAY);
        
        //panel per i tag
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
    tagPanel.setBackground(Color.WHITE);
    
    ArrayList<String> tags = poesia.getTag();
    if (tags != null && !tags.isEmpty()) {
        for (String tag : tags) {
            JLabel tagLabel = new JLabel("#" + tag);
            tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            tagLabel.setBackground(new Color(200, 230, 255));
            tagLabel.setForeground(new Color(50, 100, 150));
            tagLabel.setOpaque(true);
            tagLabel.setBorder(BorderFactory.createCompoundBorder(
                     BorderFactory.createLineBorder(new Color(150, 190, 230), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            tagPanel.add(tagLabel);
        }
    } else {
        JLabel noTagLabel = new JLabel("Nessun tag");
        noTagLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        noTagLabel.setForeground(Color.GRAY);
        tagPanel.add(noTagLabel);
    }
        
        infoPanel.add(titoloLabel, BorderLayout.NORTH);
        infoPanel.add(dettagliLabel, BorderLayout.SOUTH);
        infoPanel.add(tagPanel, BorderLayout.SOUTH);
        
        // Anteprima testo
        JTextArea anteprimaArea = new JTextArea(
            poesia.getTesto().length() > 100 ? 
            poesia.getTesto().substring(0, 100) + "..." : 
            poesia.getTesto()
        );
        anteprimaArea.setFont(new Font("Serif", Font.PLAIN, 12));
        anteprimaArea.setLineWrap(true);
        anteprimaArea.setWrapStyleWord(true);
        anteprimaArea.setEditable(false);
        anteprimaArea.setBackground(new Color(250, 250, 255));
        
        // Azioni
        JPanel azioniPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        azioniPanel.setBackground(Color.WHITE);
        
        JButton modificaBtn = creaBottonePiccolo("Modifica", SECONDARY_COLOR);
        JButton eliminaBtn = creaBottonePiccolo("Elimina", DANGER_COLOR);
        
        modificaBtn.addActionListener(e -> mostraDialogModificaPoesia(poesia));
        eliminaBtn.addActionListener(e -> {
            if (mostraConferma("Sei sicuro di voler eliminare la poesia \"" + poesia.getTitolo() + "\"?")) {
                if (poesia.eliminaPoesia()) {
                    mostraSuccesso("Poesia eliminata con successo!");
                    aggiornaContenuto();
                }
            }
        });
        
        azioniPanel.add(modificaBtn);
        azioniPanel.add(eliminaBtn);
        
        card.add(infoPanel, BorderLayout.NORTH);
        card.add(anteprimaArea, BorderLayout.CENTER);
        card.add(azioniPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Mostra dialog modifica poesia
     */
    private void mostraDialogModificaPoesia(PoesiaEntity poesia) {
        JDialog dialog = new JDialog(this, "Modifica Poesia", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titoloLabel = new JLabel("Modifica: \"" + poesia.getTitolo() + "\"");
        titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        // Form modifica
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        
        JTextField titoloField = creaCampoTesto(30);
        titoloField.setText(poesia.getTitolo());
        
        JTextArea testoArea = creaAreaTesto(8, 30);
        testoArea.setText(poesia.getTesto());
        
        JCheckBox pubblicaCheck = new JCheckBox("Poesia pubblica");
        pubblicaCheck.setSelected(poesia.getPubblica());
        
        formPanel.add(creaLabel("Titolo:"));
        formPanel.add(titoloField);
        formPanel.add(creaLabel("Testo:"));
        formPanel.add(new JScrollPane(testoArea));
        formPanel.add(pubblicaCheck);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salvaBtn = creaBottone("Salva Modifiche", PRIMARY_COLOR);
        JButton annullaBtn = creaBottone("Annulla", new Color(150, 150, 150));
        
        salvaBtn.addActionListener(e -> {
            String nuovoTitolo = titoloField.getText();
            String nuovoTesto = testoArea.getText();
            boolean nuovaPubblica = pubblicaCheck.isSelected();
            
            if (poesia.modificaPoesia(nuovoTitolo, nuovoTesto, poesia.getTag(), nuovaPubblica)) {
                dialog.dispose();
                mostraSuccesso("Poesia modificata con successo!");
                aggiornaContenuto();
            }
        });
        
        annullaBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(annullaBtn);
        buttonPanel.add(salvaBtn);
        
        panel.add(titoloLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Crea il panel raccolte
     */
    private void creaRaccoltePanel() {
        raccoltePanel = new JPanel(new BorderLayout());
        raccoltePanel.setBackground(BACKGROUND_COLOR);
        
        JPanel content = creaPannelloConTitolo("Le Mie Raccolte");
        
        // Lista raccolte
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(Color.WHITE);
        
        ArrayList<RaccoltaEntity> raccolte = autoreCorrente.getRaccolteAutore();
        
        if (raccolte.isEmpty()) {
            JLabel nessunaLabel = new JLabel("Non hai ancora creato raccolte.");
            nessunaLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            nessunaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaPanel.add(nessunaLabel);
        } else {
            for (RaccoltaEntity raccolta : raccolte) {
                listaPanel.add(creaCardRaccolta(raccolta));
                listaPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setBorder(null);
        content.add(scrollPane, BorderLayout.CENTER);
        
        raccoltePanel.add(content, BorderLayout.CENTER);
    }
    
    /**
     * Crea card per raccolta
     */
    private JPanel creaCardRaccolta(RaccoltaEntity raccolta) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 255), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(800, 120));
        
        JLabel titoloLabel = new JLabel(raccolta.getTitolo());
        titoloLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titoloLabel.setForeground(PRIMARY_COLOR);
        
        JLabel descrizioneLabel = new JLabel(raccolta.getDescrizione());
        descrizioneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel infoLabel = new JLabel("Poesie: " + raccolta.getPoesie().size());
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(titoloLabel, BorderLayout.NORTH);
        infoPanel.add(descrizioneLabel, BorderLayout.CENTER);
        infoPanel.add(infoLabel, BorderLayout.SOUTH);
        
        // Azioni
        JPanel azioniPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        azioniPanel.setBackground(Color.WHITE);
        
        JButton visualizzaBtn = creaBottonePiccolo("Visualizza", SECONDARY_COLOR);
        JButton modificaBtn = creaBottonePiccolo("Modifica", new Color(255, 200, 100));
        JButton eliminaBtn = creaBottonePiccolo("Elimina", DANGER_COLOR);
        
        visualizzaBtn.addActionListener(e -> mostraDettagliRaccolta(raccolta));
        modificaBtn.addActionListener(e -> mostraDialogModificaRaccolta(raccolta));
        eliminaBtn.addActionListener(e -> {
            if (mostraConferma("Sei sicuro di voler eliminare la raccolta \"" + raccolta.getTitolo() + "\"?")) {
                if (raccolta.eliminaRaccolta()) {
                    mostraSuccesso("Raccolta eliminata con successo!");
                    aggiornaContenuto();
                }
            }
        });
        
        azioniPanel.add(visualizzaBtn);
        azioniPanel.add(modificaBtn);
        azioniPanel.add(eliminaBtn);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(azioniPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Mostra dettagli raccolta
     */
    private void mostraDettagliRaccolta(RaccoltaEntity raccolta) {
        JDialog dialog = new JDialog(this, "Dettagli Raccolta: " + raccolta.getTitolo(), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titoloLabel = new JLabel(raccolta.getTitolo());
        titoloLabel.setFont(new Font("Serif", Font.BOLD, 20));
        
        JLabel descrizioneLabel = new JLabel(raccolta.getDescrizione());
        descrizioneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Lista poesie nella raccolta
        JTextArea poesieArea = creaAreaTesto(15, 50);
        poesieArea.setEditable(false);
        StringBuilder sb = new StringBuilder();
        for (PoesiaEntity poesia : raccolta.getPoesie()) {
            sb.append("• ").append(poesia.getTitolo())
              .append(" (").append(poesia.getCuore()).append(" cuori)\n");
        }
        poesieArea.setText(sb.toString());
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(descrizioneLabel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(poesieArea), BorderLayout.CENTER);
        
        JButton chiudiBtn = creaBottone("Chiudi", new Color(150, 150, 150));
        chiudiBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(titoloLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(chiudiBtn, BorderLayout.SOUTH);
    
        dialog.add(panel);
        dialog.setVisible(true);
    }

    
    
    @Override
protected void aggiornaContenuto() {
    // Ricarica dati correnti
    this.autoreCorrente = controller.getAutoreCorrente();
    
    aggiornaHomePanel();
    // Aggiorna home panel con statistiche aggiornate
    aggiornaListaPoesie();
    aggiornaListaRaccolte();
    
    // Forza il refresh della UI
    contentPanel.revalidate();
    contentPanel.repaint();
    
    // Aggiorna statistiche
}

/**
 * Aggiorna il panel home con dati recenti
 */
private void aggiornaHomePanel() {
    // Questo metodo verrà chiamato quando si torna alla home
    // per mostrare dati aggiornati
    if (homePanel != null) {
        // Ricrea il panel home con dati aggiornati
        contentPanel.remove(homePanel);
        creaHomePanel();
        contentPanel.add(homePanel,"HOME");
        
    }
}




private void aggiornaListaPoesie() {
    
    
    if (poesiePanel != null) {
        // 🔥 RICREA COMPLETAMENTE il panel poesie
        poesiePanel.removeAll();
        
        // Crea un nuovo container per le poesie
        JPanel listaContainer = new JPanel();
        listaContainer.setLayout(new BoxLayout(listaContainer, BoxLayout.Y_AXIS));
        listaContainer.setBackground(Color.WHITE);
        
        ArrayList<PoesiaEntity> poesie = autoreCorrente.getPoesiePubblicate();
       
        
        if (poesie.isEmpty()) {
            JLabel nessunaLabel = new JLabel("Non hai ancora pubblicato poesie.");
            nessunaLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            nessunaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaContainer.add(nessunaLabel);
        } else {
            for (PoesiaEntity poesia : poesie) {
                
                listaContainer.add(creaCardMiaPoesia(poesia));
                listaContainer.add(Box.createVerticalStrut(10));
            }
        }
        
        //viene sostituito completamente il contenuto del poesiePanel
        poesiePanel.setLayout(new BorderLayout());
        poesiePanel.removeAll();
        poesiePanel.add(new JScrollPane(listaContainer), BorderLayout.CENTER);
        
        //refresh forzato
        poesiePanel.revalidate();
        poesiePanel.repaint();
        
        
    }
}



private void aggiornaListaRaccolte() {
    
    
    if (raccoltePanel != null) {
        // 🔥 RICREA COMPLETAMENTE il panel raccolte
        raccoltePanel.removeAll();
        
        // Crea un nuovo container per le raccolte
        JPanel listaContainer = new JPanel();
        listaContainer.setLayout(new BoxLayout(listaContainer, BoxLayout.Y_AXIS));
        listaContainer.setBackground(Color.WHITE);
        
        ArrayList<RaccoltaEntity> raccolte = autoreCorrente.getRaccolteAutore();
        
        
        if (raccolte.isEmpty()) {
            JLabel nessunaLabel = new JLabel("Non hai ancora creato raccolte.");
            nessunaLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            nessunaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaContainer.add(nessunaLabel);
        } else {
            for (RaccoltaEntity raccolta : raccolte) {
                
                listaContainer.add(creaCardRaccolta(raccolta));
                listaContainer.add(Box.createVerticalStrut(15));
            }
        }
        
        //viene sostituito completamente il contenuto del raccoltePanel
        raccoltePanel.setLayout(new BorderLayout());
        raccoltePanel.removeAll();
        raccoltePanel.add(new JScrollPane(listaContainer), BorderLayout.CENTER);
        
        //refresh forzato
        raccoltePanel.revalidate();
        raccoltePanel.repaint();
        
        
    }
}
    
    /**
 * Crea il panel profilo
 */
private void creaProfiloPanel() {
    profiloPanel = new JPanel(new BorderLayout());
    profiloPanel.setBackground(BACKGROUND_COLOR);
    
    JPanel content = creaPannelloConTitolo("Il Mio Profilo");
    
    // Form profilo
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);
    
    // Nome
    gbc.gridx = 0; gbc.gridy = 0;
    formPanel.add(creaLabel("Nome:"), gbc);
    gbc.gridx = 1;
    JTextField nomeField = creaCampoTesto(25);
    nomeField.setText(autoreCorrente.getNome());
    formPanel.add(nomeField, gbc);
    
    // Cognome
    gbc.gridx = 0; gbc.gridy = 1;
    formPanel.add(creaLabel("Cognome:"), gbc);
    gbc.gridx = 1;
    JTextField cognomeField = creaCampoTesto(25);
    cognomeField.setText(autoreCorrente.getCognome());
    formPanel.add(cognomeField, gbc);
    
    // Email
    gbc.gridx = 0; gbc.gridy = 2;
    formPanel.add(creaLabel("Email:"), gbc);
    gbc.gridx = 1;
    JTextField emailField = creaCampoTesto(25);
    emailField.setText(autoreCorrente.getEmail());
    formPanel.add(emailField, gbc);
    
    // Biografia
    gbc.gridx = 0; gbc.gridy = 3;
    formPanel.add(creaLabel("Biografia:"), gbc);
    gbc.gridx = 1;
    JTextArea bioArea = creaAreaTesto(5, 25);
    bioArea.setText(autoreCorrente.getBio() != null ? autoreCorrente.getBio() : "");
    formPanel.add(new JScrollPane(bioArea), gbc);
    
    // Pulsanti
    gbc.gridx = 1; gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.EAST;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(Color.WHITE);
    
    JButton salvaBtn = creaBottone("Salva Modifiche", PRIMARY_COLOR);
    JButton resetBtn = creaBottone("Reimposta", new Color(150, 150, 150));
    
    salvaBtn.addActionListener(e -> {
        String nuovoNome = nomeField.getText();
        String nuovoCognome = cognomeField.getText();
        String nuovaEmail = emailField.getText();
        String nuovaBio = bioArea.getText();
        
        if (controller.aggiornaProfiloAutore(nuovoNome, nuovoCognome, nuovaEmail, nuovaBio)) {
            mostraSuccesso("Profilo aggiornato con successo!");
            aggiornaContenuto();
        }
    });
    
    resetBtn.addActionListener(e -> {
        nomeField.setText(autoreCorrente.getNome());
        cognomeField.setText(autoreCorrente.getCognome());
        emailField.setText(autoreCorrente.getEmail());
        bioArea.setText(autoreCorrente.getBio() != null ? autoreCorrente.getBio() : "");
    });
    
    buttonPanel.add(resetBtn);
    buttonPanel.add(salvaBtn);
    formPanel.add(buttonPanel, gbc);
    
    content.add(formPanel, BorderLayout.CENTER);
    profiloPanel.add(content, BorderLayout.CENTER);
}

private void creaStatistichePanel() {
    statistichePanel = new JPanel(new BorderLayout());
    statistichePanel.setBackground(BACKGROUND_COLOR);
   
    //  1. CHIAMATA AL CONTROL per ottenere il DTO con i dati aggregati
    StatisticheDTO stats = controller.visualizzaStatistiche();       // CHIAMATA CONTROLLER che fa partire il caso d'uso
    
    JPanel content = creaPannelloConTitolo("Le Mie Statistiche");
    // Statistiche dettagliate
    JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
    statsPanel.setBackground(Color.WHITE);
    statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    // 2. RECUPERO/INIZIALIZZAZIONE DATI PER LA PRESENTAZIONE
    int totalePoesie = autoreCorrente.getPoesiePubblicate().size();
    int totaleRaccolte = autoreCorrente.getRaccolteAutore().size();
    int cuoriTotali = 0;
    int commentiTotali = 0;
    int poesiaPiuCuori = 0;
    String infoPoesiaPiuCuori = "Nessuna poesia"; // Default se non ci sono poesie
    if (stats != null) {
        // 3. ESTRAZIONE DATI DAL DTO (BCED)
        cuoriTotali = stats.getTotaleCuori();
        commentiTotali = stats.getTotaleCommenti();
        if (stats.getPoesiaPiuApprezzata() != null) {
            PoesiaEntity topPoesia = stats.getPoesiaPiuApprezzata();
            poesiaPiuCuori = topPoesia.getCuore();
            // Logica di gestione del fallback per la Poesia più popolare
            if (poesiaPiuCuori > 0 && topPoesia.getTitolo() != null && !topPoesia.getTitolo().isEmpty()) {
                // Caso A: Trovata Poesia con cuori > 0
                infoPoesiaPiuCuori = topPoesia.getTitolo() + " (" + poesiaPiuCuori + " cuori)";
            } else if (totalePoesie > 0) {
                // Caso B: Poesie pubblicate, ma nessuna ha cuori > 0 (o il Titolo è mancante, risolto dal DAO)
                infoPoesiaPiuCuori = "Le tue poesie non hanno ancora ricevuto cuori.";
            } else {
                // Caso C: Nessuna poesia pubblicata
                infoPoesiaPiuCuori = "Ancora nessuna poesia pubblicata.";
            }
        } else if (totalePoesie > 0) {
             // Caso D: Il DAO non ha restituito una top poesia, ma le liste locali non sono vuote
             infoPoesiaPiuCuori = "Le tue poesie non hanno ancora ricevuto cuori.";
        }
    }
    // Calcolo metriche derivate (logica di presentazione)
    double mediaCuoriPerPoesia = totalePoesie > 0 ? (double) cuoriTotali / totalePoesie : 0;
    // 4. AGGIUNTA STATISTICHE AL PANNELLO
    aggiungiStatisticaDettagliata(statsPanel, "Poesie Pubblicate", String.valueOf(totalePoesie));
    aggiungiStatisticaDettagliata(statsPanel, "Raccolte Create", String.valueOf(totaleRaccolte));
    aggiungiStatisticaDettagliata(statsPanel, "Cuori Totali", String.valueOf(cuoriTotali));
    aggiungiStatisticaDettagliata(statsPanel, "Commenti Totali", String.valueOf(commentiTotali));
    aggiungiStatisticaDettagliata(statsPanel, "Media Cuori/Poesia", String.format("%.1f", mediaCuoriPerPoesia));
    aggiungiStatisticaDettagliata(statsPanel, "Poesia Più Popolare", infoPoesiaPiuCuori);
    content.add(statsPanel, BorderLayout.CENTER);
    // Grafico statistiche (placeholder)
    JPanel graficoPanel = new JPanel(new BorderLayout());
    graficoPanel.setBackground(Color.WHITE);
    graficoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    JLabel graficoLabel = creaLabelTitolo("Andamento Pubblicazioni");
    graficoPanel.add(graficoLabel, BorderLayout.NORTH);
    JTextArea graficoArea = creaAreaTesto(6, 50);
    graficoArea.setText(generaTestoGraficoStatistiche());
    graficoArea.setEditable(false);
    graficoArea.setBackground(new Color(250, 250, 255));
    graficoPanel.add(new JScrollPane(graficoArea), BorderLayout.CENTER);
    content.add(graficoPanel, BorderLayout.SOUTH);
    statistichePanel.add(content, BorderLayout.CENTER);
}
 



/**
 * Genera testo per grafico statistiche (placeholder)
 */
private String generaTestoGraficoStatistiche() {
    StringBuilder sb = new StringBuilder();
    sb.append("Ultimi 6 mesi:\n\n");
    
    // Placeholder - dati fittizi
    String[] mesi = {"Ott", "Nov", "Dic", "Gen", "Feb", "Mar"};
    int[] pubblicazioni = {2, 3, 5, 4, 6, 8};
    
    for (int i = 0; i < mesi.length; i++) {
        sb.append(mesi[i]).append(": ");
        for (int j = 0; j < pubblicazioni[i]; j++) {
            sb.append("█");
        }
        sb.append(" (").append(pubblicazioni[i]).append(" poesie)\n");
    }
    
    return sb.toString();
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
    valoreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    valoreLabel.setForeground(PRIMARY_COLOR);
    valoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
    statPanel.add(titoloLabel, BorderLayout.NORTH);
    statPanel.add(valoreLabel, BorderLayout.CENTER);
    
    panel.add(statPanel);
}

/**
 * Mostra dialog modifica raccolta
 */
private void mostraDialogModificaRaccolta(RaccoltaEntity raccolta) {
    JDialog dialog = new JDialog(this, "Modifica Raccolta", true);
    dialog.setSize(500, 400);
    dialog.setLocationRelativeTo(this);
    
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
    JLabel titoloLabel = new JLabel("Modifica: \"" + raccolta.getTitolo() + "\"");
    titoloLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    
    // Form modifica
    JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
    
    JTextField titoloField = creaCampoTesto(30);
    titoloField.setText(raccolta.getTitolo());
    
    JTextArea descrizioneArea = creaAreaTesto(5, 30);
    descrizioneArea.setText(raccolta.getDescrizione());
    
    formPanel.add(creaLabel("Titolo:"));
    formPanel.add(titoloField);
    formPanel.add(creaLabel("Descrizione:"));
    formPanel.add(new JScrollPane(descrizioneArea));
    
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton salvaBtn = creaBottone("Salva Modifiche", PRIMARY_COLOR);
    JButton annullaBtn = creaBottone("Annulla", new Color(150, 150, 150));
    
    salvaBtn.addActionListener(e -> {
        String nuovoTitolo = titoloField.getText();
        String nuovaDescrizione = descrizioneArea.getText();
        
        if (raccolta.modificaRaccolta(nuovoTitolo, nuovaDescrizione)) {
            dialog.dispose();
            mostraSuccesso("Raccolta modificata con successo!");
            aggiornaContenuto();
        }
    });
    
    annullaBtn.addActionListener(e -> dialog.dispose());
    
    buttonPanel.add(annullaBtn);
    buttonPanel.add(salvaBtn);
    
    panel.add(titoloLabel, BorderLayout.NORTH);
    panel.add(formPanel, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);
    
    dialog.add(panel);
    dialog.setVisible(true);
}
    
}
    