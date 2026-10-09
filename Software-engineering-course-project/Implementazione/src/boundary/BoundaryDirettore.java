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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import control.Controller;

public class BoundaryDirettore extends JFrame {

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
					BoundaryDirettore frame = new BoundaryDirettore();
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
	public BoundaryDirettore() {
		// Caratteristiche generali del frame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/images/iconFrame4.png")));
		setResizable(false);
		setTitle("REPORT ELENCO NOTTI");
		setSize(800, 500);
		setLocationRelativeTo(null);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
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
		
		// Inserimento della parola  "email"
		JLabel lblEmail = new JLabel("Email");
		
		float fontSizeE = 0.03f * Math.min(getWidth(), getHeight());
		lblEmail.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeE));
		
		lblEmail.setForeground(new Color(2, 18, 128));
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setBounds(154, 186, 75, 35);
		contentPane.add(lblEmail);
		
		// Inserimento della casella di testo
		JTextField textEmail = new JTextField();
		textEmail.setBounds(256, 186, 300, 35);
        textEmail.setBorder(new LineBorder(new Color(0, 206, 209), 2, true));
		contentPane.add(textEmail);
		
		//TODO controllo della mail in termini di caratteri usati e dominio
		
        //Se il giorno corrente non coincide con il primo del mese, non è possibile inserire l'email
//        textEmail.addKeyListener(new KeyListener() {
//            public void keyTyped(KeyEvent e) {
//                // Controlla solo la digitazione di caratteri
//                if (Character.isLetter(e.getKeyChar())) {
//                    // Ottieni la data corrente
//                    LocalDate today = LocalDate.now();
//                    // Controlla se non è il primo giorno del mese
//                    if (today.getDayOfMonth() != 1) {
//                        // Impedisce l'inserimento del testo
//                        e.consume();
//                        JOptionPane.showMessageDialog(null,"Non e' il primo giorno del mese","ERROR",JOptionPane.ERROR_MESSAGE); 
//                    }
//                }
//            }
//            
//            //Per poter definire il KeyListener è necessario definire i seguenti override
//			@Override
//			public void keyPressed(KeyEvent e) {
//				
//			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//				
//			}
//        });
        
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
		btnIndietro.setBounds(256, 325, 89, 23);
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
        
		// Inserimento del bottone "Invia" 
        JButton btnInvia = new JButton("INVIA");
        btnInvia.setBackground(new Color(220, 238, 241));
        btnInvia.setSelected(true);
		btnInvia.setForeground(new Color(0, 0, 0));
		btnInvia.setFont(new Font("Century Gothic", Font.BOLD, 12));
		btnInvia.setFocusable(false);
		btnInvia.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnInvia.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btnInvia.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		btnInvia.setBounds(467, 325, 89, 23);
		contentPane.add(btnInvia);
		
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnInvia) {
					
					Controller method = Controller.getInstance();
					
					if(method.generaReportElencoNotti(textEmail.getText())) {
						new MainFrame().setVisible(true);
						dispose();
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

		setContentPane(contentPane);
		contentPane.setLayout(null);

	}

}