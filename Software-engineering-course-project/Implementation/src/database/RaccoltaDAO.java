package database;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.sql.Timestamp;

public class RaccoltaDAO{
    private long IDRaccoltaD;
    private String titolo;
    private String descrizione;
    private ArrayList<PoesiaDAO> listaPoesie;
    private AutoreDAO AutoreRaccolta;
    
    //costruttore di default
    public RaccoltaDAO(){
        super();
        
        this.listaPoesie=new ArrayList<PoesiaDAO>();
        
    }
    
    //costruttore per inizializzare tramite chiave primaria
    public RaccoltaDAO(long IDRaccoltaD){
        super();
        this.listaPoesie=new ArrayList<PoesiaDAO>();
        this.IDRaccoltaD=IDRaccoltaD;
        
        readRaccolta();
        readListaPoesie();
        
    }
   
    
    //costruttore per inizializzare tramite oggetto DAO gia definito
    public RaccoltaDAO(RaccoltaDAO RDAO){
        this.listaPoesie=new ArrayList<PoesiaDAO>();
        this.IDRaccoltaD=RDAO.getIDRaccoltaD();
        this.titolo=RDAO.getTitolo();
        this.descrizione=RDAO.getDescrizione();
        this.listaPoesie=RDAO.getListaPoesie();
        this.AutoreRaccolta=RDAO.getAutoreRaccolta();
        
        readRaccolta();
        readListaPoesie();
    }
    
    //costruttore per read by email
     public RaccoltaDAO(long ID, String titolo, String descrizione){//aggiungere autore raccolta e autore commento in commentodao
        this.IDRaccoltaD=ID;
        this.titolo=titolo;
        this.descrizione=descrizione;
        
        readRaccolta();
        readListaPoesie();
        
    }
 
    //metodi get
    public long getIDRaccoltaD(){
        return this.IDRaccoltaD;
    }
    
    public String getTitolo(){
        return this.titolo;
    }
    
    public String getDescrizione(){
        return this.descrizione;
    }
    
    public ArrayList<PoesiaDAO> getListaPoesie(){
        return this.listaPoesie;
    }
    
    public AutoreDAO getAutoreRaccolta(){
        return this.AutoreRaccolta;
    }
    
    //metodi set
    public void setIDRaccoltaD(long IDRaccoltaD){
        this.IDRaccoltaD=IDRaccoltaD;
    }
    
    public void setTitolo(String titolo){
        this.titolo=titolo;
    }
    
    public void setDescrizione(String descrizione){
        this.descrizione=descrizione;
    }
    
    public void setListaPoesie(ArrayList<PoesiaDAO> listaPoesie){
        this.listaPoesie=listaPoesie;
    }
    
    public void setAutoreRaccolta(AutoreDAO ADAO){
        this.AutoreRaccolta=ADAO;
    }
    
    //creazione nuov ID per nuovo utente
   // Modifica getNuovoID:
public long getNuovoID() {
    return -1; // Auto-increment gestito dal DB
}

// SOSTITUISCI COMPLETAMENTE il metodo createRaccolta con questo:
public long createRaccolta(String titolo, String descrizione, long idAutore) {
    long nuovoId = -1;
    
    // Query senza il campo ID (auto-increment)
    String query = "INSERT INTO Raccolte (Titolo, Descrizione, IDUtente) VALUES (?, ?, ?)";

    try {
        Connection conn = DBConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, titolo);
        stmt.setString(2, descrizione);
        stmt.setLong(3, idAutore);

        int rows = stmt.executeUpdate();
        
        // Recupera l'ID generato dal database
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            nuovoId = generatedKeys.getLong(1);
            this.IDRaccoltaD = nuovoId;
        }
        
        stmt.close();
        conn.close();

    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }

    return nuovoId;
}
    
    //lettura da DB della raccolta tramite ID
    public void readRaccolta(){
        String query="SELECT * FROM Raccolte WHERE ID="+this.IDRaccoltaD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.setTitolo(rs.getString("Titolo"));
                this.setDescrizione(rs.getString("Descrizione"));
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }
    
    

    //aggiorna titolo e descrizione di una raccolta esistente
    public void updateRaccolta(){
        String query="UPDATE Raccolte SET Titolo='"+this.titolo+"', Descrizione='" 
                      +this.descrizione+"' WHERE ID="+this.IDRaccoltaD;
        try{
            DBConnectionManager.updateQuery(query);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }


    //elimina raccolta dal DB
    public void deleteRaccolta(){
        String queryPoesie="DELETE FROM Poesie WHERE IDRaccolta="+this.IDRaccoltaD;
        String queryRaccolta="DELETE FROM Raccolte WHERE ID="+this.IDRaccoltaD;
        try{
            DBConnectionManager.updateQuery(queryPoesie);
            DBConnectionManager.updateQuery(queryRaccolta);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    
    //lettura da DB delle poesie contenute in una raccolta
    public void readListaPoesie(){
        this.listaPoesie=new ArrayList<>();
        String query="SELECT * FROM Poesie WHERE IDRaccolta="+this.IDRaccoltaD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            while(rs.next()){
                PoesiaDAO PDAO=new PoesiaDAO();
         

                //altri attributi
                PDAO.setRaccolta(this);
                PDAO.setIDPoesiaD(rs.getLong("ID"));
                PDAO.setTitolo(rs.getString("Titolo"));
                PDAO.setTesto(rs.getString("Testo"));
                PDAO.setPubblica(rs.getBoolean("Pubblica"));

                
                Timestamp ts = rs.getTimestamp("DataPubblicazione"); // senza spazio!
                if (ts != null) {
                    PDAO.setDataPubblicazione(ts.toLocalDateTime());
                }
                this.listaPoesie.add(PDAO);
                
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
    
    //cerca raccolta dal suo titolo
    public static ArrayList<RaccoltaDAO> searchByTitolo(String titolo){
        ArrayList<RaccoltaDAO> result=new ArrayList<>();
        String query="SELECT * FROM Raccolte WHERE Titolo LIKE '%"+titolo+"%'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            while(rs.next()){
                RaccoltaDAO RDAO=new RaccoltaDAO(rs.getLong("ID"));
                RDAO.readRaccolta();
                result.add(RDAO);
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    //num poesie in una raccolta
    public int getNumeroPoesie(){
        return listaPoesie.size();
    }
    
 
    //poesia piu like
    public PoesiaDAO getPoesiaPiuApprezzata(){
        PoesiaDAO best=null;
        int maxCuori=-1;//-1 in modo che qualsiasi poesia supera tale valore per farr confronto
        //cioe la prima poesia diventa la best
        for(PoesiaDAO p:listaPoesie){
            if(p.getCuore()>maxCuori){
                maxCuori=p.getCuore();
                best=p;
                //poi iteraando il ciclo dei confronti si trova quella con piu like
            }
        }
        return best;
    }



    //poesie con stesso tag
    public ArrayList<PoesiaDAO> getPoesiePerTag(String tag){
        ArrayList<PoesiaDAO> result=new ArrayList<>();
        for(PoesiaDAO p:listaPoesie){
            if(p.getTag().contains(tag)){
                result.add(p);
            }
        }
        return result;
    }

    //per leggere solo poesie pubbliche e le ordina in cronologico decrescente
    public ArrayList<PoesiaDAO> getPoesiePubblicheOrdinate(){
        ArrayList<PoesiaDAO> pubbliche=new ArrayList<>();
        for(PoesiaDAO p:listaPoesie){
            if(p.getPubblica()){
                pubbliche.add(p);
            }
        }
        pubbliche.sort((p1, p2)->p2.getDataPubblicazione().compareTo(p1.getDataPubblicazione()));
        return pubbliche;
    }

    
   //FATTO 
    
}
