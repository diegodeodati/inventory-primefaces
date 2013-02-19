/**
 * 
 */
package it.betplus.inventory.db.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Diego
 *
 */
public class MysqlConnector {

	/** Effettua una connessione verso un dbms MySQL
	 * 
	 * @param db_connect_string jdbc:DRIVER:server_type://host:port/schema
	 * @param db_userid username
	 * @param db_password password 
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	 public Connection dbConnect(String db_connect_string, String db_userid, String db_password) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
			    {
		 			Connection conn = null;
		        	
							Class.forName ("com.mysql.jdbc.Driver").newInstance ();
							conn = DriverManager.getConnection (db_connect_string, db_userid, db_password);
							System.out.println("Connesso MYSQL:"+db_connect_string);
					
			        return conn;
			    }
	
	}

