package boundary;

import java.awt.EventQueue;

import control.Controller;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Controller controller;

	
	
	 // Componenti UI
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panel per le diverse schermate
    private JPanel welcomePanel;
    private JPanel loginPanel;
    private JPanel registrazionePanel;
    
    /**
     * Costruttore
     */
    public MainFrame() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
        this.controller = Controller.getInstance();
        
        // Configurazione finestra
        setTitle("Poesiamo - Piattaforma per Poesie Brevi");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        //inizializzazione notificationManager
        NotificationManager.setMainFrame(this);
        
        // Inizializza UI
        inizializzaUI();
        
        setVisible(true);
    }
    
    /**
     * Inizializza l'interfaccia utente
     */
    private void inizializzaUI() {
        // CardLayout per gestire diverse schermate
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Crea i panel
        creaWelcomePanel();
        creaLoginPanel();
        creaRegistrazionePanel();
        
        // Aggiungi panel al CardLayout
        contentPanel.add(welcomePanel, "WELCOME");
        contentPanel.add(loginPanel, "LOGIN");
        contentPanel.add(registrazionePanel, "REGISTRAZIONE");
        
        add(contentPanel);
        
        // Mostra schermata di benvenuto
        cardLayout.show(contentPanel, "WELCOME");
    }
    
    /**
     * Crea il panel di benvenuto
     */
    private void creaWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(54, 105, 92));
        
        // Panel centrale con titolo e descrizione
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        
        // Titolo
        JLabel titleLabel = new JLabel("Poesiamo...✍ ");
        titleLabel.setFont(new Font("Handwriting", Font.BOLD, 50));
        titleLabel.setForeground(new Color(70, 70, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Sottotitolo
        JLabel subtitleLabel = new JLabel("La piattaforma per condividere le tue poesie brevi");
        subtitleLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(100, 100, 120));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Spazio
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalGlue());
        
        // Panel pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(54, 105, 92));
        
        JButton loginButton = creaBottoneStilizzato("Accedi", new Color(48, 103, 207));
        JButton registraButton = creaBottoneStilizzato("Registrati", new Color(191, 46, 73));
        JButton adminButton = creaBottoneStilizzato("Admin", new Color(133, 141, 237));
        
        loginButton.addActionListener(e -> cardLayout.show(contentPanel, "LOGIN"));
        registraButton.addActionListener(e -> cardLayout.show(contentPanel, "REGISTRAZIONE"));
        adminButton.addActionListener(e -> mostraLoginAmministratore());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registraButton);
        buttonPanel.add(adminButton);
        
        welcomePanel.add(centerPanel, BorderLayout.CENTER);
        welcomePanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crea il panel di login
     */
    private void creaLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(48, 103, 207));
        
        // Panel centrale
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Titolo
        JLabel titleLabel = new JLabel("Accedi al tuo account");
        titleLabel.setFont(new Font("Serif", Font.ITALIC, 24));
        titleLabel.setForeground(new Color(70, 70, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(emailField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(passwordField, gbc);
        
        // Pulsanti
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(164, 235, 217));
        
        JButton loginButton = creaBottoneStilizzato("Accedi", new Color(70, 130, 180));
        JButton backButton = creaBottoneStilizzato("Indietro", new Color(180, 70, 145));
        
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            if (controller.loginAutore(email, password)) {
                // Login riuscito - apri AutoreBoundary
                dispose();
                new AutoreBoundary();
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "WELCOME"));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        centerPanel.add(buttonPanel, gbc);
        
        loginPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crea il panel di registrazione
     */
    private void creaRegistrazionePanel() {
        registrazionePanel = new JPanel(new BorderLayout());
        registrazionePanel.setBackground(new Color(48, 103, 207));
        
        // Panel con scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Titolo
        JLabel titleLabel = new JLabel("Registrati come Autore");
        titleLabel.setFont(new Font("Serif", Font.ITALIC, 24));
        titleLabel.setForeground(new Color(70, 70, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campi del form
        JTextField nomeField = aggiungiCampoForm(formPanel, "Nome:", 1, gbc);
        JTextField cognomeField = aggiungiCampoForm(formPanel, "Cognome:", 2, gbc);
        JTextField emailField = aggiungiCampoForm(formPanel, "Email:", 3, gbc);
        JPasswordField passwordField = (JPasswordField) aggiungiCampoForm(formPanel, "Password:", 4, gbc, true);
        JPasswordField confermaPasswordField = (JPasswordField) aggiungiCampoForm(formPanel, "Conferma Password:", 5, gbc, true);
        
        // Bio (area di testo)
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel bioLabel = new JLabel("Biografia (opzionale):");
        bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(bioLabel, gbc);
        
        gbc.gridx = 1;
        JTextArea bioArea = new JTextArea(4, 20);
        bioArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        JScrollPane bioScroll = new JScrollPane(bioArea);
        formPanel.add(bioScroll, gbc);
        
        // Immagine profilo
        JTextField immagineField = aggiungiCampoForm(formPanel, "Immagine Profilo (opzionale):", 7, gbc);
        
        // Pulsanti
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        JButton registraButton = creaBottoneStilizzato("Registrati", new Color(70, 130, 180));
        JButton backButton = creaBottoneStilizzato("Indietro", new Color(180, 70, 145));
        
        registraButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String cognome = cognomeField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confermaPassword = new String(confermaPasswordField.getPassword());
            String bio = bioArea.getText();
            String immagine = immagineField.getText();
            
            if (controller.registraAutore(nome, cognome, email, password, confermaPassword, bio, immagine)) {
                // Registrazione riuscita - torna al login
                JOptionPane.showMessageDialog(this, 
                    "Registrazione completata! Ora puoi effettuare il login.",
                    "SUCCESSO", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(contentPanel, "LOGIN");
                
                // Pulisci campi
                nomeField.setText("");
                cognomeField.setText("");
                emailField.setText("");
                passwordField.setText("");
                confermaPasswordField.setText("");
                bioArea.setText("");
                immagineField.setText("");
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(contentPanel, "WELCOME"));
        
        buttonPanel.add(registraButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);
        
        registrazionePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Mostra dialog per login amministratore
     */
    private void mostraLoginAmministratore() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Login Amministratore", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            if (controller.loginAmministratore(email, password)) {
                // Login riuscito - apri AmministratoreBoundary
                dispose();
                new AmministratoreBoundary();
            }
        }
    }
    
    /**
     * Aggiunge un campo al form
     */
    private JTextField aggiungiCampoForm(JPanel panel, String labelText, int row, 
                                        GridBagConstraints gbc) {
        return aggiungiCampoForm(panel, labelText, row, gbc, false);
    }
    
    /**
     * Aggiunge un campo al form (con opzione password)
     */
    private JTextField aggiungiCampoForm(JPanel panel, String labelText, int row, 
                                        GridBagConstraints gbc, boolean isPassword) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        JTextField field = isPassword ? new JPasswordField(20) : new JTextField(20);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(field, gbc);
        
        return field;
    }
    
    /**
     * Crea un bottone stilizzato
     */
    private JButton creaBottoneStilizzato(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.ITALIC, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.LIGHT_GRAY.brighter());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effetto hover
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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    
    
    
    
   
    

}


