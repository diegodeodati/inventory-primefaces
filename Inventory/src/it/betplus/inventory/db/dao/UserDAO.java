package it.betplus.inventory.db.dao;

import it.betplus.inventory.db.connector.DBConnectionManager;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.User;
import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class UserDAO {

	protected static Logger log = Logger.getLogger(UserDAO.class);

	private static UserDAO instance;

	private UserDAO() {
		super();
	}

	public static synchronized UserDAO getInstance() {
		if (instance == null) {
			synchronized (UserDAO.class) {
				instance = new UserDAO();
			}
		}
		return instance;
	}

	public List<User> getUsers() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		User user = null;
		List<User> alluser = new ArrayList<User>();

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select u.* from users u";

			ps = connInventory.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				user = new User();
				user.setUsername(rs.getString("USERNAME"));
				user.setPassword(rs.getString("USER_PASS"));
				user.setName(rs.getString("NOME"));
				user.setSurname(rs.getString("COGNOME"));
				user.setEmail(rs.getString("EMAIL"));
				user.setRole(rs.getString("ROLE"));

				alluser.add(user);
			}

			ps.close();
			rs.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getUsers: SQL failed", e);
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
		return alluser;

	}

	public User getUserByUsernamePassword(String username, String password)
			throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connInventory = null;
		User user = null;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select u.* from users u where u.`USERNAME` like ? and u.`USER_PASS` like ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, "%" + username + "%");
			ps.setString(2, password);
			rs = ps.executeQuery();

			while (rs.next()) {
				user = new User();
				user.setUsername(rs.getString("USERNAME"));
				user.setPassword(rs.getString("USER_PASS"));
				user.setName(rs.getString("NOME"));
				user.setSurname(rs.getString("COGNOME"));
				user.setEmail(rs.getString("EMAIL"));
				user.setRole(rs.getString("ROLE"));
			}

			ps.close();
			rs.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("getUserByUsernamePassword: SQL failed", e);
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
		return user;

	}

	public int insertNewUser(User ut) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		if (!exsistUsername(ut.getUsername())) {

			try {
				connInventory = DBConnectionManager
						.inventoryConnectionFactory();

				connInventory.setAutoCommit(false);

				String sql = "insert into users(username, user_pass, nome, cognome,email,role) values(?,?,?,?,?,?)";

				ps = connInventory.prepareStatement(sql);
				ps.setString(1, ut.getUsername());
				ps.setString(2, ut.getPassword());
				ps.setString(3, ut.getName());
				ps.setString(4, ut.getSurname());
				ps.setString(5, ut.getEmail());
				ps.setString(6, ut.getRole());

				risultato = ps.executeUpdate();

				if (risultato == 1)
					connInventory.commit();
				else
					connInventory.rollback();

				ps.close();
				DBConnectionManager.CloseConnection(connInventory);

			} catch (SQLException e) {
				log.error("insertNewUser: SQL failed", e);
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

	public boolean exsistUsername(String username) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();

			String sql = "select username from users where username = ?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, username);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			ps.close();
			rs.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("exsistUsername: SQL failed", e);
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

	public int modifyUser(User ut) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "update users set user_pass=?, nome=?, cognome=?,email=?,role=? where username=?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, ut.getPassword());
			ps.setString(2, ut.getName());
			ps.setString(3, ut.getSurname());
			ps.setString(4, ut.getEmail());
			ps.setString(5, ut.getRole());
			ps.setString(6, ut.getUsername());

			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			ps.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("modifyUser: SQL failed", e);
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

	public int deleteUser(String username) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connInventory = null;
		int risultato = 0;

		try {
			connInventory = DBConnectionManager.inventoryConnectionFactory();
			connInventory.setAutoCommit(false);

			String sql = "delete from users where username=?";

			ps = connInventory.prepareStatement(sql);
			ps.setString(1, username);
			risultato = ps.executeUpdate();

			if (risultato > 0)
				connInventory.commit();
			else
				connInventory.rollback();

			ps.close();
			DBConnectionManager.CloseConnection(connInventory);

		} catch (SQLException e) {
			log.error("deleteUser: SQL failed", e);
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