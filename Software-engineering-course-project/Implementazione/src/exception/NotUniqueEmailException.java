package exception;

/**
 * Eccezione scatenata nel caso in cui si rileva che un cliente ha provato a inserire un'e-mail
 * corrispondente già a un altro cliente nel database.
 * 
 * @apiNote L'e-mail nel database è unique.
 * */
public class NotUniqueEmailException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotUniqueEmailException() {
		super();
	}
	
    public NotUniqueEmailException(String error) {
        super(error);
    }
    
}