package it.betplus.inventory.primitive;

import java.io.Serializable;

public class Network implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7818459820530295257L;
	int id;
	String description;
	String mac_address;
	String ip_address;
	int id_pc;

	public Network(int id, String description, String mac_address,
			String ip_address, int id_pc) {
		super();
		this.id = id;
		this.description = description;
		this.mac_address = mac_address;
		this.ip_address = ip_address;
		this.id_pc = id_pc;
	}

	public Network() {
		super();
		this.id = 0;
		this.description = "";
		this.mac_address = "";
		this.ip_address = "";
		this.id_pc = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMac_address() {
		return mac_address;
	}

	public void setMac_address(String mac_address) {
		this.mac_address = mac_address;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public int getId_pc() {
		return id_pc;
	}

	public void setId_pc(int id_pc) {
		this.id_pc = id_pc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + id_pc;
		result = prime * result
				+ ((ip_address == null) ? 0 : ip_address.hashCode());
		result = prime * result
				+ ((mac_address == null) ? 0 : mac_address.hashCode());
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
		Network other = (Network) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (id_pc != other.id_pc)
			return false;
		if (ip_address == null) {
			if (other.ip_address != null)
				return false;
		} else if (!ip_address.equals(other.ip_address))
			return false;
		if (mac_address == null) {
			if (other.mac_address != null)
				return false;
		} else if (!mac_address.equals(other.mac_address))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Network [id=" + id + ", description=" + description
				+ ", mac_address=" + mac_address + ", ip_address=" + ip_address
				+ ", id_pc=" + id_pc + "]";
	}

}
