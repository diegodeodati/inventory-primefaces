package it.betplus.inventory.primitive;

import java.io.Serializable;

public class Barcode_print implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -588310473309257347L;
	
	
	Barcode_item barcode;
	int quantity;
	
	public Barcode_print(Barcode_item barcode, int quantity) {
		super();
		this.barcode = barcode;
		this.quantity = quantity;
	}
	
	public Barcode_print(){
		barcode = new Barcode_item();
		quantity = 0;
	}

	public Barcode_item getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode_item barcode) {
		this.barcode = barcode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result + quantity;
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
		Barcode_print other = (Barcode_print) obj;
		if (barcode == null) {
			if (other.barcode != null)
				return false;
		} else if (!barcode.equals(other.barcode))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Barcode_print [barcode=" + barcode + ", quantity=" + quantity
				+ "]";
	}
	
	
	
	
	
	

}
