package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;



public class DBConnectionManager {
    public static String url="jdbc:mysql://localhost:3306/";
    public static String DBName="DBSoftware";
    public static String driver="com.mysql.cj.jdbc.Driver";
    public static String username="software25";
    public static String password="Poesiamo25";
    
    //crea connessione DB
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
    Connection conn = null;
    
    try {
        Class.forName(driver);
        System.out.println("Driver JDBC caricato correttamente!");
    } catch (ClassNotFoundException e) {
        System.err.println("Errore: driver JDBC non trovato!");
        e.printStackTrace();
    }

    Class.forName(driver);
    conn = DriverManager.getConnection(url + DBName, username, password);
    
    // AGGIUNGI QUESTE RIGHE:
    conn.setAutoCommit(true); // Assicura che ogni statement venga committato automaticamente
    System.out.println("✅ Connessione ottenuta - AutoCommit: " + conn.getAutoCommit());
    
    return conn;
}
    
    //chiudi connessione DB
    public static void closeConnection(Connection conn)throws SQLException{
        conn.close();
    }
    
    //seleziona e salva i risultati di una query nel DB
    public static ResultSet selectQuery(String query) throws ClassNotFoundException,SQLException{
        Connection conn=getConnection();

        Statement statement=conn.createStatement();
        ResultSet rs=statement.executeQuery(query);
        return rs;
    }
    
    // Versione più robusta con gestione specifica dei tipi
public static ResultSet selectQueryWithParams(String query, Object... params) 
    throws ClassNotFoundException, SQLException {
    
    Connection conn = getConnection();
    PreparedStatement stmt = conn.prepareStatement(query);
    
    for (int i = 0; i < params.length; i++) {
        Object param = params[i];
        
        if (param instanceof String) {
            stmt.setString(i + 1, (String) param);
        } else if (param instanceof Integer) {
            stmt.setInt(i + 1, (Integer) param);
        } else if (param instanceof Long) {
            stmt.setLong(i + 1, (Long) param);
        } else if (param instanceof Boolean) {
            stmt.setBoolean(i + 1, (Boolean) param);
        } else if (param instanceof LocalDateTime) {
            stmt.setTimestamp(i + 1, Timestamp.valueOf((LocalDateTime) param));
        } else {
            stmt.setObject(i + 1, param);
        }
    }
    
    return stmt.executeQuery();
}
    
    //esegue una query sul DB
    public static int updateQuery(String query)throws ClassNotFoundException,SQLException{
        Connection conn=getConnection();
        Statement statement=conn.createStatement();
        System.out.println(query);
        int rs=statement.executeUpdate(query);
        conn.close();
        return rs;
    }
    
    //esegue query su DB e salva chiave primaria relativa al primo risultato
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
    
    // In DBConnectionManager
public static int updateQueryWithParams(String query, Object... params) throws ClassNotFoundException, SQLException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    
    try {
        conn = getConnection();
        pstmt = conn.prepareStatement(query);
        
        // Imposta i parametri dinamicamente
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            
            if (param instanceof String) {
                pstmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof Long) {
                pstmt.setLong(i + 1, (Long) param);
            } else if (param instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) param);
            } else if (param instanceof Timestamp) {
                pstmt.setTimestamp(i + 1, (Timestamp) param);
            } else if (param instanceof LocalDateTime) {
                pstmt.setTimestamp(i + 1, Timestamp.valueOf((LocalDateTime) param));
            } else if (param == null) {
                pstmt.setNull(i + 1, java.sql.Types.NULL);
            }
        }
        
        return pstmt.executeUpdate();
        
    } finally {
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
    }
}
}
//FATTO