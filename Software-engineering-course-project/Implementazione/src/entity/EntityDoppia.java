package entity;

import database.DoppiaDAO;

import exception.InvalidCameraTypeException;

public class EntityDoppia extends EntityCamera {

	private double prezzoPerNotte;
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @apiNote Non esiste un costruttore di default perché è impossibile creare una camera doppia, a cui
	 * corrisponde un identificativo in un database, senza verificare che la camera sia per l'appunto una Doppia
	 * 
	 * @param identificativoCamera Identificativo della camera
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera singola
	 * */
	public EntityDoppia(int identificativoCamera) throws InvalidCameraTypeException {
		super(identificativoCamera);
		
		DoppiaDAO doppia = new DoppiaDAO(this.identificativo);
		
		this.prezzoPerNotte = doppia.getPrezzoPerNotte();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param doppia Doppia già costruita
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera doppia
	 * */
	public EntityDoppia(DoppiaDAO doppia) throws InvalidCameraTypeException {
		super(doppia);
		
		// this.identificativo esiste grazie alla chiamata a super(doppia)
		if(!doppia.isDoppia(this.identificativo)) {
			throw new InvalidCameraTypeException();
		}
		
		this.prezzoPerNotte = doppia.getPrezzoPerNotte();
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
