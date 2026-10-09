package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

import java.time.LocalDate;

public class ClienteDAO {
	
	private int identificativo;
	private String nome;
	private String cognome;
	private String email;
	private String telefono;
	private String indirizzo;
	private String numeroCartaCredito;
	private ArrayList<PrenotazioneDAO> listaPrenotazioni;

	/**
	 * Costruttore di default
	 * */
	public ClienteDAO() {
		super();
		
		this.listaPrenotazioni = new ArrayList<PrenotazioneDAO>();
	}
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param identificativo Identificativo del cliente
	 * */
	public ClienteDAO(int identificativo) {
		super();
		
		this.identificativo = identificativo;
		
		readCliente();
		readListaPrenotazioni();
	}
	
	/**
	 * Costruttore: inizializzazione tramite l'e-mail
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param email L'e-mail del cliente
	 * */
	public ClienteDAO(String email) {
		super();
		
		this.email = email;
		
		readClienteByEmail();
		readListaPrenotazioni();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param cliente Cliente già costruito
	 * */
	public ClienteDAO(ClienteDAO cliente) {
		this.identificativo=cliente.getIdentificativo();
		this.nome = cliente.getNome();
		this.cognome = cliente.getCognome();
		this.email = cliente.getEmail();
		this.telefono = cliente.getTelefono();
		this.indirizzo = cliente.getIndirizzo();
		this.numeroCartaCredito = cliente.getNumeroCartaCredito();
		this.listaPrenotazioni = cliente.getListaPrenotazioni();
	}
	
	/**
	 * CREATE: Creazione di un nuovo cliente nel database
	 * 
	 * @param nome Nome del cliente
	 * @param cognome Cognome del cliente
	 * @param email Email del cliente
	 * @param telefono Telefono del cliente
	 * @param indirizzo Indirizzo del cliente
	 * @param numeroCartaCredito Numero carta di credito del cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se il cliente è stato creato, false altrimenti
	 * */
	public boolean createCliente(String nome, String cognome, String email, String telefono, String indirizzo,
							String numeroCartaCredito) {
		int rows = 0;
		
		// LOWER serve a inserire in minuscolo l'email, indipendentemente da come viene inserita dall'utente
		String query = "INSERT INTO Clienti VALUES (" + this.getIdentificativoNuovoCliente() + ", '"
				+ nome + "', '" + cognome + "', LOWER('" + email + "'), '" + telefono + "', '" + 
				indirizzo + "', '" + numeroCartaCredito + "')";
		
		try{
			rows = DBConnectionManager.updateQuery(query);
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		if(rows > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * READ: Lettura da database del cliente tramite l'identificativo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCliente() {
		String query = "SELECT * FROM Clienti WHERE identificativo = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setNome(rs.getString("nome"));
				this.setCognome(rs.getString("cognome"));
				this.setEmail(rs.getString("email"));
				this.setTelefono(rs.getString("telefono"));
				this.setIndirizzo(rs.getString("indirizzo"));
				this.setNumeroCartaCredito(rs.getString("numeroCartaCredito"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: lettura di un cliente tramite l'e-mail
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readClienteByEmail() {
		String query = "SELECT * FROM Clienti WHERE email = '" + this.email + "'";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setIdentificativo(rs.getInt("identificativo"));
				this.setNome(rs.getString("nome"));
				this.setCognome(rs.getString("cognome"));
				this.setTelefono(rs.getString("telefono"));
				this.setIndirizzo(rs.getString("indirizzo"));
				this.setNumeroCartaCredito(rs.getString("numeroCartaCredito"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della lista di prenotazioni relative a un cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readListaPrenotazioni() {
		this.listaPrenotazioni = new ArrayList<PrenotazioneDAO>();
		
		String query = "SELECT * FROM Prenotazioni WHERE identificativoCliente = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);

			while(rs.next()) {	
				PrenotazioneDAO prenotazione = new PrenotazioneDAO();
				
				prenotazione.setCodice(rs.getInt("codice"));
			
				// Per la conversione da String a LocalDate
				String dataArrivoString = new String();
				String dataPartenzaString = new String();
			
				// Salvo nelle variabili String
				dataArrivoString = rs.getString("dataArrivo");
				dataPartenzaString = rs.getString("dataPartenza");
			
				// Converto e salvo le date nelle variabili LocalDate
				LocalDate dataArrivo = LocalDate.parse(dataArrivoString);
				LocalDate dataPartenza = LocalDate.parse(dataPartenzaString);
				
				// Set con i tipi LocalDate
				prenotazione.setDataArrivo(dataArrivo);
				prenotazione.setDataPartenza(dataPartenza);
				
				// Salvo l'identificativo dell'albergo
				int identificativoAlbergo = rs.getInt("identificativoAlbergo");
				
				// Creo un nuovo albergo in base all'identificativo
				AlbergoDAO albergo = new AlbergoDAO(identificativoAlbergo);
				
				// Imposto l'albergo relativo alla prenotazione
				prenotazione.setAlbergo(albergo);
				
				// Salvo l'identificativo dela camera
				int identificativoCamera = rs.getInt("identificativoCamera");
				
				// Creo una nuova camera in base all'identificativo
				CameraDAO camera = new CameraDAO(identificativoCamera);
				
				// Imposto la camera relativa alla prenotazione
				prenotazione.setCamera(camera);
				
				prenotazione.setNumeroStanza(rs.getInt("numeroStanza"));
				
				prenotazione.setCliente(this);
				
				this.listaPrenotazioni.add(prenotazione);
			}
		
		}catch(SQLException | ClassNotFoundException e) {
				e.printStackTrace();
		}
	}
	
	/**
	 * Ricerca l'identificativo massimo di un cliente al fine di creare un nuovo cliente, aggiungendo 1 al massimo che 
	 * trova
	 * 
	 * @apiNote Se non sono presenti clienti nel database, si procede ugualmente all'aggiornamento dell'ID
	 * perché maxId + 1 = 0 + 1 = 1
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Identificativo massimo trovato + 1
	 * */
	public int getIdentificativoNuovoCliente() {
		int maxId = 0;
		
		String query = "SELECT MAX(identificativo) FROM Clienti";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				maxId = rs.getInt("MAX(identificativo)");
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return maxId + 1;
	}
	
	/**
	 * Controllo dell'esistenza di un cliente tramite l'e-mail inserita dallo stesso e tutti gli altri 
	 * suoi dati, eccetto l'identificativo
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param nome Nome del cliente
	 * @param cognome Cognome del cliente
	 * @param email Email del cliente
	 * @param telefono Telefono del cliente
	 * @param indirizzo Indirizzo del cliente
	 * @param numeroCartaCredito Numero della carta di credito del cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se il cliente esiste, false altrimenti
	 * */
	public boolean readClienteByAllData(String nome, String cognome, String email, String telefono,
			String indirizzo, String numeroCartaCredito) {
		String query = "SELECT * FROM Clienti WHERE email = '" + email + "' AND nome = '" + nome 
				+ "' AND cognome = '" + cognome + "' AND telefono = '" + telefono
				+ "' AND indirizzo = '" + indirizzo + "' AND numeroCartaCredito = '" + numeroCartaCredito + "'";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				String readEmail = rs.getString("email");
				String readNome = rs.getString("nome");
				String readCognome = rs.getString("cognome");
				String readTelefono = rs.getString("telefono");
				String readIndirizzo = rs.getString("indirizzo");
				String readNumeroCartaCredito = rs.getString("numeroCartaCredito");
				
				return email.equalsIgnoreCase(readEmail) && 
						nome.equalsIgnoreCase(readNome) &&
						cognome.equalsIgnoreCase(readCognome) && 
						telefono.equalsIgnoreCase(readTelefono) &&
						indirizzo.equalsIgnoreCase(readIndirizzo) &&
						numeroCartaCredito.equalsIgnoreCase(readNumeroCartaCredito);
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Controllo dell'esistenza di un cliente tramite l'e-mail inserita dallo stesso, che è unique,
	 * e tramite il suo nome e cognome (utile per check in e check out)
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param email Email del cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se il cliente esiste, false altrimenti
	 * */
	public boolean readClienteByEmail(String email) {
		String query = "SELECT * FROM Clienti WHERE email = '" + email + "' AND nome = '" + this.nome
				+ "' AND cognome = '" + this.cognome + "'";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				String readEmail = rs.getString("email");
				String readNome = rs.getString("nome");
				String readCognome = rs.getString("cognome");
				
				return email.equalsIgnoreCase(readEmail) && 
						nome.equalsIgnoreCase(readNome) &&
						cognome.equalsIgnoreCase(readCognome);
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Controllo dell'esistenza di un cliente tramite l'e-mail inserita dallo stesso
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param email Email del cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se il cliente esiste, false altrimenti
	 * */
	public boolean checkUniqueEmail(String email) {
		String emailCheck = new String();
		
		String query = "SELECT email FROM Clienti WHERE email = '" + email + "'";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				emailCheck = rs.getString("email");
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return emailCheck.equals(email);
	}

	public int getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(int identificativo) {
		this.identificativo = identificativo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumeroCartaCredito() {
		return numeroCartaCredito;
	}

	public void setNumeroCartaCredito(String numeroCartaCredito) {
		this.numeroCartaCredito = numeroCartaCredito;
	}

	public ArrayList<PrenotazioneDAO> getListaPrenotazioni() {
		return listaPrenotazioni;
	}

	public void setListaPrenotazioni(ArrayList<PrenotazioneDAO> listaPrenotazioni) {
		this.listaPrenotazioni = listaPrenotazioni;
	}
	
	@Override
	public String toString() {
	    String result = "\nNome: " + nome +
	                    "\nCognome: " + cognome +
	                    "\nEmail: " + email +
	                    "\nTelefono: " + telefono +
	                    "\nIndirizzo: " + indirizzo +
	                    "\nNumero Carta Credito: " + numeroCartaCredito;
	    
	    if(!listaPrenotazioni.isEmpty()) {
	    	result += "\n\nLista di Prenotazioni:\n\n";

		    for (PrenotazioneDAO prenotazione : listaPrenotazioni) {
		        result += "Data Arrivo: " + prenotazione.getDataArrivo() + "\n" +
		                  "Data Partenza: " + prenotazione.getDataPartenza() + "\n" +
		                  "Albergo: " + prenotazione.getAlbergo().getNome() + "\n" +
		                  "Numero stanza: " + prenotazione.getNumeroStanza() + "\n\n";
		    }
	    }
	                    
	    return result;
	}
	
}