package it.betplus.inventory.primitive;

import java.io.Serializable;

public class Component implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3036330640438847275L;
	int id;
	String name;
	String details;
	
	public Component(){
		this.id=0;
		this.name="";
		this.details="";
	}
	
	public Component(int id, String name, String details) {
		super();
		this.id = id;
		this.name = name;
		this.details = details;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((details == null) ? 0 : details.hashCode());
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
		Component other = (Component) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (details == null) {
			if (other.details != null)
				return false;
		} else if (!details.equals(other.details))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Component [id=" + id + ", name=" + name + ", details="
				+ details + "]";
	}
	
	

}
