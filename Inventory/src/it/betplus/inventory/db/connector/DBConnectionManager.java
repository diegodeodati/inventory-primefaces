package it.betplus.inventory.db.connector;


import it.betplus.web.framework.exceptions.DataLayerException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;




public class DBConnectionManager {

	protected static Logger logger = Logger.getLogger(DBConnectionManager.class);

	
	public static Connection inventoryConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/mysql_inventory");
			conn = ds.getConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			if(conn.isClosed())
				throw new DataLayerException("La connessione è chiusa");			


		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi su mysql", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione su mysql", e);
		}
		return conn;	

	}	
	
	public static void CloseConnection(Connection conn) throws DataLayerException{
		try{
			if(conn!=null)
			conn.close(); // Return to connection pool
			conn = null;  // Make sure we don't close it twice
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		}  
		finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}

		}
	}
	
}
