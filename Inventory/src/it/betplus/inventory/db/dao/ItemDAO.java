package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.GeneralKind;
import it.betplus.inventory.primitive.Item;
import it.betplus.inventory.primitive.Kind;
import it.betplus.inventory.primitive.Network;
import it.betplus.inventory.primitive.Office;
import it.betplus.inventory.primitive.Owner;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ItemDAO {

	protected static Logger log = Logger.getLogger(ItemDAO.class);

	private static ItemDAO instance;

	private ItemDAO() {
		super();
	}

	public static synchronized ItemDAO getInstance() {
		if (instance == null) {
			synchronized (ItemDAO.class) {
				instance = new ItemDAO();
			}
		}
		return instance;
	}

	public HashMap<Integer, Item> getItems() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Item item = null;
		Kind k = null;
		GeneralKind g = null;
		Barcode_item bar = null;
		HashMap<Integer, Item> allItem = new HashMap<Integer, Item>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select i.id itemid,i.description itemdescription,i.brand itembrand,u.id uid,"
					+ " b.barcode barcode, b.description bdesc,b.id_general_kinds bgid,k.name kname,k.id kid, "
					+ " k.net_reachable knet_reachable,k.id_general_kinds kgid,count(u.quantity) quantity "
					+ " from general_items i "
					+ " inner join used u on u.item_id = i.id "
					+ " left join barcode_items b on b.barcode = i.barcode "
					+ " left join items_kinds k on k.id = i.kind "
					+ " group by i.id";

			/*
			 * "select i.id itemid,i.description itemdescription,i.brand itembrand,u.quantity quantity,u.id uid, "
			 * +
			 * " o.id ownerid, o.name ownername,o.surname ownersurname,o.email owneremail, "
			 * +
			 * " off.id offid , off.name offname,off.id_map offidmap,off.pos offpos, "
			 * +
			 * " b.barcode barcode, b.description bdesc,b.id_general_kinds bgid, "
			 * +
			 * " net.description ndescription,net.mac_address,net.ip_address,k.name kname,k.id kid,k.net_reachable knet_reachable,k.id_general_kinds kgid, "
			 * + " IF(r.value is null,0,1) net_discovered " +
			 * "from general_items i " +
			 * "inner join used u on u.item_id = i.id " +
			 * "left join owner o on u.owner_id = o.id " +
			 * "left join network net on net.id_item_pc=i.id " +
			 * "left join office off on u.office_id = off.id " +
			 * "left join barcode_items b on b.barcode = i.barcode " +
			 * "left join items_kinds k on k.id = i.kind " +
			 * "left join registry_system r on k.net_reachable = 1 " +
			 * "and r.parameter = 'MAC-address' and r.value  = net.mac_address and r.status = 1"
			 * ;
			 */

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				item = new Item();
				item.setId(rs.getInt("itemid"));
				item.setDescription(rs.getString("itemdescription"));
				item.setBrand(rs.getString("itembrand"));
				item.setQuantity(rs.getInt("quantity"));

				bar = new Barcode_item();
				bar.setBarcode(rs.getString("barcode"));
				bar.setDescription(rs.getString("bdesc"));
				bar.barcodeToImage();

				/* imposto id per limite corrispondenza barocde - kind */
				g = new GeneralKind();
				g.setId(rs.getInt("bgid"));
				bar.setgKind(g);

				item.setBarcode(bar);

				k = new Kind();
				k.setId(rs.getInt("kid"));
				k.setName(rs.getString("kname"));
				k.setNet_reachable(rs.getBoolean("knet_reachable"));

				/* imposto id per limite corrispondenza barocde - kind */
				g = new GeneralKind();
				g.setId(rs.getInt("kgid"));
				k.setgKind(g);

				item.setKind(k);

				allItem.put(item.getId(), item);
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getItems: SQL failed", e);
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
		return allItem;

	}

	public HashMap<Integer, Item> getItems(int officePos, int idMap)
			throws DataAccessException, DataLayerException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		Item item = null;
		Office off = null;
		Owner own = null;
		Kind k = null;
		GeneralKind g = null;
		Network net = null;
		Barcode_item bar = null;
		HashMap<Integer, Item> allItem = new HashMap<Integer, Item>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select i.id itemid,i.description itemdescription,i.brand itembrand,u.quantity quantity,u.id uid, "
					+ " o.id ownerid, o.name ownername,o.surname ownersurname,o.email owneremail, "
					+ " off.id offid , off.name offname,off.id_map offidmap,off.pos offpos, "
					+ " b.barcode barcode, b.description bdesc,b.id_general_kinds bgid, "
					+ " net.description ndescription,net.mac_address,net.ip_address,k.name kname,k.id kid,k.net_reachable knet_reachable,k.id_general_kinds kgid, "
					+ " IF(r.value is null,0,1) net_discovered "
					+ " from general_items i "
					+ " inner join used u on u.item_id = i.id "
					+ " left join owner o on u.owner_id = o.id "
					+ " inner join office off on u.office_id = off.id and off.id_map = ? and off.pos= ? "
					+ " left join network net on net.id_item_pc=i.id "
					+ " left join barcode_items b on b.barcode = i.barcode "
					+ " left join items_kinds k on k.id = i.kind "
					+ " left join registry_system r on k.net_reachable = 1 "
					+ " and r.parameter = 'MAC-address' and r.value  = net.mac_address and r.status = 1";

			ps = connInventory.prepareStatement(sql);
			ps.setInt(1, idMap);
			ps.setInt(2, officePos);
			rs = ps.executeQuery();

			while (rs.next()) {
				item = new Item();
				item.setId(rs.getInt("itemid"));
				item.setDescription(rs.getString("itemdescription"));
				item.setBrand(rs.getString("itembrand"));
				item.setQuantity(rs.getInt("quantity"));
				item.setUid(rs.getInt("uid"));
				item.setNet_discovered(rs.getBoolean("net_discovered"));

				own = new Owner();
				own.setId(rs.getInt("ownerid"));
				own.setName(rs.getString("ownername"));
				own.setSurname(rs.getString("ownersurname"));
				own.setEmail(rs.getString("owneremail"));

				item.setOwner(own);

				off = new Office();
				off.setId(rs.getInt("offid"));
				off.setName(rs.getString("offname"));
				off.setId_map(rs.getInt("offidmap"));
				off.setPos(rs.getInt("offpos"));

				item.setOffice(off);

				bar = new Barcode_item();
				bar.setBarcode(rs.getString("barcode"));
				bar.setDescription(rs.getString("bdesc"));
				bar.barcodeToImage();

				/* imposto id per limite corrispondenza barocde - kind */
				g = new GeneralKind();
				g.setId(rs.getInt("bgid"));
				bar.setgKind(g);

				item.setBarcode(bar);

				k = new Kind();
				k.setId(rs.getInt("kid"));
				k.setName(rs.getString("kname"));
				k.setNet_reachable(rs.getBoolean("knet_reachable"));

				/* imposto id per limite corrispondenza barocde - kind */
				g = new GeneralKind();
				g.setId(rs.getInt("kgid"));
				k.setgKind(g);

				item.setKind(k);

				net = new Network();
				net.setDescription(rs.getString("ndescription"));
				net.setIp_address(rs.getString("ip_address"));
				net.setMac_address(rs.getString("mac_address"));

				if (allItem.containsKey(item.getId())) {
					Item aux = allItem.get(item.getId());
					aux.addToNetList(net);
				} else {
					item.addToNetList(net);
					allItem.put(item.getId(), item);
				}
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getItems: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allItem;

	}

	public int insertNewItem(Item i) throws DataAccessException,
			DataLayerException {

		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!existItemWithDescription(i.getDescription())
				|| i.getOffice().getId() != 0) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				if (i.getOffice().getId() == 0) {
					String sql = "insert into general_items(description,brand,barcode,kind) values(?,?,?,?)";

					ps = connInventory.prepareStatement(sql);
					ps.setString(1, i.getDescription().toUpperCase());
					ps.setString(2, i.getBrand());
					ps.setString(3, i.getBarcode().getBarcode());
					ps.setInt(4, i.getKind().getId());

					risultato = ps.executeUpdate();
				} else {
					risultato = 1;
				}

				if (risultato < 0) {
					throw new DataLayerException("Insert Error Item: " + i);
				} else {
					i.setId(lastInsertId(connInventory));
					UsedDAO.getInstance().insertUsed(connInventory, i);
					connInventory.commit();
					/*
					 * TableBeanItem tabBean = TableBeanItem
					 * .findBean("tableBeanItem"); List<Barcode_item>
					 * barcode_items = Barcode_itemDAO
					 * .getInstance().getBarcodes();
					 * tabBean.setAvaliableBarcode_item(barcode_items);
					 */
				}

				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertNewItem: SQL failed", e);
				e.printStackTrace();
				throw new DataAccessException(e.toString(), e);
			} catch (RuntimeException e) {
				throw new DataAccessException(e.toString());
			} finally {
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new DataLayerException("Insert Error Item: " + i);
		}
		return risultato;
	}

	public int lastInsertId(Connection connInventory)
			throws DataAccessException {
		ResultSet rs = null;
		Statement statement = null;
		int lastInsert = -1;

		try {
			statement = connInventory.createStatement();

			rs = statement.executeQuery("SELECT LAST_INSERT_ID()");

			if (rs.next())
				lastInsert = rs.getInt(1);

			statement.close();
			rs.close();

		} catch (SQLException e) {
			log.error("lastInsertId: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} finally {
			try {
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lastInsert;
	}

	public boolean existItemWithDescription(String description)
			throws DataAccessException, DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select description from general_items where UPPER(description) = UPPER(?)";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, description);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("existItemWithDescription: SQL failed", e);
			e.printStackTrace();
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

	public boolean existMacAddress(String mac_address)
			throws DataAccessException, DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select mac_address from general_items where mac_address = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, mac_address);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("existMacAddress: SQL failed", e);
			e.printStackTrace();
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

	public boolean usedBarcodeIntoItem(Item i) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean used = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select count(barcode) from general_items where barcode = ? and id!=?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, i.getBarcode().getBarcode());
			ps.setInt(2, i.getId());

			rs = ps.executeQuery();

			if (rs.next())
				if (rs.getInt(1) > 0)
					used = true;

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("usedBarcodeIntoItem: SQL failed", e);
			e.printStackTrace();
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
		return used;
	}

	public int modifyItem(Item i) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "update general_items set brand=?,barcode=?,kind=? where id=?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, i.getBrand());
			ps.setString(2, i.getBarcode().getBarcode());
			ps.setInt(3, i.getKind().getId());
			ps.setInt(4, i.getId());
			i.getBarcode().barcodeToImage();

			risultato = ps.executeUpdate();

			if (i.getOffice().getId() != 0 || i.getOwner().getId() != 0)
				risultato = UsedDAO.getInstance().modifyUsed(connInventory, i);

			if (risultato > 0) {
				connInventory.commit();
			} else
				connInventory.rollback();
			/*
			 * TableBeanItem tabBean = TableBeanItem.findBean("tableBeanItem");
			 * List<Barcode_item> barcode_items = Barcode_itemDAO.getInstance()
			 * .getBarcodes(); tabBean.setAvaliableBarcode_item(barcode_items);
			 */

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("modifyItem: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException("Errore Sql");
		} catch (RuntimeException e) {
			throw new DataAccessException("Errore Runtime");
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;

	}

	public int moveItem(Item iFrom, Item iTo) throws DataAccessException,
			DataLayerException {
		Connection connInventory = null;
		int risultato = 0;

		if (iFrom.getOffice().getId() != iTo.getOffice().getId()
				|| iFrom.getOwner().getId() != iTo.getOwner().getId()) {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			try {
				connInventory.setAutoCommit(false);
				risultato = UsedDAO.getInstance().moveUsedTo(connInventory,
						iFrom, iTo);

				if (risultato > 0) {
					connInventory.commit();
				} else
					connInventory.rollback();

				DBConnectionManager.CloseConnection(connInventory);
			} catch (SQLException e) {
				throw new DataLayerException("Move Item Error From: " + iFrom
						+ " To: " + iTo);
			}
		} else {
			throw new DataLayerException("Move Item Error From: " + iFrom
					+ " To: " + iTo + " are the same");
		}
		return risultato;

	}

	public int deleteItem(Item i) throws DataAccessException,
			DataLayerException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {

			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			if (!UsedDAO.getInstance().existUsedItem(connInventory, i)) {

				String sql = "delete from general_items where id=?";

				ps = connInventory.prepareStatement(sql);
				ps.setInt(1, i.getId());
				risultato = ps.executeUpdate();

				if (risultato > 0) {
					UsedDAO.getInstance().decreaseUsed(i);
					UsedDAO.getInstance().cleanNoQuantityUsed(connInventory);

					connInventory.commit();/*
											 * TableBeanItem tabBean =
											 * TableBeanItem
											 * .findBean("tableBeanItem");
											 * List<Barcode_item> barcode_items
											 * = Barcode_itemDAO
											 * .getInstance().getBarcodes();
											 * tabBean.setAvaliableBarcode_item(
											 * barcode_items);
											 */
				} else
					connInventory.rollback();

				ps.close();

			} else {
				throw new DataLayerException("Delete Error Item Id: " + i);
			}

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("deleteItem: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		}

		return risultato;
	}

	public List<Integer> getNetDiscoveredPcList() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		List<Integer> allIdDiscovered = new ArrayList<Integer>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select g.id gid from general_items g "
					+ "inner join network n on g.id = n.id_item_pc "
					+ "inner join items_kinds k on g.kind = k.id "
					+ "inner join registry_system r on k.net_reachable = 1 "
					+ "and r.parameter = 'MAC-address' and r.value  = n.mac_address and r.status = 1";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				allIdDiscovered.add(rs.getInt("gid"));
			}

			ps.close();
			rs.close();

			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getNetDiscoveredPcList: SQL failed", e);
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
		return allIdDiscovered;
	}

}
