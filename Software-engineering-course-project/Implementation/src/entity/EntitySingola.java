package entity;

import database.SingolaDAO;

import exception.InvalidCameraTypeException;

public class EntitySingola extends EntityCamera {

	private double prezzoPerNotte;
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @apiNote Non esiste un costruttore di default perché è impossibile creare una camera singola, a cui
	 * corrisponde un identificativo in un database, senza verificare che la camera sia per l'appunto una Singola
	 * 
	 * @param identificativoCamera Identificativo della camera
	 *
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera singola
	 * */
	public EntitySingola(int identificativoCamera) throws InvalidCameraTypeException {
		super(identificativoCamera);
		
		SingolaDAO singola = new SingolaDAO(this.identificativo);
		
		this.prezzoPerNotte = singola.getPrezzoPerNotte();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param singola Singola già costruita
	 *
	 * @throws InvalidCameraTypeException Si scatena quando si prova a creare un oggetto con identificativo
	 * che non corrisponde a una camera singola
	 * */
	public EntitySingola(SingolaDAO singola) throws InvalidCameraTypeException {
		super(singola);
		
		// this.identificativo esiste grazie alla chiamata a super(singola)
		if(!singola.isSingola(this.identificativo)) {
			throw new InvalidCameraTypeException();
		}
		
		this.prezzoPerNotte = singola.getPrezzoPerNotte();
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
