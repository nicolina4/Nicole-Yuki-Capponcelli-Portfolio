package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.text.DecimalFormat;

import java.time.LocalDate;

public class PrenotazioneDAO {
	
	private int codice;
	private LocalDate dataArrivo;
	private LocalDate dataPartenza;
	private int numeroStanza;
	private AlbergoDAO albergo;
	private CameraDAO camera;
	private ClienteDAO cliente;
	
	/**
	 * Costruttore di default
	 * */
	public PrenotazioneDAO() {
		super();
		
		this.albergo = new AlbergoDAO();
		this.camera = new CameraDAO();
		this.cliente = new ClienteDAO(); 
	}
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param codice Codice della prenotazione
	 * */
	public PrenotazioneDAO(int codice) {
		super();
		
		this.codice = codice;
		
		readPrenotazione();
		readAlbergo();
		readCamera();
		readCliente();
		
		this.numeroStanza = this.getCamera().getNumeroCamera();
	}

	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param prenotazione Prenotazione già costruita
	 * */
	public PrenotazioneDAO(PrenotazioneDAO prenotazione) {
		this.codice = prenotazione.getCodice();
		this.dataArrivo = prenotazione.getDataArrivo();
		this.dataPartenza = prenotazione.getDataPartenza();
		this.numeroStanza = prenotazione.getNumeroStanza();
		this.albergo = prenotazione.getAlbergo();
		this.camera = prenotazione.getCamera();
		this.cliente = prenotazione.getCliente();	
	}
	
	/**
	 * CREATE: Creazione di una nuova prenotazione nel database
	 * 
	 * @param dataArrivo Data di arrivo della prenotazione
	 * @param dataPartenza Data di partenza della prenotazione
	 * @param numeroStanza Numero stanza della prenotazione
	 * @param identificativoAlbergo Identificativo dell'albergo della prenotazione
	 * @param identificativoCamera Identificativo della camera della prenotazione
	 * @param identificativoCliente Identificativo del cliente della prenotazione
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se la prenotazione è stata creata, false altrimenti
	 * */
	public boolean createPrenotazione(LocalDate dataArrivo, LocalDate dataPartenza,int numeroStanza,
					int identificativoAlbergo, int identificativoCamera, int identificativoCliente) {
		int rows = 0;
		
		String query = "INSERT INTO Prenotazioni VALUES (" + this.getCodiceNuovaPrenotazione() + ", '" +
				dataArrivo + "', '" + dataPartenza + "', " + identificativoAlbergo + ", " + identificativoCamera
				+ ", " + numeroStanza + ", " + identificativoCliente + ")";
		
		try {
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
	 * READ: Lettura da database delle prenotazioni tramite il codice
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readPrenotazione() {		
		String query = "SELECT * FROM Prenotazioni WHERE codice = " + this.codice;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
		
			if(rs.next()) {
				// Per la conversione da String a LocalDate
				String dataArrivoString = new String();
				String dataPartenzaString = new String();
			
				// Salvo nelle variabili String
				dataArrivoString = rs.getString("dataArrivo");
				dataPartenzaString = rs.getString("dataPartenza");
			
				// Converto e salvo nelle variabili LocalDate
				LocalDate dataArrivo = LocalDate.parse(dataArrivoString);
				LocalDate dataPartenza = LocalDate.parse(dataPartenzaString);
			
				this.setDataArrivo(dataArrivo);
				this.setDataPartenza(dataPartenza);
				
				this.setNumeroStanza(rs.getInt("numeroStanza"));
			}
			
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database dell'albergo relativo a una prenotazione
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readAlbergo() {
		this.albergo = new AlbergoDAO();
		
		String query = "SELECT Al.identificativo, Al.codiceCatenaAlberghiera, Al.nome, Al.citta, "
				+ "Al.indirizzo, Al.CAP, Al.numeroDiTelefono "
				+ "FROM Alberghi Al JOIN Prenotazioni Pr ON Al.identificativo = Pr.identificativoAlbergo "
				+ "WHERE Pr.codice = " + this.codice;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.getAlbergo().setIdentificativo(rs.getInt("identificativo"));
				this.getAlbergo().setNome(rs.getString("nome"));
				this.getAlbergo().setCitta(rs.getString("citta"));
				this.getAlbergo().setIndirizzo(rs.getString("indirizzo"));
				this.getAlbergo().setCAP(rs.getString("CAP"));
				this.getAlbergo().setNumeroDiTelefono(rs.getString("numeroDiTelefono"));
			}
			
			this.getAlbergo().readCatenaAlberghiera();
			this.getAlbergo().readListaCamere();
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della camera relativa a una prenotazione
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCamera() {
		this.camera = new CameraDAO();
		
		String query = "SELECT Ca.identificativo, Ca.identificativoAlbergo, Ca.numeroCamera, Ca.stato "
				+ "FROM Camere Ca JOIN Prenotazioni Pr ON Ca.identificativo = Pr.identificativoCamera "
				+ "WHERE Pr.codice = " + this.codice;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.getCamera().setIdentificativo(rs.getInt("identificativo"));
				this.getCamera().setNumeroCamera(rs.getInt("numeroCamera"));
				this.getCamera().setStato(rs.getString("stato"));
			}
			
			this.getCamera().readAlbergo();
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database del cliente relativo a una prenotazione
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCliente() {
		this.cliente = new ClienteDAO();
		
		String query = "SELECT Cl.identificativo, Cl.nome, Cl.cognome, Cl.email, Cl.telefono, Cl.indirizzo, "
				+ "Cl.numeroCartaCredito "
				+ "FROM Clienti Cl JOIN Prenotazioni Pr ON Cl.identificativo = Pr.identificativoCliente "
				+ "WHERE Pr.codice = " + this.codice;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.getCliente().setNome(rs.getString("nome"));
				this.getCliente().setCognome(rs.getString("cognome"));
				this.getCliente().setEmail(rs.getString("email"));
				this.getCliente().setTelefono(rs.getString("telefono"));
				this.getCliente().setIndirizzo(rs.getString("indirizzo"));
				this.getCliente().setNumeroCartaCredito(rs.getString("numeroCartaCredito"));
			}
			
			this.getCliente().readListaPrenotazioni();
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ricerca l'identificativo massimo di una prenotazione al fine di creare una nuova prenotazione, aggiungendo
	 * 1 al massimo che trova
	 * 
	 * @apiNote Se non sono presenti prenotazioni nel database, si procede ugualmente all'aggiornamento dell'ID
	 * perché maxId + 1 = 0 + 1 = 1
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Identificativo massimo trovato + 1
	 * */
	public int getCodiceNuovaPrenotazione() {
		int maxCodice = 0;
		
		String query = "SELECT MAX(codice) FROM Prenotazioni";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			// Ricerca del massimo ID 
			if(rs.next()) {
				maxCodice = rs.getInt("MAX(codice)");
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return maxCodice + 1;
	}
	
	/**
	 * READ: Lettura del codice della prenotazione del cliente tramite la sua e-mail
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param email E-mail del cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Codice della prenotazione
	 * */
	public int readCodicePrenotazioneCheckIn(String email) {
		LocalDate today = LocalDate.now();
		
		String query = "SELECT Pr.codice FROM (Clienti Cl JOIN Prenotazioni Pr "
					+ "ON Pr.identificativoCliente = Cl.identificativo) "
					+ "JOIN Camere Ca ON  Pr.identificativoCamera = Ca.identificativo "
					+ "WHERE Cl.email = '" + email + "' "
					+ "AND Ca.stato = 'PRENOTATA' "
					+ "AND Pr.dataArrivo = '" + today + "'";
		
		int codice = 0;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				codice = rs.getInt("codice");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return codice;
	}
	
	/**
	 * READ: Lettura del codice della prenotazione del cliente tramite la sua e-mail
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param email E-mail del cliente
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Codice della prenotazione
	 * */
	public int readCodicePrenotazioneCheckOut(String email) {
		LocalDate today = LocalDate.now();
		
		String query = "SELECT Pr.codice FROM (clienti Cl JOIN Prenotazioni Pr on Pr.identificativoCliente = Cl.identificativo) "
					+ "JOIN Camere Ca on  Pr.identificativoCamera = Ca.identificativo "
					+ "WHERE Cl.email = '" + email + "' "
					+ "AND Ca.stato = 'OCCUPATA' AND Pr.dataPartenza= '" + today + "'";
		
		int codice = 0;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				codice = rs.getInt("codice");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return codice;
	}
	
	/**
	 * Crea il prezzo complessivo relativo a una prenotazione per il riepilogo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Prezzo complessivo della prenotazione
	 * */
	public double createPrezzoComplessivo() {
		double prezzoComplessivo = 0.0;
		
		String query = "SELECT Si.prezzoPerNotte AS prezzoSingola, Dp.prezzoPerNotte AS prezzoDoppia, "
				+ "Tr.prezzoPerNotte AS prezzoTripla, DATEDIFF(Pr.dataPartenza, Pr.dataArrivo) AS numeroNotti "
				+ "FROM CAMERE Ca "
				+ "LEFT JOIN SINGOLE Si ON Ca.identificativo = Si.identificativoCamera "
				+ "LEFT JOIN DOPPIE Dp ON Ca.identificativo = Dp.identificativoCamera "
				+ "LEFT JOIN TRIPLE Tr ON Ca.identificativo = Tr.identificativoCamera "
				+ "JOIN PRENOTAZIONI Pr ON Ca.identificativo = Pr.identificativoCamera "
				+ "WHERE Ca.identificativo = " + this.camera.getIdentificativo();
		
		try {
			ResultSet resultSet = DBConnectionManager.selectQuery(query);
			
			while(resultSet.next()) {
				float prezzoSingola = resultSet.getFloat("prezzoSingola");
				float prezzoDoppia = resultSet.getFloat("prezzoDoppia");
				float prezzoTripla = resultSet.getFloat("prezzoTripla");
				int numeroNotti = resultSet.getInt("numeroNotti");
				
				double prezzoPerNotte = 0;
				
				if( prezzoSingola != 0 && prezzoDoppia == 0 && prezzoTripla == 0 ) {
					
					prezzoPerNotte = prezzoSingola;
					
				} else if( prezzoSingola == 0 && prezzoDoppia != 0 && prezzoTripla == 0 ) {
					
					prezzoPerNotte = prezzoDoppia;
					
				} else if( prezzoSingola == 0 && prezzoDoppia == 0 && prezzoTripla != 0 ) {
					
					prezzoPerNotte = prezzoTripla;
					
				}
				
				prezzoComplessivo = prezzoPerNotte * numeroNotti;
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return prezzoComplessivo;
	}
	
	/**
	 * Crea la fattura relativa a una prenotazione
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Fattura come stringa
	 * */
	public String createFattura() {
		String fattura = new String();
		
		String query = "SELECT Ca.numeroCamera, Si.prezzoPerNotte AS prezzoSingola, Dp.prezzoPerNotte AS prezzoDoppia, "
				+ "Tr.prezzoPerNotte AS prezzoTripla, DATEDIFF(Pr.dataPartenza, Pr.dataArrivo) AS numeroNotti "
				+ "FROM CAMERE Ca "
				+ "LEFT JOIN SINGOLE Si ON Ca.identificativo = Si.identificativoCamera "
				+ "LEFT JOIN DOPPIE Dp ON Ca.identificativo = Dp.identificativoCamera "
				+ "LEFT JOIN TRIPLE Tr ON Ca.identificativo = Tr.identificativoCamera "
				+ "JOIN PRENOTAZIONI Pr ON Ca.identificativo = Pr.identificativoCamera "
				+ "WHERE Ca.identificativo = " + this.camera.getIdentificativo();
		
		try {
			ResultSet resultSet = DBConnectionManager.selectQuery(query);
			
			String tipologiaCamera = new String();
			
			while(resultSet.next()) {
				int numeroCamera = resultSet.getInt("numeroCamera");
				float prezzoSingola = resultSet.getFloat("prezzoSingola");
				float prezzoDoppia = resultSet.getFloat("prezzoDoppia");
				float prezzoTripla = resultSet.getFloat("prezzoTripla");
				int numeroNotti = resultSet.getInt("numeroNotti");
				
				double prezzoPerNotte = 0;
				
				if( prezzoSingola != 0 && prezzoDoppia == 0 && prezzoTripla == 0 ) {
					
					prezzoPerNotte = prezzoSingola;
					tipologiaCamera = "Singola";
					
				} else if( prezzoSingola == 0 && prezzoDoppia != 0 && prezzoTripla == 0 ) {
					
					prezzoPerNotte = prezzoDoppia;
					tipologiaCamera = "Doppia";
					
				} else if( prezzoSingola == 0 && prezzoDoppia == 0 && prezzoTripla != 0 ) {
					
					prezzoPerNotte = prezzoTripla;
					tipologiaCamera = "Tripla";
					
				}
				
				double prezzoComplessivo = prezzoPerNotte * numeroNotti;
				
				DecimalFormat decimalFormat = new DecimalFormat("#.##"); 
				String prezzoFormattato = decimalFormat.format(prezzoComplessivo);
				
				fattura = "\nFATTURA\n\n" + " - Camera " + tipologiaCamera + " n° " + numeroCamera + " per "
						+ numeroNotti + " notti \n\t\t\t € " + prezzoFormattato;
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return fattura;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public LocalDate getDataArrivo() {
		return dataArrivo;
	}

	public void setDataArrivo(LocalDate dataArrivo) {
		this.dataArrivo = dataArrivo;
	}

	public LocalDate getDataPartenza() {
		return dataPartenza;
	}

	public void setDataPartenza(LocalDate dataPartenza) {
		this.dataPartenza = dataPartenza;
	}
	
	public int getNumeroStanza() {
		return numeroStanza;
	}

	public void setNumeroStanza(int numeroStanza) {
		this.numeroStanza = numeroStanza;
	}
	
	public AlbergoDAO getAlbergo() {
	    return albergo;
	}

	public void setAlbergo(AlbergoDAO albergo) {
		this.albergo = albergo;
	}

	public CameraDAO getCamera() {
		return camera;
	}

	public void setCamera(CameraDAO camera) {
		this.camera = camera;
	}

	public ClienteDAO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDAO cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
	    return "\nCliente: " + cliente.getNome() + " " + cliente.getCognome() +
	           "\nData Arrivo: " + dataArrivo +
	           "\nData Partenza: " + dataPartenza +
	           "\nAlbergo: " + albergo.getNome() + 
	           "\nNumero stanza: " + numeroStanza;
	}
}
