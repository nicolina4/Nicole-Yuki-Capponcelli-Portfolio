package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionManager {
	
	public static String url = "jdbc:mysql://localhost:3306/";
	public static String dbName = "schemasoftware";
	public static String driver = "com.mysql.cj.jdbc.Driver";
	public static String userName = "software23"; 
	public static String password = "dbaAlbergo_23";
	
	/**
	 * Crea la connessione al database
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Connessione
	 * */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		Connection conn = null;
		
		Class.forName(driver);
		
		conn = DriverManager.getConnection(url+dbName,userName,password);
		
		return conn;
	}
	
	/**
	 * Chiude la connessione al database
	 * 
	 * @param c Connessione da chiudere
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public static void closeConnection(Connection c) throws SQLException {
		c.close();
	}
	
	/**
	 * Seleziona e salva i risultati di una query sul database
	 * 
	 * @param query Query di cui salvare i risultati
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Risultati della query
	 * */
	public static ResultSet selectQuery(String query) throws ClassNotFoundException, SQLException {
		
		Connection conn = getConnection();
		
		Statement statement = conn.createStatement();
		
		ResultSet ret = statement.executeQuery(query);
		
		return ret;
	}
	
	/**
	 * Esegue una query sul database
	 * 
	 * @param query Query da eseguire
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Valore intero che specifica il numero di righe modificate dalla query
	 * */
	public static int updateQuery(String query) throws ClassNotFoundException, SQLException {
		
		Connection conn = getConnection();
		
		Statement statement = conn.createStatement();
		
		int ret = statement.executeUpdate(query);
		
		conn.close();
		
		return ret;
	}
	
	/**
	 * Esegue una query sul database e salva la chiave primaria relativa al primo risultato trovato
	 * 
	 * @param query Query da eseguire
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Chiave generata dopo l'esecuzione della query
	 * */
	public static Integer updateQueryReturnGeneratedKey(String query) throws ClassNotFoundException, SQLException {
		Integer ret = null;
		
		Connection conn = getConnection();
		
		Statement statement = conn.createStatement();
		
		statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		
		ResultSet rs = statement.getGeneratedKeys();
		
		if (rs.next()){
		    ret = rs.getInt(1);
		}
		
		conn.close();
		
		return ret;
	}
}