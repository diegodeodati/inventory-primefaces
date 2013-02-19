package it.betplus.inventory.primitive;

public class Map {

	int id;
	int floor;
	String build;
	String nameFile;
	String nameMap;

	public Map() {
		this.id = 0;
		this.floor = 0;
		this.build = "none";
		this.nameFile = "none.xml";
		this.nameMap = "none";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getNameMap() {
		
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		String piano = nameFile.substring(3,4)+"° Piano ";
		
		String palazzina = " - Palazzina "+alphabet[Integer.parseInt(nameFile.substring(4).replace(".xml",""))-1];
		
		nameMap = piano + palazzina; 
		
		return nameMap;
	}

	public void setNameMap(String nameMap) {
		this.nameMap = nameMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((build == null) ? 0 : build.hashCode());
		result = prime * result + floor;
		result = prime * result + id;
		result = prime * result
				+ ((nameFile == null) ? 0 : nameFile.hashCode());
		result = prime * result + ((nameMap == null) ? 0 : nameMap.hashCode());
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
		Map other = (Map) obj;
		if (build == null) {
			if (other.build != null)
				return false;
		} else if (!build.equals(other.build))
			return false;
		if (floor != other.floor)
			return false;
		if (id != other.id)
			return false;
		if (nameFile == null) {
			if (other.nameFile != null)
				return false;
		} else if (!nameFile.equals(other.nameFile))
			return false;
		if (nameMap == null) {
			if (other.nameMap != null)
				return false;
		} else if (!nameMap.equals(other.nameMap))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Map [id=" + id + ", floor=" + floor + ", build=" + build
				+ ", nameFile=" + nameFile + ", nameMap=" + nameMap + "]";
	}

	public static void main(String[] args){
		Map m = new Map();
		m.setNameFile("map21.xml");
		
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		System.out.println( m.getNameFile().substring(0,3));
		
		System.out.println(m.getNameFile().substring(3,4));
		
		System.out.println(alphabet[Integer.parseInt(m.getNameFile().substring(4).replace(".xml",""))-1]);
	}
	

}
