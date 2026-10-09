package database;

import java.time.LocalDateTime;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.sql.Timestamp;


public class CommentoDAO{
    private long IDCommentoD;
    private String testo;
    private LocalDateTime dataPubblicazione;
    private AutoreDAO autoreCommento;
    
    //costruttore di default
    public CommentoDAO(){
        super();
        this.autoreCommento=new AutoreDAO();
    }
    
    //costruttore per inizializzare tramite chiave primaria
    public CommentoDAO(long IDCommentoD){
        super();
        this.IDCommentoD=IDCommentoD;
        
    }
    
    //costruttore per inizializzare tramite oggetto DAO gia definito
    public CommentoDAO(CommentoDAO CDAO){
        this.IDCommentoD=CDAO.getIDCommentoD();
        this.testo=CDAO.getTesto();
        this.dataPubblicazione=CDAO.getDataPubblicazione();
        this.autoreCommento=CDAO.getAutoreCommento();
    }
    
    //costruttore per read byemail in adao
     public CommentoDAO(long IDCommentoD, String TestoD, LocalDateTime dataPubblicazione, long IDAutoreCommento){
        this.IDCommentoD=IDCommentoD;
        this.testo=TestoD;
        this.dataPubblicazione=dataPubblicazione;

        this.autoreCommento=new AutoreDAO(IDAutoreCommento);
        
    }
    
    
    //metodi get
    public long getIDCommentoD(){
        return this.IDCommentoD;
    }
    
    public String getTesto(){
        return this.testo;
    }
    
    public LocalDateTime getDataPubblicazione(){
        return this.dataPubblicazione;
    }
    
    public AutoreDAO getAutoreCommento(){
        return this.autoreCommento;
    }
    
    //metodi set
    public void setIDCommentoD(long IDCommentoD){
        this.IDCommentoD=IDCommentoD;
    }
    
    public void setTesto(String testo){
        this.testo=testo;
    }
    
    public void setDataPubblicazione(LocalDateTime dataPubblicazione){
        this.dataPubblicazione=dataPubblicazione;
    }
    
    public void setAutoreCommento(AutoreDAO ADAO){
        this.autoreCommento=ADAO;
    }
    
    //nuovoID
    // Modifica getNuovoID:
public long getNuovoID() {
    return -1; // Auto-increment gestito dal DB
}

// Modifica createCommento:
public boolean createCommento(long IDPoesia) {
    String query = "INSERT INTO Commenti (Testo, DataPubblicazione, AutoreCommento, IDPoesia) VALUES (?, ?, ?, ?)";
    
    try {
        Connection conn = DBConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        
        stmt.setString(1, this.testo);
        stmt.setTimestamp(2, Timestamp.valueOf(this.dataPubblicazione));
        stmt.setLong(3, this.autoreCommento.getIDUtenteD());
        stmt.setLong(4, IDPoesia);
        
        int rows = stmt.executeUpdate();
        
        // Recupera l'ID generato
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.IDCommentoD = generatedKeys.getLong(1);
        }
        
        stmt.close();
        conn.close();
        
        return rows > 0;
        
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    //lettura da DB del commento tramite ID
    public void readCommento(){
        String query="SELECT * FROM Commenti WHERE ID="+this.IDCommentoD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                // testo del commento
                this.setTesto(rs.getString("Testo"));
            
                // data pubblicazione
                
            Timestamp ts = rs.getTimestamp("DataPubblicazione"); // senza spazio!
            if (ts != null) {
                this.setDataPubblicazione(ts.toLocalDateTime());
            }

                // autore del commento
                long IDAutore= rs.getLong("AutoreCommento");
                AutoreDAO ADAO=new AutoreDAO();   //usa il costruttore di default
                ADAO.setIDUtenteD(IDAutore); //imposta l'ID
                ADAO.readAutore(); //legge tutti i dati dell'autore dal DB
                this.setAutoreCommento(ADAO);
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }


    
    //lettura da DB del commento tramite Autore
    public void readCommentoByAutore(){
        String query ="SELECT * FROM Commenti WHERE AutoreCommento="+this.autoreCommento;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.setTesto(rs.getString("Testo"));
                Timestamp ts = rs.getTimestamp("DataPubblicazione");
                LocalDateTime dataPubblicazione = ts.toLocalDateTime();

                //this.dataPubblicazione(rs.getArray("Data pubblicazione"));
                //this.setAutoreCommento(rs.getObject("", type));
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }    
    
    
    //non so se vogliamo metterlo
    public void updateCommento(){
    String query="UPDATE Commenti SET Testo='"+this.testo+"' WHERE ID="+this.IDCommentoD;
        try{
            DBConnectionManager.updateQuery(query);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    //cancella commento
    public void deleteCommento(){
        String query="DELETE FROM Commenti WHERE ID="+this.IDCommentoD;
        try{
            DBConnectionManager.updateQuery(query);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    
    
//FATTO
    
}