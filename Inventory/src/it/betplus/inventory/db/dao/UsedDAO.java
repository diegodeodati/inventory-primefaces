package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Item;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class UsedDAO {

	protected static Logger log = Logger.getLogger(UsedDAO.class);

	private static UsedDAO instance;

	private UsedDAO() {
		super();
	}

	public static synchronized UsedDAO getInstance() {
		if (instance == null) {
			synchronized (UsedDAO.class) {
				instance = new UsedDAO();
			}
		}
		return instance;
	}

	public int moveUsedTo(Connection connInventory, Item iFrom, Item iTo)
			throws DataAccessException, DataLayerException {
		PreparedStatement ps = null;
		int risultato = 0;

		try {

			String sql = "update used set quantity=quantity-? where item_id=? and office_id=? and owner_id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, iTo.getQuantity());
			ps.setInt(2, iFrom.getId());
			ps.setInt(3, iFrom.getOffice().getId());
			ps.setInt(4, iFrom.getOwner().getId());
			
			risultato = ps.executeUpdate();

			if (risultato > 0)
				cleanNoQuantityUsed(connInventory);

			sql = "insert into used(office_id,item_id,owner_id,quantity) values (?,?,?,?) on duplicate key UPDATE quantity = quantity+?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, iTo.getOffice().getId());
			ps.setInt(2, iFrom.getId());
			ps.setInt(3, iTo.getOwner().getId());
			ps.setInt(4, iTo.getQuantity());
			ps.setInt(5, iTo.getQuantity());

			risultato = ps.executeUpdate();

		} catch (SQLException e) {
			log.error("moveUsedTo: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			throw new DataLayerException(e.toString());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return risultato;

	}

	public int modifyUsed(Connection connInventory, Item i)
			throws DataLayerException, DataAccessException {
		PreparedStatement ps = null;
		int risultato = 0;

		try {

			String sql = "update used set office_id=?,owner_id=?,quantity=? where item_id=? and office_id=? and owner_id=?";

			ps = connInventory.prepareStatement(sql);

			ps.setInt(1, i.getOffice().getId());
			ps.setInt(2, i.getOwner().getId());
			ps.setInt(3, i.getQuantity());
			ps.setInt(4, i.getId());
			ps.setInt(5, i.getOffice().getId());
			ps.setInt(6, i.getOwner().getId());
			risultato = ps.executeUpdate();
			
			cleanNoQuantityUsed(connInventory);

		} catch (SQLException e) {
			log.error("modifyUsed: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			throw new DataLayerException(e.toString());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;
	}

	public boolean existUsedItem(Connection connInventory, Item i)
			throws DataAccessException, DataLayerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {

			String sql = "select count(item_id) quantity_used from used where item_id = ? and (office_id is not null and office_id!=0)";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, i.getId());

			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getInt("quantity_used") > 0)
					esiste = true;
			}

		} catch (SQLException e) {
			log.error("existUsedItem: SQL failed", e);
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
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

	public int decreaseUsed(Item i) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "update used set quantity=quantity-1 where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, i.getUid());
			risultato = ps.executeUpdate();

			if (risultato > 0)
				cleanNoQuantityUsed(connInventory);

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("deleteUsed: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			throw new DataLayerException(e.toString());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;
	}

	public int insertUsed(Connection connInventory, Item i)
			throws DataAccessException, DataLayerException {
		PreparedStatement ps = null;
		int risultato = 0;

		try {

			String sql = "insert into used(office_id,item_id,owner_id,quantity) values (?,?,?,?) on duplicate key UPDATE quantity = quantity+1";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, i.getOffice().getId());
			ps.setInt(2, i.getId());
			ps.setInt(3, i.getOwner().getId());
			ps.setInt(4, i.getQuantity());
			risultato = ps.executeUpdate();

		} catch (SQLException e) {
			log.error("insertUsed: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			throw new DataLayerException(e.toString());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;
	}

	public void cleanNoQuantityUsed(Connection connInventory)
			throws DataAccessException, DataLayerException {

		try {

			CallableStatement cs = connInventory
					.prepareCall("{ CALL clean_noquantity_used }");

			cs.execute();

			cs.close();
		} catch (SQLException e) {
			log.error("deleteUsed: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}

	}
}
