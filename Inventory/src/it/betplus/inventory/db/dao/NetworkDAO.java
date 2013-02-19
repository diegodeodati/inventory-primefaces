package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Network;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class NetworkDAO {

	protected static Logger log = Logger.getLogger(UserDAO.class);

	private static NetworkDAO instance;

	private NetworkDAO() {
		super();
	}

	public static synchronized NetworkDAO getInstance() {
		if (instance == null) {
			synchronized (NetworkDAO.class) {
				instance = new NetworkDAO();
			}
		}
		return instance;
	}

	public List<Network> getNetworks() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Network net = null;
		List<Network> allnetwork = new ArrayList<Network>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select n.* from network n";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				net = new Network();
				net.setId(rs.getInt("id"));
				net.setDescription(rs.getString("description"));
				net.setMac_address(rs.getString("mac_address"));
				net.setIp_address(rs.getString("ip_address"));
				net.setId_pc(rs.getInt("id_item_pc"));

				allnetwork.add(net);
			}

			ps.close();
			rs.close();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getNetworks: SQL failed", e);
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
		return allnetwork;

	}

	
	public List<Network> getNetworks(int id_item_pc) throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Network net = null;
		List<Network> allnetwork = new ArrayList<Network>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select n.* from network n where id_item_pc";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				net = new Network();
				net.setId(rs.getInt("id"));
				net.setDescription(rs.getString("description"));
				net.setMac_address(rs.getString("mac_address"));
				net.setIp_address(rs.getString("ip_address"));
				net.setId_pc(rs.getInt("id_item_pc"));

				allnetwork.add(net);
			}

			ps.close();
			rs.close();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getNetworks: SQL failed", e);
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
		return allnetwork;

	}
	
	public int insertNewNetwrok(Network n) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!exsistNetwork(n)) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				String sql = "insert into network(description,mac_address,ip_address,id_item_pc) values(?,?,?,?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, n.getDescription());
				ps.setString(2, n.getMac_address());
				ps.setString(3, n.getIp_address());
				ps.setInt(4, n.getId_pc());

				risultato = ps.executeUpdate();

				if (risultato == 1)
					connInventory.commit();
				else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertNewNetwrok: SQL failed", e);
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
			risultato = -1;
		}
		return risultato;
	}

	public boolean exsistNetwork(Network net) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			if (net.getId() > 0) {
				String sql = "select id from network where (mac_address = ? or ip_address = ?) and id<>?";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, net.getMac_address());
				ps.setString(2, net.getIp_address());
				ps.setInt(3, net.getId());
			} else {
				String sql = "select id from network where (mac_address = ? or ip_address = ?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, net.getMac_address());
				ps.setString(2, net.getIp_address());
			}

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("exsistNetwork: SQL failed", e);
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

	public int modifyNetwork(Network n) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!exsistNetwork(n)) {
			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();
				connInventory.setAutoCommit(false);

				String sql = "update network set description=?,mac_address=?,ip_address=?,id_item_pc=? where id=?";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, n.getDescription());
				ps.setString(2, n.getMac_address());
				ps.setString(3, n.getIp_address());
				ps.setInt(4, n.getId());

				risultato = ps.executeUpdate();

				if (risultato > 0)
					connInventory.commit();
				else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("modifyNetwork: SQL failed", e);
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
			risultato = -1;
		}
		return risultato;
	}

	public int deleteNetwork(Network n) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "delete from network where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, n.getId());
			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("deleteNetwork: SQL failed", e);
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

}
