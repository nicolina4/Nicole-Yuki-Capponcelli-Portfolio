package boundary;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import control.Controller;

public class BoundaryPersonaleReception extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel contentPane = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BoundaryPersonaleReception frame = new BoundaryPersonaleReception();
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
	public BoundaryPersonaleReception() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/images/iconFrame4.png")));
		setTitle("ITALIAN TRAVEL");
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel iconTitle = new JLabel("");
		iconTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame2_2.png")));
		iconTitle.setBounds(0, 0, 145, 97);
		contentPane.add(iconTitle);
		
		JLabel iconTitle2 = new JLabel("");
		iconTitle2.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame3_2_2.png")));
		iconTitle2.setBounds(706, 376, 78, 85);
		contentPane.add(iconTitle2);
		
		
		// Inserimento dei dati del cliente
		JLabel lblNome = new JLabel("Nome");
		
		float fontSizeN = 0.026f * Math.min(getWidth(), getHeight());
		lblNome.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeN));
		
		lblNome.setForeground(new Color(2, 18, 128));
		lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNome.setBounds(70, 130, 158, 14);
		contentPane.add(lblNome);
		
		JLabel lblCognome = new JLabel("Cognome");
		
		float fontSizeC = 0.026f * Math.min(getWidth(), getHeight());
		lblCognome.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeC));
		
		lblCognome.setForeground(new Color(2, 18, 128));
		lblCognome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCognome.setBounds(70, 196, 158, 14);
		contentPane.add(lblCognome);
		
		JLabel lblEmail = new JLabel("Email");
		
		float fontSizeE = 0.026f * Math.min(getWidth(), getHeight());
		lblEmail.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeE));
		
		lblEmail.setForeground(new Color(2, 18, 128));
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setBounds(70, 258, 158, 14);
		contentPane.add(lblEmail);
		
		JTextField textNome = new JTextField();
		textNome.setBounds(250, 120, 300, 35);
        textNome.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		contentPane.add(textNome);
		
		JTextField textCognome = new JTextField();
		textCognome.setBounds(250, 186, 300, 35);
		textCognome.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		contentPane.add(textCognome);
		
		JTextField textEmail = new JTextField();
		textEmail.setBounds(250, 248, 300, 35);
		textEmail.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		contentPane.add(textEmail);
		
		// Inserimento del bottone indietro
		JButton btnIndietro = new JButton("INDIETRO");
		btnIndietro.setBackground(new Color(220, 238, 241));
		btnIndietro.setSelected(true);
		btnIndietro.setForeground(new Color(0, 0, 0));
		btnIndietro.setFont(new Font("Century Gothic", Font.BOLD, 12));
		btnIndietro.setFocusable(false);
		btnIndietro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnIndietro.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btnIndietro.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		btnIndietro.setBounds(197, 344, 89, 23);
		contentPane.add(btnIndietro);
		
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
				
		// Con il click del bottone si ritorna alla pagina iniziale
		btnIndietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnIndietro) {
					new MainFrame().setVisible(true);
					dispose();
				}
			}
		});
		
		JButton btnCheckIn = new JButton("CHECK IN");
		btnCheckIn.setBackground(new Color(220, 238, 241));
		btnCheckIn.setSelected(true);
		btnCheckIn.setForeground(new Color(0, 0, 0));
		btnCheckIn.setFont(new Font("Century Gothic", Font.BOLD, 12));
		btnCheckIn.setFocusable(false);
		btnCheckIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCheckIn.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btnCheckIn.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		btnCheckIn.setBounds(352, 344, 89, 23);
		contentPane.add(btnCheckIn);
		
		// Quando il bottone viene premuto si inspessisce il bordo, mentre quando viene rilasciato torna nella posizione iniziale
		btnCheckIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	btnCheckIn.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnCheckIn.setBackground(new Color(220, 238, 241));
            	btnCheckIn.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }

		});		
		
		// Carica l'immagine da utilizzare come icona
        ImageIcon icona = new ImageIcon(MainFrame.class.getResource("/images/tick.jpg"));
        
		// Ridimensiona l'immagine all'altezza e alla larghezza desiderate
        Image immagine = icona.getImage();
        Image resizedImage = immagine.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		
        // Crea un'icona con l'immagine ridimensionata
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
		
		btnCheckIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnCheckIn) {
					Controller method = Controller.getInstance();
					
					String riepilogoDati = 
							"Nome: " + textNome.getText() + 
							"\nCognome: " + textCognome.getText() + 
							"\nEmail: " + textEmail.getText() +
							"\n\nI dati inseriti sono corretti?";
					
					if(textNome.getText().isEmpty() && textCognome.getText().isEmpty() &&
							textEmail.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Inserisci almeno un dato!", "DATI NULLI", JOptionPane.ERROR_MESSAGE);
					}else {
						int choice = JOptionPane.showConfirmDialog(null, riepilogoDati, "RIEPILOGO DATI", JOptionPane.YES_NO_OPTION);
						
						if(choice == JOptionPane.YES_OPTION) {
							if(method.effettuaCheckIn(textNome.getText(), textCognome.getText(), textEmail.getText())) {
								
								new BoundaryPersonaleReception().setVisible(true);
								dispose();
							}
						}
					}

				}
			}
		});

		JButton btnCheckOut = new JButton("CHECK OUT");
		btnCheckOut.setBackground(new Color(220, 238, 241));
		btnCheckOut.setSelected(true);
		btnCheckOut.setForeground(new Color(0, 0, 0));
		btnCheckOut.setFont(new Font("Century Gothic", Font.BOLD, 12));
		btnCheckOut.setFocusable(false);
		btnCheckOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCheckOut.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btnCheckOut.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		btnCheckOut.setBounds(504, 344, 89, 23);
		contentPane.add(btnCheckOut);
		
		// Quando il bottone viene premuto si inspessisce il bordo, mentre quando viene rilasciato torna nella posizione iniziale
		btnCheckOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	btnCheckOut.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnCheckOut.setBackground(new Color(220, 238, 241));
            	btnCheckOut.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }

		});   
		
		btnCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnCheckOut) {
					Controller method = Controller.getInstance();
					
					String riepilogoDati = 
							"Nome: " + textNome.getText() + 
							"\nCognome: " + textCognome.getText() + 
							"\nEmail: " + textEmail.getText() +
							"\n\nI dati inseriti sono corretti?";
					
					int choice = JOptionPane.showConfirmDialog(null, riepilogoDati, "RIEPILOGO DATI", JOptionPane.YES_NO_OPTION);
					
					if(choice == JOptionPane.YES_OPTION) {
						int codice = method.effettuaCheckOut(textNome.getText(),textCognome.getText(),textEmail.getText());
						if(codice > 0) {
							
							String fatturaText = method.generaFattura(codice);
							
							new BoundaryPersonaleReception().setVisible(true);
							dispose();
							
							fattura(btnCheckOut,textNome,textCognome,textEmail,fatturaText);
						}else {
							JOptionPane.showMessageDialog(null, "Nessuna camera rilevata per la generazione della fattura", "ERRORE", JOptionPane.ERROR_MESSAGE);
						
							new BoundaryPersonaleReception().setVisible(true);
							dispose();
						}
					}
				}
			}
		});
		
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
		
	public void fattura(JButton btn, JTextField name, JTextField surname, JTextField email, String fatturaText) {
		
		JPanel fattura = new JPanel();
		JFrame frameFattura = new JFrame();
					
		name.setText("");
		surname.setText("");
		email.setText("");
		
		frameFattura.setVisible(true);
		frameFattura.setTitle("RICEVUTA");
		fattura.setBorder(new LineBorder(new Color(72, 72, 72), 3));
		frameFattura.getContentPane().add(fattura);
		frameFattura.setBounds(900, 140, 250, 350);
		frameFattura.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		fattura.setBackground(new Color(255, 255, 255));
		fattura.setLayout(null);
		frameFattura.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/images/iconFattura6.png")));
		
		JLabel iconTitle = new JLabel("");
		iconTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/images/iconFattura6_1.png")));
		iconTitle.setBounds(0, 0, 59, 36);
		fattura.add(iconTitle);
		
		JLabel iconTitle2 = new JLabel("");
		iconTitle2.setIcon(new ImageIcon(MainFrame.class.getResource("/images/FATTURA_2.png")));
		iconTitle2.setBounds(122, 11, 102, 46);
		fattura.add(iconTitle2);
		
		ImageIcon icon;
		Image img, imgScaled;
		JLabel sfondo = new JLabel("");
		sfondo.setHorizontalAlignment(SwingConstants.CENTER);
		sfondo.setBounds(81, 133, 74, 60);
		icon = new ImageIcon(MainFrame.class.getResource("/images/iconFattura4_1.png"));
		img = icon.getImage();
		imgScaled = img.getScaledInstance(frameFattura.getWidth()-200, frameFattura.getHeight()-300, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(imgScaled);
		sfondo.setIcon(scaledIcon);
		sfondo.setBounds(82, 240, 74, 60);
		fattura.add(sfondo);
		
		JTextArea textArea = new JTextArea();
        textArea.setBounds(10, 102, 214, 109);
        fattura.add(textArea);
        
        textArea.setText(fatturaText);

	}
}