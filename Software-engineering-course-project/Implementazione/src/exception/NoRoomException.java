package exception;

/**
 * Eccezione che viene scatenata nel caso in cui non viene trovata una camera d'albergo della tipologia richiesta 
 * dall'utente (Singola, Doppia, Tripla) negli alberghi della città richiesta dall'utente.
 * */
public class NoRoomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di default
	 * */
	public NoRoomException() {
		super();
	}
	
	/**
	 * Costruttore con messaggio d'errore
	 * 
	 * @param error Messaggio di errore che specifica che non esistono alberghi nella città selezionata
	 * */
	public NoRoomException(String error) {
		super(error);
	}
	
}
