package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Owner;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class OwnerDAO {

	protected static Logger log = Logger.getLogger(OwnerDAO.class);

	private static OwnerDAO instance;

	private OwnerDAO() {
		super();
	}

	public static synchronized OwnerDAO getInstance() {
		if (instance == null) {
			synchronized (OwnerDAO.class) {
				instance = new OwnerDAO();
			}
		}
		return instance;
	}

	public List<Owner> getOwners() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Owner own = null;
		List<Owner> allowner = new ArrayList<Owner>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select o.* from owner o";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				own = new Owner();
				own.setId(rs.getInt("id"));
				own.setName(rs.getString("name"));
				own.setSurname(rs.getString("surname"));
				own.setEmail(rs.getString("email"));
				allowner.add(own);
			}
			
			

			ps.close();
			rs.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getOwners: SQL failed", e);
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
		return allowner;

	}
	
	
	
	public int insertNewOwner(Owner o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!exsistOwner(o.getName(), o.getSurname())) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				String sql = "insert into owner(name,surname,email) values(?,?,?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, o.getName());
				ps.setString(2, o.getSurname());
				ps.setString(3, o.getEmail());
				
				risultato = ps.executeUpdate();

				if (risultato == 1)
					connInventory.commit();
				else
					connInventory.rollback();

				ps.close();
				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertNewOwner: SQL failed", e);
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

	public boolean exsistOwner(String name, String surname)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select name from owner where name = ? and surname= ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, surname);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			ps.close();
			rs.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("exsistOwner: SQL failed", e);
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

	public int modifyOwner(Owner o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "update owner set name=?, surname=?, email=? where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, o.getName());
			ps.setString(2, o.getSurname());
			ps.setString(3, o.getEmail());
			ps.setInt(4, o.getId());

			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			ps.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("modifyOwner: SQL failed", e);
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

	
	public int deleteOwner(Owner o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if(!usedOwnerIntoItem_pc(o)){		
		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "delete from owner where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, o.getId());
			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("deleteOwner: SQL failed", e);
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
	
	public boolean usedOwnerIntoItem_pc(Owner o) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean used = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select count(owner_id) from general_items g inner join used u on u.item_id = g.id  and u.owner_id = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, o.getId());

			rs = ps.executeQuery();

			if (rs.next())
				if(rs.getInt(1)>0)
					used=true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("usedOwnerIntoItem_pc: SQL failed", e);
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
}