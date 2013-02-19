package it.betplus.inventory.exception;



//import org.hibernate.HibernateException;
//import org.hibernate.exception.DataException;

public class DataAccessException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String faultType;
	Exception exception;
	


	public DataAccessException(String f,Exception e){
		super();
		faultType = f;
		exception = e;
	}
	
	public DataAccessException(String f){
		super();
		faultType = f;
		exception = new Exception();
	}	



	public String getFaultType() {
		return faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String toString() {
		String message = "DataAccessException:  Message: " + faultType + " Exception: "+ exception;
		return message;
	}

}
