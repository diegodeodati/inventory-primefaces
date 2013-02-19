package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Component;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ComponentDAO {

	protected static Logger log = Logger.getLogger(UserDAO.class);

	private static ComponentDAO instance;

	private ComponentDAO() {
		super();
	}

	public static synchronized ComponentDAO getInstance() {
		if (instance == null) {
			synchronized (ComponentDAO.class) {
				instance = new ComponentDAO();
			}
		}
		return instance;
	}

	public int findComponentId(String mac_address) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		int comp_id = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select comp_id from registry_system r "
					+ "where r.value = ? " + "and r.parameter = 'MAC-address' and r.status = 1";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, mac_address);

			rs = ps.executeQuery();

			if (rs.next())
				comp_id = rs.getInt("comp_id");

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("findComponentId: SQL failed", e);
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
		return comp_id;
	}

	public List<Component> getComponents(int comp_id)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		List<Component> compList = new ArrayList<Component>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select r.* from registry_system r "
					+ "where r.comp_id = ? and r.status = 1 and r.parameter REGEXP 'HostName|IP-address|System|Motherboard|Processor|Bank|Disk|Video|MAC-address'";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, comp_id);

			rs = ps.executeQuery();

			Component c;
			while (rs.next()) {
				c = new Component();
				c.setId(rs.getInt("id"));
				c.setName(rs.getString("parameter"));
				c.setDetails(rs.getString("value"));
				compList.add(c);
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getComponents: SQL failed", e);
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
		return compList;
	}
}
