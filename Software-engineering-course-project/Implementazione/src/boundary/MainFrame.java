package boundary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane = new JPanel();

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

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/images/iconFrame4.png")));
		setTitle("ITALIAN TRAVEL");
		setBounds(0, 500, 920, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel title = new JLabel("");
		title.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame2.png")));
		title.setBounds(365, 80, 268, 97);
		contentPane.add(title);
		
		JLabel iconTitle = new JLabel("");
		iconTitle.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame2_2.png")));
		iconTitle.setBounds(232, 80, 145, 97);
		contentPane.add(iconTitle);
		
		JLabel iconTitle2 = new JLabel("");
		iconTitle2.setIcon(new ImageIcon(MainFrame.class.getResource("/images/titleFrame3_2_2.png")));
		iconTitle2.setBounds(0, 377, 82, 97);
		contentPane.add(iconTitle2);
		
		JLabel iconTitle3 = new JLabel("");
		iconTitle3.setIcon(new ImageIcon(MainFrame.class.getResource("/images/iconFrame4_1.png")));
		iconTitle3.setBounds(511, 134, 48, 43);
		contentPane.add(iconTitle3);
		
		// Buona combinazione labelTitle_1(250, 50, 480, 80) : background2.jpg
		// Buona combinazione labelTitle2_1(110, 60, 480, 80) : background3.png
		
		JButton utente = new JButton("UTENTE");
		utente.setBackground(new Color(220, 238, 241));
		utente.setSelected(true);
		utente.setForeground(new Color(0, 0, 0));
		
		float fontSizeU = 0.025f * Math.min(getWidth(), getHeight());
		utente.setFont(new Font("Century Gothic", Font.BOLD, (int)fontSizeU));
		
		utente.setFocusable(false);
		utente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		utente.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		utente.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		utente.setBounds(440, 278, 75, 35);
		contentPane.add(utente);
		
		utente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	utente.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	utente.setBackground(new Color(220, 238, 241));
            	utente.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }
        });
		
		utente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == utente) {
					new BoundaryUtente().setVisible(true);
					dispose();
				}
			}
		});
		
		
		JButton personale = new JButton("PERSONALE DELLA RECEPTION");
		personale.setBackground(new Color(220, 238, 241));
		personale.setSelected(true);
		personale.setForeground(new Color(0, 0, 0));
		
		float fontSizeP = 0.025f * Math.min(getWidth(), getHeight());
		personale.setFont(new Font("Century Gothic", Font.BOLD, (int)fontSizeP));
		
		personale.setFocusable(false);
		personale.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		personale.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		personale.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		personale.setBounds(110, 278, 235, 35);
		contentPane.add(personale);
		
		personale.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	personale.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	personale.setBackground(new Color(220, 238, 241));
            	personale.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }
        });
		
		personale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == personale) {
					new BoundaryPersonaleReception().setVisible(true);
					dispose();
				}
			}
		});
		
		JButton direttore = new JButton("DIRETTORE");
		direttore.setBackground(new Color(220, 238, 241));
		direttore.setSelected(true);
		direttore.setForeground(new Color(0, 0, 0));
		
		float fontSizeD = 0.025f * Math.min(getWidth(), getHeight());
		direttore.setFont(new Font("Century Gothic", Font.BOLD, (int)fontSizeD));
		
		direttore.setFocusable(false);
		direttore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		direttore.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		direttore.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
		direttore.setBounds(600, 278, 100, 35);
		contentPane.add(direttore);
		
		direttore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	direttore.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	direttore.setBackground(new Color(220, 238, 241));
            	direttore.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }

		});
		
		direttore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == direttore) {
					new BoundaryDirettore().setVisible(true);
					dispose();
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
}