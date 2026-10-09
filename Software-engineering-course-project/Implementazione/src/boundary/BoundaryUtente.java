package boundary;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import control.Controller;

public class BoundaryUtente extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	public static String nomeAlbergo;
	public static String dataArrivoString;
	public static String dataPartenzaString;
	public static String tipologiaCamera;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BoundaryUtente frame = new BoundaryUtente();
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
	public BoundaryUtente() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/images/iconFrame4.png")));
		setTitle("ITALIAN TRAVEL");
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
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
		
		 // Crea un array di stringhe con le date
        String[] dates = generateDates();

        // Crea un JComboBox e aggiungi le date come elementi
        
        JComboBox dateAComboBox = new JComboBox(dates); 
        							//dates
        dateAComboBox.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
        dateAComboBox.setForeground(new Color(0, 0, 0));
        dateAComboBox.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        dateAComboBox.setFocusable(false);
        dateAComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dateAComboBox.setBackground(new Color(220, 238, 241));
        
        JComboBox datePComboBox = new JComboBox(dates); 
        							//dates
        datePComboBox.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
        datePComboBox.setForeground(new Color(0, 0, 0));
        datePComboBox.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        datePComboBox.setFocusable(false);
        datePComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datePComboBox.setBackground(new Color(220, 238, 241));
        
        dateAComboBox.setBounds(183, 123, 194, 32);
        datePComboBox.setBounds(183, 178, 194, 32);

        // Aggiungi il JComboBox al JFrame
        contentPane.add(dateAComboBox);
        contentPane.add(datePComboBox);
        
        String[] cities = listOfCities();
        Arrays.sort(cities);
		
		JComboBox comboCitta = new JComboBox(cities);
		comboCitta.setForeground(Color.BLACK);
		comboCitta.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		comboCitta.setFocusable(false);
		comboCitta.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
		comboCitta.setBackground(new Color(220, 238, 241));
		comboCitta.setBounds(183, 236, 194, 32);
		contentPane.add(comboCitta);

        JButton btnConferma = new JButton("CONFERMA");
        btnConferma.setBounds(306, 376, 96, 23);
        btnConferma.setBackground(new Color(220, 238, 241));
        btnConferma.setSelected(true);
        btnConferma.setForeground(new Color(0, 0, 0));
        btnConferma.setFont(new Font("Century Gothic", Font.BOLD, 14));
        btnConferma.setFocusable(false);
        btnConferma.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConferma.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        btnConferma.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
        contentPane.add(btnConferma);
        
        btnConferma.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	btnConferma.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnConferma.setBackground(new Color(220, 238, 241));
            	btnConferma.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }

		});		
        
        JButton btnPrenota = new JButton("PRENOTA");
        btnPrenota.setEnabled(false);
        btnPrenota.setVisible(false);
        btnPrenota.setBounds(543, 376, 96, 23);
        btnPrenota.setBackground(new Color(220, 238, 241));
        btnPrenota.setSelected(true);
        btnPrenota.setForeground(new Color(0, 0, 0));
        btnPrenota.setFont(new Font("Century Gothic", Font.BOLD, 14));
        btnPrenota.setFocusable(false);
        btnPrenota.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPrenota.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        btnPrenota.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
        contentPane.add(btnPrenota);
        
        btnPrenota.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	btnPrenota.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnPrenota.setBackground(new Color(220, 238, 241));
            	btnPrenota.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
            }

		});	
        
        String[] tipologie = {"Singola", "Doppia", "Tripla"};
        
        JComboBox comboBox = new JComboBox(tipologie);
		comboBox.setBounds(183, 297, 194, 32);
		comboBox.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
		comboBox.setForeground(new Color(0, 0, 0));
		comboBox.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 13));
		comboBox.setFocusable(false);
		comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comboBox.setBackground(new Color(220, 238, 241));
		contentPane.add(comboBox);
		
		JComboBox<String> citta = new JComboBox<String>();
		citta.setForeground(Color.BLACK);
		citta.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
		citta.setFocusable(false);
		citta.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
		citta.setBackground(new Color(220, 238, 241));
		citta.setBounds(183, 236, 194, 32);
		contentPane.add(citta);
		
		JTextArea textArea = new JTextArea();
		textArea.setColumns(2);
		textArea.setBounds(421, 11, 353, 318);
		textArea.setForeground(new Color(21, 21, 21));
		textArea.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
		textArea.setEnabled(true);
		textArea.setFocusable(true);
		textArea.setDisabledTextColor(new Color(185, 13, 13));
		textArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		textArea.setBorder(new LineBorder(new Color(2, 18, 128), 3, true));
		textArea.setBackground(new Color(220, 238, 241));
		textArea.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(textArea.getBounds());
		getContentPane().add(scroll);
		
        
		btnPrenota.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	btnPrenota.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnPrenota.setBackground(new Color(220, 238, 241));
            	btnPrenota.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
            }
        });
        
		btnPrenota.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(e.getSource() == btnPrenota) {
        			new BoundaryCliente().setVisible(true);
        			dispose();
        		}
        	}
        });
        
		btnConferma.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	btnConferma.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	btnConferma.setBackground(new Color(220, 238, 241));
            	btnConferma.setBorder(new LineBorder(new Color(32, 187, 204), 2, true));
            }
        });
		
		btnConferma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(e.getSource() == btnConferma) {
            		textArea.setText("");
            		textArea.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 14));
            		
            		//Controller control = new Controller();
            		String citta = (String) comboCitta.getSelectedItem();
            		tipologiaCamera = (String) comboBox.getSelectedItem();
                  	dataArrivoString = (String) dateAComboBox.getSelectedItem();
                  	dataPartenzaString = (String) datePComboBox.getSelectedItem();
                  	
                  	Controller method = Controller.getInstance();
                  	
                  	ArrayList<String> alberghi = method.verificaDisponibilitaCamera(citta, tipologiaCamera, dataArrivoString,
                  		dataPartenzaString, textArea, btnPrenota);
                  	if(!alberghi.isEmpty()) {
                  		JComboBox comboAlberghi = new JComboBox(alberghi.toArray());
                  		comboAlberghi.setFocusable(false);
                  		
                  		int choice = JOptionPane.showOptionDialog(null, comboAlberghi, "SELEZIONA L'ALBERGO",
                  				JOptionPane.DEFAULT_OPTION,	JOptionPane.PLAIN_MESSAGE, null, null, null);
                  		
                  		if(choice == JOptionPane.OK_OPTION) {
                  			nomeAlbergo = (String) comboAlberghi.getSelectedItem();
                  			textArea.setText("");
                  			textArea.setFont(new Font("Segoe UI", Font.BOLD, 16));
                  			textArea.append("\n\n\n\n\n\n\n              ALBERGO SELEZIONATO:\n"
                  					+ "                         " + nomeAlbergo);
                  		}
                  	}
            	}
            }
        });
		
		JLabel lblDataArrivo = new JLabel("Data di Arrivo");
		
		float fontSizeDA = 0.028f * Math.min(getWidth(), getHeight());
		lblDataArrivo.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeDA));
		
		lblDataArrivo.setForeground(new Color(2, 18, 128));
		lblDataArrivo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataArrivo.setBounds(15, 134, 158, 14);
		contentPane.add(lblDataArrivo);
		
		JLabel lblDataPartenza = new JLabel("Data di Partenza");
		lblDataPartenza.setForeground(new Color(2, 18, 128));
		
		float fontSizeDP = 0.028f * Math.min(getWidth(), getHeight());
		lblDataPartenza.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeDP));
		
		lblDataPartenza.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataPartenza.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblDataPartenza.setBounds(15, 189, 158, 14);
		contentPane.add(lblDataPartenza);
		
		JLabel lblCitta = new JLabel("Città");
		
		float fontSizeC = 0.028f * Math.min(getWidth(), getHeight());
		lblCitta.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeC));
		
		lblCitta.setForeground(new Color(2, 18, 128));
		lblCitta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCitta.setBounds(15, 247, 158, 14);
		contentPane.add(lblCitta);
		
		JLabel lblTipologia = new JLabel("Tipologia Camera");
		lblTipologia.setForeground(new Color(2, 18, 128));
		
		float fontSizeTC = 0.028f * Math.min(getWidth(), getHeight());
		lblTipologia.setFont(new Font("Segoe UI", Font.BOLD, (int)fontSizeTC));
		
		lblTipologia.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTipologia.setBounds(15, 307, 158, 14);
		contentPane.add(lblTipologia);
	
		JButton indietro = new JButton("INDIETRO");
        indietro.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(e.getSource()==indietro) {
        			new MainFrame().setVisible(true);
        			dispose();
        		}
        	}
        });
        indietro.setBounds(155, 376, 96, 23);
        indietro.setBackground(new Color(220, 238, 241));
        indietro.setSelected(true);
        indietro.setForeground(new Color(0, 0, 0));
        indietro.setFont(new Font("Century Gothic", Font.BOLD, 14));
        indietro.setFocusable(false);
        indietro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        indietro.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        indietro.setBorder(new LineBorder(new Color(32, 187, 204), 3, true));
        contentPane.add(indietro);
        
        indietro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	// Cambia il colore del bottone al passaggio del cursore
            	indietro.setBorder(new LineBorder(new Color(32, 187, 204), 4, true)); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            	// Ripristina il colore predefinito del bottone
            	indietro.setBackground(new Color(220, 238, 241));
            	indietro.setBorder(new LineBorder(new Color(32, 187, 204), 3, true)); 
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
 
	
	private String[] generateDates() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);
        List<String> datesList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            datesList.add(currentDate.format(formatter));
            currentDate = currentDate.plusDays(1);
        }

        String[] dates = new String[datesList.size()];
        dates = datesList.toArray(dates);

        return dates;
    }
	
	private String[] listOfCities() {
		String[] cities = {"Catanzaro", "Palermo", "Catania", "Siracusa", "Cosenza", "Crotone",
				"Potenza", "Matera", "Bari", "Foggia", "Lecce", "Benevento", "Napoli", "Pozzuoli",
				"Amalfi", "Campobasso", "Roma", "Frosinone", "Viterbo", "Pescara", "L'Aquila", "Perugia",
				"Ancona", "Firenze", "Livorno", "Pisa", "Lucca", "Bologna", "Parma", "Rimini", "Verona",
				"Treviso", "Venezia", "Genova", "Imperia", "Aosta", "Torino", "Asti", "Biella", "Milano",
				"Bergamo", "Brescia", "Cremona", "Trento", "Bolzano", "Udine", "Trieste"};
		
		return cities;
	}
}