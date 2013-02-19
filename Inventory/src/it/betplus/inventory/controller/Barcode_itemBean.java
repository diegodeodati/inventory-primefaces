package it.betplus.inventory.controller;

import it.betplus.inventory.primitive.GeneralKind;
import it.betplus.web.framework.managedbeans.GeneralBean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "barcode_itemBean")
@RequestScoped
public class Barcode_itemBean extends GeneralBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3936743854885421428L;
	String barcode;
	String description;
	int quantity;
	GeneralKind gKind;

	public Barcode_itemBean(String barcode, String description,
			GeneralKind gKind,int quantity) {
		this.barcode = barcode;
		this.description = description;
		this.gKind = gKind;
		this.quantity = quantity;
	}

	public Barcode_itemBean() {
		barcode = "";
		description = "";
		gKind = new GeneralKind();
		quantity = 1;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GeneralKind getgKind() {
		return gKind;
	}

	public void setgKind(GeneralKind gKind) {
		this.gKind = gKind;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Barcode_itemBean [barcode=" + barcode + ", description="
				+ description + ", quantity=" + quantity + ", gKind=" + gKind
				+ "]";
	}

	

}
