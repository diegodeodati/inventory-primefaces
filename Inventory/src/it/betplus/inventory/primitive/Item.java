package it.betplus.inventory.primitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

	private static final long serialVersionUID = -645418425327798507L;

	int id;
	String description;
	Office office;
	Owner owner;
	Barcode_item barcode;
	Kind kind;
	String brand;
	int quantity;
	int uid;
	boolean net_discovered;
	List<Network> netList;
	List<Component> componentList;

	public Item() {
		this.id = 0;
		this.owner = new Owner();
		this.barcode = new Barcode_item();
		this.office = new Office();
		this.kind = new Kind();
		this.netList = new ArrayList<Network>();
		this.componentList = new ArrayList<Component>();
	}

	public Item(int id, String description) {
		super();
		this.id = id;
		this.description = description;
		this.barcode = new Barcode_item();
		this.owner = new Owner();
		this.barcode = new Barcode_item();
		this.kind = new Kind();
		this.netList = new ArrayList<Network>();
		this.componentList = new ArrayList<Component>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		if(id!=0)
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Office getOffice() {
		if (office != null)
			return office;
		else
			return new Office();
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Owner getOwner() {
		if (owner != null)
			return owner;
		else
			return new Owner();
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Barcode_item getBarcode() {
		if (barcode != null)
			return barcode;
		else
			return new Barcode_item();
	}

	public void setBarcode(Barcode_item barcode) {
		this.barcode = barcode;
	}

	public Kind getKind() {
		if (kind != null)
			return kind;
		else
			return new Kind();
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public boolean isNet_discovered() {
		return net_discovered;
	}

	public void setNet_discovered(boolean net_discovered) {
		this.net_discovered = net_discovered;
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
		if (quantity > 0)
			this.quantity = quantity;
		else
			this.quantity = 1;
	}

	public List<Network> getNetList() {
		return netList;
	}

	public void setNetList(List<Network> netList) {
		this.netList = netList;
	}

	public List<Component> getComponentList() {
		return componentList;
	}

	public void setComponentList(List<Component> componentList) {
		this.componentList = componentList;
	}

	public void addToNetList(Network n) {
		netList.add(n);
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
		Item other = (Item) obj;
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
		return "Item [id=" + id + ", description=" + description + ", office="
				+ office + ", owner=" + owner + ", barcode=" + barcode
				+ ", brand=" + brand + ", quantity=" + quantity + ", netList="
				+ netList + "]";
	}

}
