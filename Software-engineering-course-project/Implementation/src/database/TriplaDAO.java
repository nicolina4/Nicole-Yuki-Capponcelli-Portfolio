package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import exception.InvalidCameraTypeException;

public class TriplaDAO extends CameraDAO {
	
	private double prezzoPerNotte;
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @apiNote Non esiste un costruttore di default perché è impossibile creare una camera tripla, a cui
	 * corrisponde un identificativo in un database, senza verificare che la camera sia per l'appunto una Tripla
	 * 
	 * @param identificativo Identificativo dell'albergo
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera tripla
	 * */
	public TriplaDAO(int identificativoCamera) throws InvalidCameraTypeException {
		super(identificativoCamera);
		
		if(!this.isTripla(identificativoCamera)) {
			throw new InvalidCameraTypeException();
		}
		
		readTripla();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param tripla Tripla già costruita
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera tripla
	 * */
	public TriplaDAO(TriplaDAO tripla) throws InvalidCameraTypeException {
		super(tripla);
		
		// this.identificativo esiste grazie alla chiamata a super(tripla)
		if(!this.isTripla(this.identificativo)) {
			throw new InvalidCameraTypeException();
		}
		
		this.prezzoPerNotte = tripla.getPrezzoPerNotte();
	}
	
	/**
	 * READ: Lettura da database della tripla tramite l'identificativo
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readTripla() {
		String query = "SELECT * FROM Triple WHERE identificativoCamera = " + this.identificativo;
		
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
	 * Funzione per verificare che la camera sia tripla
	 * 
	 * @param identificativo L'identificativo della camera da controllare
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return true se l'identificativo è di una stanza tripla, false altrimenti
	 * */
	public boolean isTripla(int identificativo) {
		String query = "SELECT identificativoCamera FROM Triple WHERE identificativoCamera = " + identificativo;
		
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