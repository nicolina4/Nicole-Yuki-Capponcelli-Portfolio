package database;

import java.sql.SQLException;
import java.sql.ResultSet;

public class CameraDAO {
	
	protected int identificativo;
	protected int numeroCamera;
	protected String stato;
	protected AlbergoDAO albergo;
	
	/**
	 * Costruttore di default
	 * */
	public CameraDAO() {
		super();
		
		this.albergo = new AlbergoDAO();
	}

	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param numeroCamera Numero della camera d'albergo
	 * */
	public CameraDAO(int identificativo) {
		
		this.identificativo = identificativo;
		
		readCamera();
		readAlbergo();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param camera Camera già costruita
	 * */
	public CameraDAO(CameraDAO camera) {
		this.identificativo = camera.getIdentificativo();
		this.numeroCamera = camera.getNumeroCamera();
		this.stato = camera.getStato();
		this.albergo = camera.getAlbergo();
	}

	/**
	 * READ: Lettura da database della camera tramite l'identificativo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCamera() {
		String query = "SELECT * FROM Camere WHERE identificativo = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setNumeroCamera(rs.getInt("numeroCamera"));
				this.setStato(rs.getString("stato"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database dell'albergo associato alla camera
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readAlbergo() {
		this.albergo = new AlbergoDAO();
		
		String query = "SELECT Al.identificativo, Al.codiceCatenaAlberghiera, Al.nome, Al.citta, "
				+ "Al.indirizzo, Al.CAP, Al.numeroDiTelefono "
				+ "FROM Alberghi Al JOIN Camere Ca ON Al.identificativo = Ca.identificativoAlbergo "
				+ "WHERE Ca.identificativo = " + this.identificativo;
		
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
	 * UPDATE: Aggiornamento della camera nel database
	 * 
	 * @param stato Nuovo stato della camera
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se la query ha modificato almeno una riga, false altrimenti
	 * */
	public boolean updateCamera(String stato) {
		this.setStato(stato);
		
		// UPPER serve a inserire in maiuscolo lo stato della camera, indipendentemente da come viene inserito
		String query = "UPDATE Camere SET stato = UPPER('" + stato + "') "
				+ "WHERE identificativo = " + this.identificativo;
		
		int rows = 0;
		
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

	public int getNumeroCamera() {
		return numeroCamera;
	}

	public void setNumeroCamera(int numeroCamera) {
		this.numeroCamera = numeroCamera;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public AlbergoDAO getAlbergo() {
		return albergo;
	}

	public void setAlbergo(AlbergoDAO albergo) {
		this.albergo = albergo;
	}

	public int getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(int identificativo) {
		this.identificativo = identificativo;
	}

	@Override
	public String toString() {
		return "\nNumero Camera: " + numeroCamera + 
	           "\nAlbergo: " + albergo.getNome();
	}

}