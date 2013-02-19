package it.betplus.inventory.exception;

public class ConfigurationException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int code;
	String faultType;
	Exception exception;

	/** Costruttore della classe 
	 * 
	 * @param c Codice dell'errore
	 * @param f Testo dell'errore
	 * @param e Eccezione
	 */
	public ConfigurationException(int c,String f,Exception e){
		super();
		code = c;
		faultType = f;
		exception = e;
	}
	

	
	public ConfigurationException(String f){
		super();
		code = -1;
		faultType = f;
	}	
	

	

	public String toString(){
		String ret="Configuration Exception ";
		ret+="Error: "+code+" - "+faultType;
		return ret;
	}
}
