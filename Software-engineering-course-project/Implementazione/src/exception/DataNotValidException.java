package exception;

/**
 * Eccezione che viene scatenata nel caso in cui le date di arrivo e di partenza, inserite al fine di
 * effettuare una prenotazione, non sono valide.
 * <br><br>I casi di non validità sono:
 * 
 * <dl> - la data di arrivo è successiva o uguale alla data di partenza;
 * <dl> - la data di partenza è precedente o uguale alla data di arrivo;
 * <dl> - la data di arrivo è precedente o uguale alla data odierna;
 * <dl> - la data di partenza è precedente o uguale alla data odierna.
 * */
public class DataNotValidException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di default
	 * */
	public DataNotValidException() {
		super();
	}
	
	/**
	 * Costruttore con messaggio d'errore
	 * 
	 * @param error Messaggio di errore che specifica che non esistono alberghi nella città selezionata
	 * */
	public DataNotValidException(String error) {
		super(error);
	}
}
