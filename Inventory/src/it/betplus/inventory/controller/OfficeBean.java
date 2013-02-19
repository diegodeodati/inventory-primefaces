package it.betplus.inventory.controller;

import it.betplus.web.framework.managedbeans.GeneralBean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "officeBean")
@RequestScoped
public class OfficeBean extends GeneralBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7045222261760534726L;
	int id;
	String name;
	int id_map;
	int pos;

	public OfficeBean(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.id_map = 0;
		this.pos = 0;
	}

	public OfficeBean() {
		id = 0;
		name = "";
		id_map = 0;
		pos = 0;
		// TODO Auto-generated constructor stub
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
		OfficeBean other = (OfficeBean) obj;
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
		return "OfficeBean [id=" + id + ", name=" + name + ", id_map=" + id_map
				+ ", pos=" + pos + "]";
	}

}
