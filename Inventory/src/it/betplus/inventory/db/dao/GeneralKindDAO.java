package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.GeneralKind;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class GeneralKindDAO {

	protected static Logger log = Logger.getLogger(GeneralKindDAO.class);

	private static GeneralKindDAO instance;

	private GeneralKindDAO() {
		super();
	}

	public static synchronized GeneralKindDAO getInstance() {
		if (instance == null) {
			synchronized (GeneralKindDAO.class) {
				instance = new GeneralKindDAO();
			}
		}
		return instance;
	}

	public List<GeneralKind> getGeneralKinds() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		GeneralKind g = null;
		List<GeneralKind> allgeneralKind = new ArrayList<GeneralKind>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select gk.id gkid,gk.cod gkcod,gk.description gkdesc,gk.counter gkcounter"
					+ " from general_kinds gk ";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				g = new GeneralKind();
				g.setId(rs.getInt("gkid"));
				g.setCod(rs.getString("gkcod"));
				g.setDescription(rs.getString("gkdesc"));
				g.setCounter(rs.getInt("gkcounter"));

				allgeneralKind.add(g);
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getGeneralKinds: SQL failed", e);
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
		return allgeneralKind;
	}

	public int insertGeneralKind(GeneralKind gk) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!existGeneralKind(gk.getCod())) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				String sql = "insert into general_kinds(cod,description) values(?,?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, gk.getCod());
				ps.setString(2, gk.getDescription());

				risultato = ps.executeUpdate();

				if (risultato == 1)
					connInventory.commit();
				else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertGeneralKind: SQL failed", e);
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
					"Tipo genrico gia presente nel Database");
		}
		return risultato;
	}

	public boolean existGeneralKind(String code) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select code from general_kins where code = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, code);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("existGeneralKind: SQL failed", e);
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

	public boolean isUsedGeneralKind(String cod) throws DataAccessException {
		boolean result = false;
		List<Barcode_item> usedBarcodes = Barcode_itemDAO.getInstance()
				.getUsedBarcodes();

		for (Barcode_item b : usedBarcodes) {
			if (b.getgKind().getCod().equals(cod))
				return true;
		}

		return result;
	}

	public int deleteGeneralKind(String cod) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!isUsedGeneralKind(cod)) {
			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();
				connInventory.setAutoCommit(false);

				String sql = "delete from general_items where cod=?";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, cod);
				risultato = ps.executeUpdate();

				if (risultato > 0)
					connInventory.commit();
				else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("deleteGeneralKind: SQL failed", e);
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
					"Impossibile Cancellare Tipo Generico in uso");
		}

		return risultato;
	}

	public void increaseCounterGeneralKind(Connection connInventory, int gid)
			throws DataAccessException, DataLayerException {

		try {

			CallableStatement cs = connInventory
					.prepareCall("{ CALL increase_general_kind_counter(?) }");

			cs.setInt(1, gid);
			cs.execute();

			cs.close();
		} catch (SQLException e) {
			log.error("increaseCounterGeneralKind: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}

	}

	public void decreaseCounterGeneralKind(Connection connInventory, int gid)
			throws DataAccessException, DataLayerException {

		try {

			CallableStatement cs = connInventory
					.prepareCall("{ CALL decrease_general_kind_counter(?) }");

			cs.setInt(1, gid);
			cs.execute();

			cs.close();
		} catch (SQLException e) {
			log.error("increaseCounterGeneralKind: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}

	}

}
