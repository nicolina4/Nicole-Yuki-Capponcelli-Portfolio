package exception;

/**
 * Eccezione che viene scatenata nel caso in cui viene inserita una e-mail che non corrisponde a un 
 * Direttore nel database.
 * */
public class NoDirectorFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoDirectorFoundException() {
		super();
	}
	
    public NoDirectorFoundException(String error) {
        super(error);
    }

}
