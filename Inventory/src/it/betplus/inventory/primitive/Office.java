package it.betplus.inventory.primitive;

import java.io.Serializable;

public class Office implements Serializable {

	
	private static final long serialVersionUID = 7045222261760534726L;
	int id;
	String name;
	int id_map;
	int pos;

	public Office(String name) {
		super();
		this.name = name;
	}

	public Office() {
		id = 0;
		name = "";
	}

	public Office(int id, String name, int id_map, int pos) {
		super();
		this.id = id;
		this.name = name;
		this.id_map = id_map;
		this.pos = pos;
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

	public int getId_map() {
		return id_map;
	}

	public void setId_map(int id_map) {
		this.id_map = id_map;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + id_map;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + pos;
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
		Office other = (Office) obj;
		if (id != other.id)
			return false;
		if (id_map != other.id_map)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pos != other.pos)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Office [id=" + id + ", name=" + name + ", id_map=" + id_map
				+ ", pos=" + pos + "]";
	}

}
