package exception;

/**
 * Eccezione scatenata nel caso in cui la data odierna non corrisponda al primo giorno del mese
 * */
public class NotFirstDayException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFirstDayException() {
		super();
	}
	
    public NotFirstDayException(String error) {
        super(error);
    }
    
}