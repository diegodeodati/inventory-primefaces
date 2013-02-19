package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.GeneralKind;
import it.betplus.inventory.primitive.Kind;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class KindDAO {

	protected static Logger log = Logger.getLogger(KindDAO.class);

	private static KindDAO instance;

	private KindDAO() {
		super();
	}

	public static synchronized KindDAO getInstance() {
		if (instance == null) {
			synchronized (KindDAO.class) {
				instance = new KindDAO();
			}
		}
		return instance;
	}

	public List<Kind> getKinds() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Kind k = null;
		List<Kind> allkind = new ArrayList<Kind>();

		
		try {
			connInventory =  DBConnectionManager.inventoryConnectionFactory();

			String sql = "select k.*,gk.id gkid,gk.cod gkcod,gk.description gkdesc,gk.counter gkcounter" +
					" from items_kinds k inner join general_kinds gk on k.id_general_kinds=gk.id";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				k = new Kind();
				k.setId(rs.getInt("id"));
				k.setName(rs.getString("name"));
				k.setNet_reachable(rs.getBoolean("net_reachable"));
				
				GeneralKind g = new GeneralKind();
				g.setId(rs.getInt("gkid"));
				g.setCod(rs.getString("gkcod"));
				g.setDescription(rs.getString("gkdesc"));
				g.setCounter(rs.getInt("gkcounter"));
				
				k.setgKind(g);

				allkind.add(k);
			}

			ps.close();
			rs.close();
			
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getKinds: SQL failed", e);
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
		return allkind;
	}
}
