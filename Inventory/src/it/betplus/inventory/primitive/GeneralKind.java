package it.betplus.inventory.primitive;

import java.io.Serializable;

public class GeneralKind implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7007787568667170585L;
	int id;
	String cod;
	String description;
	int counter;

	public GeneralKind() {
		id = 0;
		cod = "";
		description = "";
		counter = 0;
	}

	public GeneralKind(int id, String cod, String description, int counter) {
		super();
		this.id = id;
		this.cod = cod;
		this.description = description;
		this.counter = counter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cod == null) ? 0 : cod.hashCode());
		result = prime * result + counter;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralKind other = (GeneralKind) obj;
		if (cod == null) {
			if (other.cod != null)
				return false;
		} else if (!cod.equals(other.cod))
			return false;
		if (counter != other.counter)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeneralKind [id=" + id + ", cod=" + cod + ", description="
				+ description + ", counter=" + counter + "]";
	}

	public String getDerivedBarcode(){
		String barcode = "8";
		
		String kind = String.format("%6s", this.getId()).replace(' ', '0');
		String serial = String.format("%6s", this.getCounter()*7).replace(' ', '0');
		
		barcode = barcode +kind+serial;
				
		return barcode;		
	}
	
	public String getDerivedSignature(){
		String signature = this.getCod();
		
		String serial = String.format("%5s", this.getCounter()).replace(' ', '0');
		
		signature = signature +serial;
				
		return signature;		
	}

}
