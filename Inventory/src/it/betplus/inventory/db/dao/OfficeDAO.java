package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Office;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class OfficeDAO {

	protected static Logger log = Logger.getLogger(OfficeDAO.class);

	private static OfficeDAO instance;

	private OfficeDAO() {
		super();
	}

	public static synchronized OfficeDAO getInstance() {
		if (instance == null) {
			synchronized (OfficeDAO.class) {
				instance = new OfficeDAO();
			}
		}
		return instance;
	}
	
	public List<Office> getOffices(int mapId) throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Office off = null;
		List<Office> alloffice = new ArrayList<Office>();

		
		try {
			connInventory =  DBConnectionManager.inventoryConnectionFactory();

			String sql = "select o.* from office o where id_map = ? order by pos";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1,mapId);
			rs = ps.executeQuery();

			while (rs.next()) {
				off = new Office();
				off.setId(rs.getInt("id"));
				off.setName(rs.getString("name"));
				off.setId_map(rs.getInt("id_map"));
				off.setPos(rs.getInt("pos"));

				alloffice.add(off);
			}

			ps.close();
			rs.close();
			
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getOffices: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alloffice;

	}

	public List<Office> getOffices() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Office off = null;
		List<Office> alloffice = new ArrayList<Office>();

		
		try {
			connInventory =  DBConnectionManager.inventoryConnectionFactory();

			String sql = "select o.* from office o";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				off = new Office();
				off.setId(rs.getInt("id"));
				off.setName(rs.getString("name"));
				off.setId_map(rs.getInt("id_map"));
				off.setPos(rs.getInt("pos"));

				alloffice.add(off);
			}

			ps.close();
			rs.close();
			
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getOffices: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alloffice;

	}
	
	public int insertNewOffice(Office o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!exsistOffice(o.getName())) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				String sql = "insert into office(name,id_map,pos) values(?,?,?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, o.getName());
				ps.setInt(2,o.getId_map());
				ps.setInt(3, o.getPos());
				

				risultato = ps.executeUpdate();

				if (risultato == 1)
					connInventory.commit();
				else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertNewOffice: SQL failed", e);
				e.printStackTrace();
				throw new DataAccessException(e.toString(), e);
			} catch (RuntimeException e) {
				throw new DataAccessException(e.toString());
			} catch (DataLayerException e) {
				e.printStackTrace();
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return risultato;
	}

	public boolean exsistOffice(String name) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select name from office where name = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, name);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("exsistOffice: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return esiste;
	}
	
	public boolean usedOfficeIntoItem_pc(Office o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean used = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select count(office) from general_items where office = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, o.getId());

			rs = ps.executeQuery();

			if (rs.next())
				if(rs.getInt(1)>0)
					used=true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("usedOfficeIntoItem_pc: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return used;
	}

	public int modifyOffice(Office o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "update office set name=?,id_map=?,pos=? where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, o.getName());
			ps.setInt(2, o.getId_map());
			ps.setInt(3,o.getPos());
			ps.setInt(4, o.getId());

			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("modifyOffice: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;
	}

	public int deleteOffice(Office o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if(!usedOfficeIntoItem_pc(o)){		
		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "delete from office where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, o.getId());
			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("deleteOffice: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
		else
			risultato = -1;
		
		return risultato;
	}

}