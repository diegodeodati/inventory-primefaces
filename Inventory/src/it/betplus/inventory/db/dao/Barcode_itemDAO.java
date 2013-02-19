package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.GeneralKind;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Barcode_itemDAO {

	protected static Logger log = Logger.getLogger(UserDAO.class);

	private static Barcode_itemDAO instance;

	private Barcode_itemDAO() {
		super();
	}

	public static synchronized Barcode_itemDAO getInstance() {
		if (instance == null) {
			synchronized (Barcode_itemDAO.class) {
				instance = new Barcode_itemDAO();
			}
		}
		return instance;
	}

	public List<Barcode_item> getBarcodes() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Barcode_item barcode = null;
		List<Barcode_item> allBarcode = new ArrayList<Barcode_item>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select distinct x.*,gk.id gkid,gk.cod gkcod,gk.description gkdesc,gk.counter gkcounter,IF((gi.id is not null && gi.id>0),1,0) used from "
					+ "(select distinct b.description, b.barcode,b.id_general_kinds from barcode_items b) x "
					+ "inner join general_kinds gk on x.id_general_kinds=gk.id "
					+ "left join general_items gi on gi.barcode = x.barcode";

			/*
			 * "select b.barcode,b.description,gk.id gkid,gk.cod gkcod,gk.description gkdesc,gk.counter gkcounter "
			 * +
			 * "from barcode_items b inner join general_kinds gk on b.id_general_kinds=gk.id"
			 * ;
			 */

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				barcode = new Barcode_item();
				barcode.setBarcode(rs.getString("barcode"));
				barcode.setDescription(rs.getString("description"));
				barcode.setUsed(rs.getBoolean("used"));

				GeneralKind g = new GeneralKind();
				g.setId(rs.getInt("gkid"));
				g.setCod(rs.getString("gkcod"));
				g.setDescription(rs.getString("gkdesc"));
				g.setCounter(rs.getInt("gkcounter"));

				barcode.setgKind(g);

				allBarcode.add(barcode);
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getBarcodes: SQL failed", e);
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
		return allBarcode;

	}

	public List<Barcode_item> getUsedBarcodes() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Barcode_item barcode = null;
		List<Barcode_item> allBarcode = new ArrayList<Barcode_item>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select * from ( "
					+ "select distinct x.*,gk.id gkid,gk.cod gkcod,gk.description gkdesc,gk.counter gkcounter,IF((gi.id is not null && gi.id>0),1,0) used from "
					+ "(select distinct b.description, b.barcode,b.id_general_kinds from barcode_items b) x "
					+ "inner join general_kinds gk on x.id_general_kinds=gk.id "
					+ "left join general_items gi on gi.barcode = x.barcode "
					+ ")x where used > 0";

			/*
			 * "select b.barcode,b.description,IF((g.id is not null && g.id>0),1,0) used,"
			 * +
			 * "gk.id gkid,gk.cod gkcod,gk.description gkdesc,gk.counter gkcounter from barcode_items b "
			 * +
			 * "left join general_items g on b.barcode = g.barcode left join general_kinds gk on b.id_general_kinds=gk.id "
			 * ;
			 */

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				barcode = new Barcode_item();
				barcode.setBarcode(rs.getString("barcode"));
				barcode.setDescription(rs.getString("description"));
				barcode.setUsed(true);

				GeneralKind g = new GeneralKind();
				g.setId(rs.getInt("gkid"));
				g.setCod(rs.getString("gkcod"));
				g.setDescription(rs.getString("gkdesc"));
				g.setCounter(rs.getInt("gkcounter"));

				barcode.setgKind(g);

				allBarcode.add(barcode);
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getUsedBarcode: SQL failed", e);
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
		return allBarcode;

	}

	public int insertNewBarcode(Barcode_item b) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!existBarcode(b.getBarcode())) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				String sql = "insert into barcode_items(barcode,description,id_general_kinds) values(?,?,?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, b.getBarcode());
				ps.setString(2, b.getDescription());
				ps.setInt(3, b.getgKind().getId());

				risultato = ps.executeUpdate();

				if (risultato == 1) {
					GeneralKindDAO.getInstance().increaseCounterGeneralKind(
							connInventory, b.getgKind().getId());
					connInventory.commit();
				} else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertNewBarcode: SQL failed", e);
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
		} else {
			throw new DataLayerException("Barcode gia presente nel Database");
		}
		return risultato;
	}

	public boolean existBarcode(String barcode) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select barcode from barcode_items where barcode = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, barcode);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("existBarcode: SQL failed", e);
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

	/*
	 * public int modifyBarcode(Barcode_item b) throws DataAccessException {
	 * PreparedStatement ps = null; Connection connInventory = null; int
	 * risultato = 0;
	 * 
	 * try { connInventory = DBConnectionManager.inventoryConnectionFactory();
	 * connInventory.setAutoCommit(false);
	 * 
	 * String sql = "update barcode_items set description=? where barcode=?";
	 * 
	 * ps = connInventory.prepareStatement(sql); ps.setString(1,
	 * b.getDescription()); ps.setString(2, b.getBarcode());
	 * 
	 * risultato = ps.executeUpdate();
	 * 
	 * if (risultato > 0) connInventory.commit(); else connInventory.rollback();
	 * 
	 * DBConnectionManager.CloseConnection(connInventory);
	 * 
	 * } catch (SQLException e) { log.error("modificaBarcode: SQL failed", e);
	 * e.printStackTrace(); throw new DataAccessException(e.toString(), e); }
	 * catch (RuntimeException e) { throw new DataAccessException(e.toString());
	 * } catch (DataLayerException e) { e.printStackTrace(); } finally { try {
	 * ps.close(); } catch (SQLException e) { e.printStackTrace(); } } return
	 * risultato; }
	 */

	public boolean isUsedBarcode(String barcode) throws DataAccessException {
		boolean result = false;

		List<Barcode_item> usedBarcodes = getUsedBarcodes();

		for (Barcode_item b : usedBarcodes) {
			if (b.getBarcode().equals(barcode))
				if (b.isUsed())
					return true;
				else
					return false;
		}

		return result;
	}

	public int deleteBarcode(Barcode_item b) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!isUsedBarcode(b.getBarcode())) {
			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();
				connInventory.setAutoCommit(false);

				String sql = "delete from barcode_items where barcode=?";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, b.getBarcode());
				risultato = ps.executeUpdate();

				if (risultato > 0) {
					GeneralKindDAO.getInstance().decreaseCounterGeneralKind(
							connInventory, b.getgKind().getId());
					connInventory.commit();
				} else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("deleteBarcode: SQL failed", e);
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
		} else {
			throw new DataLayerException(
					"Impossibile Cancellare Barcode in uso");
		}

		return risultato;
	}
	/*
	 * public boolean usedBarcodeIntoItem_pc(Office o) throws
	 * DataAccessException { PreparedStatement ps = null; Connection
	 * connInventory = null; ResultSet rs = null; boolean used = false;
	 * 
	 * try { connInventory = DBConnectionManager.inventoryConnectionFactory();
	 * 
	 * String sql = "select count(barcode) from items_pc where barcode = ?";
	 * 
	 * ps = connInventory.prepareStatement(sql); ps.setInt(1, o.getId());
	 * 
	 * rs = ps.executeQuery();
	 * 
	 * if (rs.next()) if (rs.getInt(1) > 0) used = true;
	 * 
	 * DBConnectionManager.CloseConnection(connInventory);
	 * 
	 * } catch (SQLException e) {
	 * log.error("usedBarcodeIntoItem_pc: SQL failed", e); e.printStackTrace();
	 * throw new DataAccessException(e.toString(), e); } catch (RuntimeException
	 * e) { throw new DataAccessException(e.toString()); } catch
	 * (DataLayerException e) { e.printStackTrace(); } finally { try {
	 * rs.close(); ps.close(); } catch (SQLException e) { e.printStackTrace(); }
	 * } return used; }
	 */

}
