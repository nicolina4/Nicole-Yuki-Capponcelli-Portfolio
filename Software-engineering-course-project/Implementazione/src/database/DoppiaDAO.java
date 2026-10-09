package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import exception.InvalidCameraTypeException;

public class DoppiaDAO extends CameraDAO {
	
	private double prezzoPerNotte;
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @apiNote Non esiste un costruttore di default perché è impossibile creare una camera doppia, a cui
	 * corrisponde un identificativo in un database, senza verificare che la camera sia per l'appunto una Doppia
	 * 
	 * @param identificativo Identificativo dell'albergo
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera doppia
	 * */
	public DoppiaDAO(int identificativoCamera) throws InvalidCameraTypeException {
		super(identificativoCamera);
		
		if(!this.isDoppia(identificativoCamera)) {
			throw new InvalidCameraTypeException();
		}
		
		readDoppia();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param doppia Doppia già costruita
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera doppia
	 * */
	public DoppiaDAO(DoppiaDAO doppia) throws InvalidCameraTypeException {
		super(doppia);
		
		// this.identificativo esiste grazie alla chiamata a super(doppia)
		if(!this.isDoppia(this.identificativo)) {
			throw new InvalidCameraTypeException();
		}

		this.prezzoPerNotte = doppia.getPrezzoPerNotte();
	}
	
	/**
	 * READ: Lettura da database della doppia tramite l'identificativo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readDoppia() {
		String query = "SELECT * FROM Doppie WHERE identificativoCamera = " + this.identificativo;
		
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
	 * Funzione per verificare che la camera sia doppia
	 * 
	 * @param identificativo L'identificativo della camera da controllare
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return true se l'identificativo è di una stanza doppia, false altrimenti
	 * */
	public boolean isDoppia(int identificativo) {
		String query = "SELECT identificativoCamera FROM Doppie WHERE identificativoCamera = " + identificativo;
		
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