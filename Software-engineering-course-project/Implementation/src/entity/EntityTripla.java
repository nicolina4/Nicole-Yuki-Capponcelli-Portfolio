package entity;

import database.TriplaDAO;

import exception.InvalidCameraTypeException;

public class EntityTripla extends EntityCamera {

	private double prezzoPerNotte;
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @apiNote Non esiste un costruttore di default perché è impossibile creare una camera tripla, a cui
	 * corrisponde un identificativo in un database, senza verificare che la camera sia per l'appunto una Tripla
	 * 
	 * @param identificativoCamera Identificativo della camera
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera singola
	 * */
	public EntityTripla(int identificativoCamera) throws InvalidCameraTypeException {
		super(identificativoCamera);
		
		TriplaDAO tripla = new TriplaDAO(this.identificativo);
		
		this.prezzoPerNotte = tripla.getPrezzoPerNotte();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param tripla Tripla già costruita
	 * 
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera tripla
	 * */
	public EntityTripla(TriplaDAO tripla) throws InvalidCameraTypeException {
		super(tripla);
		
		// this.identificativo esiste grazie alla chiamata a super(tripla)
		if(!tripla.isTripla(this.identificativo)) {
			throw new InvalidCameraTypeException();
		}
		
		this.prezzoPerNotte = tripla.getPrezzoPerNotte();
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
