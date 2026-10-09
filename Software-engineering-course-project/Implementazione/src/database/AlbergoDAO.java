package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

import exception.InvalidCameraTypeException;

public class AlbergoDAO {

	private int identificativo;
	private String nome;
	private String citta;
	private String indirizzo;
	private String CAP;
	private String numeroDiTelefono;
	private CatenaAlberghieraDAO catenaAlberghiera;
	private ArrayList<CameraDAO> listaCamere;
	
	/**
	 * Costruttore di default
	 * */
	public AlbergoDAO() {
		super();
		
		this.catenaAlberghiera = new CatenaAlberghieraDAO();
		this.listaCamere = new ArrayList<CameraDAO>();
	}

	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param identificativo Identificativo dell'albergo
	 * */
	public AlbergoDAO(int identificativo) {
		super();
		
		this.identificativo = identificativo;

		readAlbergo();
		readCatenaAlberghiera();
		readListaCamere();
	}
	
	/**
	 * Costruttore: inizializzazione tramite il nome
	 * 
	 * @apiNote Il nome nel database è unique
	 * 
	 * @param nome Nome dell'albergo
	 * */
	public AlbergoDAO(String nome) {
		super();
		
		this.nome = nome;

		readAlbergoByNome();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param albergo Albergo già costruito
	 * */
	public AlbergoDAO(AlbergoDAO albergo) {
		this.identificativo = albergo.getIdentificativo();
		this.nome=albergo.getNome();
		this.citta=albergo.getCitta();
		this.indirizzo = albergo.getIndirizzo();
		this.CAP = albergo.getCAP();
		this.numeroDiTelefono = albergo.getNumeroDiTelefono();
		this.catenaAlberghiera = albergo.getCatenaAlberghiera();
		this.listaCamere = albergo.getListaCamere();
	}
	
	/**
	 * READ: Lettura da database dell'albergo tramite l'identificativo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readAlbergo() {
		String query = "SELECT * FROM Alberghi WHERE identificativo = " + this.identificativo;

		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setNome(rs.getString("nome"));
				this.setCitta(rs.getString("citta"));
				this.setIndirizzo(rs.getString("indirizzo"));
				this.setCAP(rs.getString("CAP"));
				this.setNumeroDiTelefono(rs.getString("numeroDiTelefono"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database dell'albergo tramite il nome
	 * 
	 * @apiNote Il nome nel database è unique
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readAlbergoByNome() {
		String query = "SELECT * FROM Alberghi WHERE nome = '" + this.nome + "'";

		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setIdentificativo(rs.getInt("identificativo"));
				this.setCitta(rs.getString("citta"));
				this.setIndirizzo(rs.getString("indirizzo"));
				this.setCAP(rs.getString("CAP"));
				this.setNumeroDiTelefono(rs.getString("numeroDiTelefono"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della catena alberghiera relativa all'albergo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCatenaAlberghiera() {
		String query = "SELECT Ca.codice, Ca.nome, Ca.emailDirettore "
				+ "FROM CateneAlberghiere Ca JOIN Alberghi Al "
				+ "ON Ca.codice = Al.codiceCatenaAlberghiera "
				+ "WHERE Al.identificativo = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.catenaAlberghiera = new CatenaAlberghieraDAO();
				
				this.getCatenaAlberghiera().setCodice(rs.getInt("codice"));
				this.getCatenaAlberghiera().setNome(rs.getString("nome"));
				this.getCatenaAlberghiera().setEmailDirettore(rs.getString("emailDirettore"));
			}
			
			this.getCatenaAlberghiera().readListaAlberghi();
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della lista di camere relative a un albergo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readListaCamere() {
		this.listaCamere = new ArrayList<CameraDAO>();
		
		String query = "SELECT * FROM Camere WHERE identificativoAlbergo = " + this.identificativo;	
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			while(rs.next()) {
				CameraDAO camera = new CameraDAO();
				
				camera.setNumeroCamera(rs.getInt("numeroCamera"));
				camera.setAlbergo(this);
				camera.setStato(rs.getString("stato"));
				
				this.listaCamere.add(camera);
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della lista di camere singole disponibili relative a un albergo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Lista di camere singole disponibili
	 * */
	public ArrayList<SingolaDAO> readListaSingoleDisponibili() {
		ArrayList<SingolaDAO> listaSingole = new ArrayList<SingolaDAO>();
	
		String query = "SELECT Si.identificativoCamera, Si.prezzoPerNotte "
				+ "FROM ( Alberghi Al JOIN Camere Ca ON Al.identificativo = Ca.identificativoAlbergo ) "
				+ "JOIN Singole Si ON Si.identificativoCamera = Ca.identificativo "
				+ "WHERE Ca.stato = 'DISPONIBILE' AND Al.identificativo = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			while(rs.next()) {
				SingolaDAO singola = new SingolaDAO(rs.getInt("identificativoCamera"));

				singola.setPrezzoPerNotte(rs.getDouble("prezzoPerNotte"));
				
				listaSingole.add(singola);
			}
		}catch(SQLException | ClassNotFoundException | InvalidCameraTypeException e) {
			e.printStackTrace();
		}
		
		return listaSingole;
	}
	
	/**
	 * READ: Lettura da database della lista di camere doppie disponibili relative a un albergo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Lista di camere doppie disponibili
	 * */
	public ArrayList<DoppiaDAO> readListaDoppieDisponibili() {
		ArrayList<DoppiaDAO> listaDoppie = new ArrayList<DoppiaDAO>();
	
		String query = "SELECT Do.identificativoCamera, Do.prezzoPerNotte "
				+ "FROM ( Alberghi Al JOIN Camere Ca ON Al.identificativo = Ca.identificativoAlbergo ) "
				+ "JOIN Doppie Do ON Do.identificativoCamera = Ca.identificativo "
				+ "WHERE Ca.stato = 'DISPONIBILE' AND Al.identificativo = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			while(rs.next()) {
				DoppiaDAO doppia = new DoppiaDAO(rs.getInt("identificativoCamera"));
				
				doppia.setPrezzoPerNotte(rs.getDouble("prezzoPerNotte"));
				
				listaDoppie.add(doppia);
			}
		}catch(SQLException | ClassNotFoundException | InvalidCameraTypeException e) {
			e.printStackTrace();
		}
		
		return listaDoppie;
	}

	/**
	 * READ: Lettura da database della lista di camere triple disponibili relative a un albergo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Lista di camere triple disponibili
	 * */
	public ArrayList<TriplaDAO> readListaTripleDisponibili() {
		ArrayList<TriplaDAO> listaTriple = new ArrayList<TriplaDAO>();
	
		String query = "SELECT Tr.identificativoCamera, Tr.prezzoPerNotte "
				+ "FROM ( Alberghi Al JOIN Camere Ca ON Al.identificativo = Ca.identificativoAlbergo ) "
				+ "JOIN Triple Tr ON Tr.identificativoCamera = Ca.identificativo "
				+ "WHERE Ca.stato = 'DISPONIBILE' AND Al.identificativo = " + this.identificativo;	
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			while(rs.next()) {
				TriplaDAO tripla = new TriplaDAO(rs.getInt("identificativoCamera"));

				tripla.setPrezzoPerNotte(rs.getDouble("prezzoPerNotte"));
				
				listaTriple.add(tripla);
			}
		}catch(SQLException | ClassNotFoundException | InvalidCameraTypeException e) {
			e.printStackTrace();
		}
		
		return listaTriple;
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

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCAP() {
		return CAP;
	}

	public void setCAP(String cAP) {
		CAP = cAP;
	}

	public String getNumeroDiTelefono() {
		return numeroDiTelefono;
	}

	public void setNumeroDiTelefono(String numeroDiTelefono) {
		this.numeroDiTelefono = numeroDiTelefono;
	}

	public CatenaAlberghieraDAO getCatenaAlberghiera() {
		return catenaAlberghiera;
	}

	public void setCatenaAlberghiera(CatenaAlberghieraDAO catenaAlberghiera) {
		this.catenaAlberghiera = catenaAlberghiera;
	}

	public ArrayList<CameraDAO> getListaCamere() {
		return listaCamere;
	}

	public void setListaCamere(ArrayList<CameraDAO> listaCamere) {
		this.listaCamere = listaCamere;
	}

	@Override
	public String toString() {
		return "\nNome: " + nome +
	           "\nCittà: " + citta +
	           "\nIndirizzo: " + indirizzo +
	           "\nCAP: " + CAP +
	           "\nNumero di telefono: " + numeroDiTelefono +
	           "\nCatena Alberghiera: " + catenaAlberghiera.getNome();
	}
	
}