package boundary;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.border.LineBorder;

import control.Controller;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoundaryCliente extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField textNome;
	private JTextField textCognome;
	private JTextField textEmail;
	private JTextField textIndirizzo;
	private JTextField textTelefono;
	private JTextField textCartaCredito;
	private JLabel lblNome;
	private JLabel lblCognome;
	private JLabel lblEmail;
	private JLabel lblIndirizzo;
	private JLabel lblTelefono;
	private JLabel lblCarta;
	
	String albergo = BoundaryUtente.nomeAlbergo;
	String dataArrivo = BoundaryUtente.dataArrivoString;
	String dataPartenza = BoundaryUtente.dataPartenzaString;
	String tipologiaCamera = BoundaryUtente.tipologiaCamera;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BoundaryCliente frame = new BoundaryCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BoundaryCliente() {
	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/images/iconP.png")));
		setTitle("[LOGIN - REGISTRAZIONE]");
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnIndietro = new JButton("INDIETRO");
		btnIndietro.setBounds(250, 400, 96, 23);
		btnIndietro.setBackground(new Color(220, 238, 241));
		btnIndietro.setSelected(true);
		btnIndietro.setForeground(new Color(0, 0, 0));
		
		float fontSizeInd = 0.025f * Math.min(getWidth(), getHeight());
		btnIndietro.setFont(new Font("Century Gothic", Font.BOLD, (int)fontSizeInd));
		
		btnIndietro.setFocusable(false);
		btnIndietro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnIndietro.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btnIndietro.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
        contentPane.add(btnIndietro);
				
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == btnIndietro) {
					new BoundaryUtente().setVisible(true);
					dispose();
				}
			}
		});
		
		// Quando il bottone viene premuto si inspessisce il bordo, mentre quando viene rilasciato torna nella posizione iniziale
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
				btnIndietro.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnIndietro.setBackground(new Color(220, 238, 241));
            	btnIndietro.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }
		});
		
		textNome = new JTextField();
		textNome.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		textNome.setBounds(280, 45, 250, 35);
		contentPane.add(textNome);
		textNome.setColumns(10);
		
		textCognome = new JTextField();
		textCognome.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		textCognome.setBounds(280, 100, 250, 35);
		contentPane.add(textCognome);
		textCognome.setColumns(10);
		
		textEmail = new JTextField();
		textEmail.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		textEmail.setBounds(280, 155, 250, 35);
		contentPane.add(textEmail);
		textEmail.setColumns(10);
		
		textIndirizzo = new JTextField();
		textIndirizzo.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		textIndirizzo.setBounds(280, 265, 250, 35);
		contentPane.add(textIndirizzo);
		textIndirizzo.setColumns(10);
		
		textTelefono = new JTextField();
		textTelefono.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		textTelefono.setBounds(280, 210, 250, 35);
		contentPane.add(textTelefono);
		textTelefono.setColumns(10);
		
		textCartaCredito = new JTextField();
		textCartaCredito.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		textCartaCredito.setBounds(280, 320, 250, 35);
		contentPane.add(textCartaCredito);
		textCartaCredito.setColumns(10);
		
		JButton btnInvia = new JButton("INVIA");
		btnInvia.setBounds(450, 400, 96, 23);
		btnInvia.setBackground(new Color(220, 238, 241));
		btnInvia.setSelected(true);
		btnInvia.setForeground(new Color(0, 0, 0));
		
		float fontSizeInv = 0.025f * Math.min(getWidth(), getHeight());
		btnInvia.setFont(new Font("Century Gothic", Font.BOLD, (int)fontSizeInv));
		
		btnInvia.setFocusable(false);
		btnInvia.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnInvia.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btnInvia.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
		
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Controller method = Controller.getInstance();
				
				String riepilogoDati = 
						"Nome: " + textNome.getText() + 
						"\nCognome: " + textCognome.getText() + 
						"\nEmail: " + textEmail.getText() +
						"\nTelefono: " + textTelefono.getText() + 
						"\nIndirizzo: " + textIndirizzo.getText() + 
						"\nNumero carta di credito: " + textCartaCredito.getText() +
						"\n\nI dati inseriti sono corretti?";
				
				
				if(textNome.getText().isEmpty() && textCognome.getText().isEmpty() &&
						textEmail.getText().isEmpty() &&  textTelefono.getText().isEmpty() &&
						textIndirizzo.getText().isEmpty() && textCartaCredito.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Inserisci almeno un dato!", "DATI NULLI", JOptionPane.ERROR_MESSAGE);
				}else {
					int choice = JOptionPane.showConfirmDialog(null, riepilogoDati, "RIEPILOGO DATI", JOptionPane.YES_NO_OPTION);
					
					if(choice == JOptionPane.YES_OPTION) {
						if(method.effettuaPrenotazione(textNome.getText(), textCognome.getText(), textEmail.getText(),
								textTelefono.getText(), textIndirizzo.getText(), textCartaCredito.getText(),
								dataArrivo, dataPartenza, tipologiaCamera, albergo)) {
							
							new BoundaryUtente().setVisible(true);
							dispose();
						}
					}
				}
			}
		});
		
		// Quando il bottone viene premuto si inspessisce il bordo, mentre quando viene rilasciato torna nella posizione iniziale
		btnInvia.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
				btnInvia.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnInvia.setBackground(new Color(220, 238, 241));
            	btnInvia.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }
		});
		
		contentPane.add(btnInvia);
		
		lblNome = new JLabel("Nome");
		
		float fontSizeN = 0.03f * Math.min(getWidth(), getHeight());
		lblNome.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeN));
		
		lblNome.setForeground(new Color(2, 18, 128));
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome.setBounds(191, 53, 79, 19);
		contentPane.add(lblNome);
		
		lblCognome = new JLabel("Cognome");
		
		float fontSizeC = 0.03f * Math.min(getWidth(), getHeight());
		lblCognome.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeC));
		
		lblCognome.setForeground(new Color(2, 18, 128));
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCognome.setBounds(182, 108, 88, 19);
		contentPane.add(lblCognome);
		
		lblEmail = new JLabel("E-mail");
		
		float fontSizeE = 0.03f * Math.min(getWidth(), getHeight());
		lblEmail.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeE));
		
		lblEmail.setForeground(new Color(2, 18, 128));
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setBounds(171, 163, 99, 19);
		contentPane.add(lblEmail);
		
		lblIndirizzo = new JLabel("Indirizzo");
		
		float fontSizeI = 0.03f * Math.min(getWidth(), getHeight());
		lblIndirizzo.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeI));
		
		lblIndirizzo.setForeground(new Color(2, 18, 128));
		lblIndirizzo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIndirizzo.setBounds(171, 273, 99, 19);
		contentPane.add(lblIndirizzo);
		
		lblTelefono = new JLabel("Telefono");
		
		float fontSizeT = 0.03f * Math.min(getWidth(), getHeight());
		lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeT));
		
		
		lblTelefono.setForeground(new Color(2, 18, 128));
		lblTelefono.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTelefono.setBounds(171, 218, 99, 19);
		contentPane.add(lblTelefono);
		
		lblCarta = new JLabel("Carta di credito");
		
		float fontSizeCa = 0.03f * Math.min(getWidth(), getHeight());
		lblCarta.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeCa));
		
		lblCarta.setForeground(new Color(2, 18, 128));
		lblCarta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCarta.setBounds(142, 328, 128, 19);
		contentPane.add(lblCarta);
		
		// Inserimento di un icona in alto a sinistra
		JLabel iconTitle = new JLabel("");
		iconTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame2_2.png")));
		iconTitle.setBounds(0, 0, 145, 97);
		contentPane.add(iconTitle);
		
		// Inserimento di un icona in alto a destra
		JLabel iconTitle2 = new JLabel("");
		iconTitle2.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame3_2_2.png")));
		iconTitle2.setBounds(706, 376, 78, 85);
		contentPane.add(iconTitle2);

		
		ImageIcon icon;
		Image img, imgScaled;
		JLabel sfondo = new JLabel("");
		sfondo.setBounds(0, 0, 1200, 461);
		icon = new ImageIcon(MainFrame.class.getResource("/images/background2.png"));
		img = icon.getImage();
		imgScaled = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(imgScaled);
		sfondo.setIcon(scaledIcon);
		contentPane.add(sfondo);
		
			}
}