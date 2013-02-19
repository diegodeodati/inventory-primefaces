package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Map;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MapDAO {
	
	protected static Logger log = Logger.getLogger(UserDAO.class);

	private static MapDAO instance;

	private MapDAO() {
		super();
	}

	public static synchronized MapDAO getInstance() {
		if (instance == null) {
			synchronized (MapDAO.class) {
				instance = new MapDAO();
			}
		}
		return instance;
	}
	
	public List<Map> getMaps() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Map map = null;
		List<Map> allmap = new ArrayList<Map>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select m.* from maps m";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				map = new Map();
				map.setId(rs.getInt("id"));
				map.setFloor(rs.getInt("floor"));
				map.setBuild(rs.getString("build"));
				map.setNameFile(rs.getString("nameFile"));

				allmap.add(map);
			}

			ps.close();
			rs.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getMaps: SQL failed", e);
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
		return allmap;

	}

}
