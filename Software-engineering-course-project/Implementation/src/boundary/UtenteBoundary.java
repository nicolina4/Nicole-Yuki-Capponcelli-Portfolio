package boundary;

import control.Controller;
import entity.UtenteEntity;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * UtenteBoundary - Classe base astratta per le boundary degli utenti
 * Fornisce funzionalità comuni a tutti i tipi di utente
 */
public abstract class UtenteBoundary extends JFrame {
    private static final long serialVersionUID = 1L;
    
    protected Controller controller;
    protected UtenteEntity utenteCorrente;
    
    // Componenti UI comuni
    protected JPanel mainPanel;
    protected JPanel topPanel;
    protected JPanel contentPanel;
    protected CardLayout cardLayout;
    
    // Colori tema
    protected static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    protected static final Color SECONDARY_COLOR = new Color(100, 180, 100);
    protected static final Color DANGER_COLOR = new Color(220, 80, 80);
    protected static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    protected static final Color TEXT_COLOR = new Color(50, 50, 70);
    
    /**
     * Costruttore
     */
    public UtenteBoundary(String title) {
        this.controller = Controller.getInstance();
        
        // Configurazione finestra
        setTitle(title);
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inizializza UI di base
        inizializzaUIBase();
    }
    
    /**
     * Inizializza l'interfaccia di base (comune a tutte le boundary)
     */
    private void inizializzaUIBase() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Top panel con info utente e logout
        creaTopPanel();
        
        // Content panel con CardLayout per le diverse sezioni
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Crea il pannello superiore con info utente
     */
    protected void creaTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.setPreferredSize(new Dimension(0, 70));
        
        // Info utente a sinistra
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("✍ PoesieOnline");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        userInfoPanel.add(titleLabel);
        
        // Pulsanti a destra
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton logoutButton = creaBottoneTopBar("Logout", DANGER_COLOR);
        logoutButton.addActionListener(e -> logout());
        
        buttonPanel.add(logoutButton);
        
        topPanel.add(userInfoPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
    }
    
    /**
     * Crea un bottone per la top bar
     */
    protected JButton creaBottoneTopBar(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Crea un bottone stilizzato standard
     */
    protected JButton creaBottone(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Crea un bottone piccolo
     */
    protected JButton creaBottonePiccolo(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Crea un pannello con titolo
     */
    protected JPanel creaPannelloConTitolo(String titolo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(titolo);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Crea un campo di testo stilizzato
     */
    protected JTextField creaCampoTesto(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    /**
     * Crea un'area di testo stilizzata
     */
    protected JTextArea creaAreaTesto(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return area;
    }
    
    /**
     * Crea una label stilizzata
     */
    protected JLabel creaLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    /**
     * Crea una label titolo
     */
    protected JLabel creaLabelTitolo(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    /**
     * Mostra un messaggio di successo
     */
    protected void mostraSuccesso(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, 
            "SUCCESSO", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Mostra un messaggio di errore
     */
    protected void mostraErrore(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, 
            "ERRORE", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Mostra un messaggio di conferma
     */
    protected boolean mostraConferma(String messaggio) {
        int result = JOptionPane.showConfirmDialog(this, messaggio, 
            "CONFERMA", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Logout - torna al MainFrame
     */
    protected void logout() {
        if (mostraConferma("Sei sicuro di voler effettuare il logout?")) {
            controller.logout();
            dispose();
            new MainFrame();
        }
    }
    
    /**
     * Mostra una sezione specifica del CardLayout
     */
    protected void mostraSezione(String nomeSezione) {
        cardLayout.show(contentPanel, nomeSezione);
    }
    
    /**
     * Metodo astratto da implementare nelle sottoclassi
     * per inizializzare le sezioni specifiche
     */
    protected abstract void inizializzaSezioni();
    
    /**
     * Metodo astratto per aggiornare i dati visualizzati
     */
    protected abstract void aggiornaContenuto();
}