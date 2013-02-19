package it.betplus.inventory.controller;

import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.Item;
import it.betplus.inventory.primitive.Kind;
import it.betplus.inventory.primitive.Network;
import it.betplus.inventory.primitive.Office;
import it.betplus.inventory.primitive.Owner;
import it.betplus.web.framework.managedbeans.GeneralBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "itemBean")
@RequestScoped
public class ItemBean extends GeneralBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8875629453181705508L;

	int id;
	Item internalItem;
	String description;
	Office office;
	Owner owner;
	Barcode_item barcode;
	Kind kind;
	String brand;
	int quantity;
	List<Network> netList = new ArrayList<Network>();

	public ItemBean() {
		internalItem = null;
		office = new Office();
		barcode = new Barcode_item();
		owner = new Owner();
		quantity=1;
	}

	public ItemBean(int id, String description, Office office, Owner owner,
			Barcode_item barcode, Kind kind, String brand, int quantity,
			List<Network> netList) {
		super();
		this.id = id;
		this.internalItem = null;
		this.description = description;
		this.office = office;
		this.owner = owner;
		this.barcode = barcode;
		this.kind = kind;
		this.brand = brand;
		this.quantity = quantity;
		this.netList = netList;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		if (id > 0)
			this.id = id;
	}

	public Item getInternalItem() {
		return internalItem;
	}

	public void setInternalItem(Item internalItem) {
		this.internalItem = internalItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		if (office != null)
			this.office = office;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		if (owner != null)
			this.owner = owner;
	}

	public Barcode_item getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode_item barcode) {
		if (barcode != null)
			this.barcode = barcode;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		if (kind != null)
			this.kind = kind;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<Network> getNetList() {
		return netList;
	}

	public void setNetList(List<Network> netList) {
		this.netList = netList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((netList == null) ? 0 : netList.hashCode());
		result = prime * result + ((office == null) ? 0 : office.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		ItemBean other = (ItemBean) obj;
		if (barcode == null) {
			if (other.barcode != null)
				return false;
		} else if (!barcode.equals(other.barcode))
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (kind == null) {
			if (other.kind != null)
				return false;
		} else if (!kind.equals(other.kind))
			return false;
		if (netList == null) {
			if (other.netList != null)
				return false;
		} else if (!netList.equals(other.netList))
			return false;
		if (office == null) {
			if (other.office != null)
				return false;
		} else if (!office.equals(other.office))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemBean [id=" + id + ", description=" + description
				+ ", office=" + office + ", owner=" + owner + ", barcode="
				+ barcode + ", kind=" + kind + ", brand=" + brand
				+ ", quantity=" + quantity + ", netList=" + netList + "]";
	}

}
