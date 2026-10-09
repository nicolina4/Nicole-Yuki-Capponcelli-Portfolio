package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import exception.InvalidCameraTypeException;

public class SingolaDAO extends CameraDAO {
	
	private double prezzoPerNotte;
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria.
	 * 
 	 * @apiNote Non esiste un costruttore di default perché è impossibile creare una camera singola, a cui
	 * corrisponde un identificativo in un database, senza verificare che la camera sia per l'appunto una Singola
	 * 
	 * @param identificativo Identificativo dell'albergo
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera singola
	 * */
	public SingolaDAO(int identificativoCamera) throws InvalidCameraTypeException {
		super(identificativoCamera);
		
		if(!this.isSingola(identificativoCamera)) {
			throw new InvalidCameraTypeException();
		}
		
		readSingola();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param singola Singola già costruita
	 *
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera singola
	 * */
	public SingolaDAO(SingolaDAO singola) throws InvalidCameraTypeException {
		super(singola);
		
		// this.identificativo esiste grazie alla chiamata a super(singola)
		if(!this.isSingola(this.identificativo)) {
			throw new InvalidCameraTypeException();
		}

		this.prezzoPerNotte = singola.getPrezzoPerNotte();
	}
	
	/**
	 * READ: Lettura da database della singola tramite l'identificativo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readSingola() {
		String query = "SELECT * FROM Singole WHERE identificativoCamera = " + this.identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setPrezzoPerNotte(rs.getDouble("prezzoPerNotte"));
			}
						
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Funzione per verificare che la camera sia singola
	 * 
	 * @param identificativo L'identificativo della camera da controllare
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return true se l'identificativo è di una stanza singola, false altrimenti
	 * */
	public boolean isSingola(int identificativo) {
		String query = "SELECT identificativoCamera FROM Singole WHERE identificativoCamera = " + identificativo;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
	        
	        if(rs.next()) {
	        	return true;
	        }
	        
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	public double getPrezzoPerNotte() {
		return prezzoPerNotte;
	}

	public void setPrezzoPerNotte(double prezzoPerNotte) {
		this.prezzoPerNotte = prezzoPerNotte;
	}
	
	
	@Override
	public String toString() {
		return "\nNumero Camera: " + numeroCamera +
		       "\nAlbergo: " + albergo.getNome() +
               "\nPrezzo Per Notte: " + prezzoPerNotte;
	}
	
}