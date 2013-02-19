package it.betplus.inventory.primitive;

import java.io.Serializable;

public class Kind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355591442132103885L;
	int id;
	String name;
	boolean net_reachable;
	GeneralKind gKind;

	public Kind() {
		this.id = 0;
		this.name = "";
		this.net_reachable = false;
		this.gKind = new GeneralKind();
	}

	public Kind(int id, String name, boolean net_reachable,GeneralKind gKind) {
		super();
		this.id = id;
		this.name = name;
		this.net_reachable = net_reachable;
		this.gKind = gKind;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		if (name != null)
			if (!name.equals(""))
				return name;
			else
				return "";
		else
			return "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNet_reachable() {
		return net_reachable;
	}

	public void setNet_reachable(boolean net_reachable) {
		this.net_reachable = net_reachable;
	}

	public GeneralKind getgKind() {
		return gKind;
	}

	public void setgKind(GeneralKind gKind) {
		this.gKind = gKind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (net_reachable ? 1231 : 1237);
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
		Kind other = (Kind) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (net_reachable != other.net_reachable)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kind [id=" + id + ", name=" + name + ", net_reachable="
				+ net_reachable + "]";
	}

}
