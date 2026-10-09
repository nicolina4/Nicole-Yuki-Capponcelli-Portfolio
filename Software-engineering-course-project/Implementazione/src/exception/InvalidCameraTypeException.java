package exception;


/**
 * Eccezione che viene scatenata nel caso in cui si prova a creare una stanza Singola, Doppia o Tripla che non corrisponde
 * ad una stanza nel database.
 * 
 * @apiNote Le classi Singola, Doppia, Tripla sono sottoclassi di Camera. Ciò avviene chiaramente anche nel database,
 *  dunque nessun identificativo di una Camera farà riferimento sia a una Singola, che a una DOppia che a una Tripla.
 * */
public class InvalidCameraTypeException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di default
	 * */
	public InvalidCameraTypeException() {
		super();
	}
	
	/**
	 * Costruttore con messaggio d'errore
	 * 
	 * @param error Messaggio di errore che specifica che non esistono alberghi nella città selezionata
	 * */
	public InvalidCameraTypeException(String error) {
		super(error);
	}

}
