package database;

import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.sql.Timestamp;


public class PoesiaDAO{
    private long IDPoesiaD;
    private String titolo;
    private String testo;
    private ArrayList<String> tag;
    private boolean pubblica;
    private LocalDateTime dataPubblicazione;
    private AutoreDAO autore;
    private RaccoltaDAO raccolta;
    private int cuore;
    private ArrayList<CommentoDAO> commento;
    
    //costruttore di default
    public PoesiaDAO(){
        this.tag=new ArrayList<String>();
        this.pubblica=false;
        this.autore=new AutoreDAO();
        this.raccolta=new RaccoltaDAO();
        this.commento=new ArrayList<CommentoDAO>();
        
       
    }
    
    //costruttore per inizializzare tramite oggetto DAO gia definito
    public PoesiaDAO(PoesiaDAO poesia){
         this.tag=new ArrayList<String>();
        this.commento=new ArrayList<CommentoDAO>();
        this.IDPoesiaD=poesia.getIDPoesiaD();
        this.titolo=poesia.getTitolo();
        this.testo=poesia.getTesto();
        this.tag=poesia.getTag();
        this.pubblica=poesia.getPubblica();
        this.dataPubblicazione=poesia.getDataPubblicazione();
        this.autore=poesia.getAutore();
        this.raccolta=poesia.getRaccolta();
        this.cuore=poesia.getCuore();
        this.commento=poesia.getCommenti();
    }
    
     //costruttore per inizializzare tramite chiave primaria
    public PoesiaDAO(long ID){
        super();
         this.tag=new ArrayList<String>();
        this.commento=new ArrayList<CommentoDAO>();
        this.IDPoesiaD=ID;
        readPoesia();
        readAutore();
        readRaccolta();
        
        
        
    }
    
    //costruttore per adao
    public PoesiaDAO(long ID, String TitoloP, String TestoP, boolean Pubblica, LocalDateTime DataPubblicazione,ArrayList<String> ListaTag, ArrayList<CommentoDAO> ListaCommenti){
        this.IDPoesiaD=ID;
        this.titolo=TitoloP;
        this.testo=TestoP;
        this.pubblica=Pubblica;
        this.dataPubblicazione=DataPubblicazione;
        this.tag=ListaTag;
        this.commento=ListaCommenti;
    }
    
    
    
    //metodi get
    public long getIDPoesiaD(){
        return this.IDPoesiaD;
    }
    
    public String getTitolo(){
        return this.titolo;
    }
    
    public String getTesto(){
        return this.testo;
    }
    
    public ArrayList<String> getTag(){
        return this.tag;
    }
    
    public boolean getPubblica(){
        return this.pubblica;
    }
    
    public LocalDateTime getDataPubblicazione(){
        return this.dataPubblicazione;
    }
    
    public AutoreDAO getAutore(){
        return this.autore;
    }
    
    public RaccoltaDAO getRaccolta(){
        return this.raccolta;
    }
    
    public int getCuore(){
        return this.cuore;
    }
    
    public ArrayList<CommentoDAO> getCommenti(){
        return this.commento;
    }
    
    //metodi set
    public void setIDPoesiaD(long ID){
        this.IDPoesiaD=ID;
    }
    
    public void setTitolo(String titolo){
        this.titolo=titolo;
    }
    
    public void setTesto(String testo){
        this.testo=testo;
    }
    
    public void setTag(ArrayList<String> tag){
        this.tag=tag;
    }
    
    public void setPubblica(boolean pubblica){
        this.pubblica=pubblica;
    }
    
    public void setDataPubblicazione(LocalDateTime dataPubblicazione){
        this.dataPubblicazione=dataPubblicazione;
    }
    
    public void setAutore(AutoreDAO autore){
        this.autore=autore;
    }
    
    public void setRaccolta(RaccoltaDAO RDAO){
        this.raccolta=RDAO;
    }
    
    public void setCuore(int cuore){
        this.cuore=cuore;
    }
    
    public void setCommento(ArrayList<CommentoDAO> commento){
        this.commento=commento;
    }
    
   
    
    //creazione nuov ID per nuovo utente
    // Modifica getNuovoID:
public long getNuovoID() {
    return -1; // Auto-increment gestito dal DB
}

// Modifica createPoesia:
// SOSTITUISCI COMPLETAMENTE il metodo createPoesia con questo:
public boolean createPoesia(String titolo, String testo, ArrayList<String> tag,
                           boolean pubblica, LocalDateTime dataPubblicazione,
                           long idAutore, Long idRaccolta) {
    int rows = 0;
    
    // Query senza il campo ID (auto-increment)
    String query = "INSERT INTO Poesie (Titolo, Testo, Pubblica, DataPubblicazione, IDUtente, IDRaccolta, NumCuori) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try {
        Connection conn = DBConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        stmt.setString(1, titolo);
        stmt.setString(2, testo);
        stmt.setBoolean(3, pubblica);
        stmt.setTimestamp(4, java.sql.Timestamp.valueOf(dataPubblicazione));
        stmt.setLong(5, idAutore);

        if (idRaccolta != null) {
            stmt.setLong(6, idRaccolta);
        } else {
            stmt.setNull(6, java.sql.Types.BIGINT);
        }

        stmt.setInt(7, 0); // NumCuori iniziali

        rows = stmt.executeUpdate();
        
        // Recupera l'ID generato dal database
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.IDPoesiaD = generatedKeys.getLong(1);
        }
        
        stmt.close();
        conn.close();

    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }

    return rows > 0;
}
    
    //lettura poesie nel DB tramite ID, 101
    public void readPoesia(){
        String query ="SELECT *FROM Poesie WHERE ID= "+this.IDPoesiaD;
        
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                //per convertire local date in string
                Timestamp ts = rs.getTimestamp("DataPubblicazione");
                LocalDateTime dataPubblicazione = ts.toLocalDateTime();

                
                this.setDataPubblicazione(dataPubblicazione);
                this.setTitolo(titolo);
                this.setTesto(testo);
                this.setTag(tag);
                this.setPubblica(pubblica);
                this.setAutore(autore);
                this.setRaccolta(raccolta);
                this.setCuore(cuore);
                this.setCommento(commento);
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }
    
    //lettura autore di una poesia da DB, 137
    public void readAutore(){
        this.autore=new AutoreDAO();
        String query="SELECT Au.ID, Au.nome, Au.cognome"+" FROM Autori Au JOIN Poesie Po ON Au.ID=Po.IDPoesia"+"WHERE Po.ID= "+this.IDPoesiaD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.getAutore().setIDUtenteD(rs.getLong("ID"));
                this.getAutore().setEmail(rs.getString("Email"));
                this.getAutore().setPassword(rs.getString("Password"));
                this.getAutore().setNome(rs.getString("Nome"));
                this.getAutore().setCognome(rs.getString("Cognome"));
                
            }
            
            this.getAutore().readUtente();
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }
    
    //lettura raccolta nella quale e la poesia da DB
    public void readRaccolta(){
	String query="SELECT Ra.ID, Ra.titolo, Ra.descrizione"+ "FROM Raccolte Ra JOIN Poesie Po"+ "ON Ra.ID = Po.IDRaccolta"+ "WHERE Po.ID="+this.IDPoesiaD;
	try{
	    ResultSet rs=DBConnectionManager.selectQuery(query);
	    if(rs.next()){
		this.raccolta=new RaccoltaDAO();
                this.getRaccolta().setIDRaccoltaD(rs.getLong("ID Raccolta"));
                this.getRaccolta().setTitolo(rs.getString("Titolo"));
		this.getRaccolta().setDescrizione(rs.getString("Descrizione"));
	    }
            this.getRaccolta().readListaPoesie();
			
	}catch(SQLException|ClassNotFoundException e){
	        e.printStackTrace();
        }
    }
	
    //lettura commenti di una poesia nel DB
    public void readCommenti(){
        String query="SELECT Co.ID, Co.Testo, Co.DataPubblicazione, Co.AutoreCommento"+
                "FROM Commenti Co JOIN Poesie Po"+"ON Co.ID=Po.IDCommento"+"WHERE Po.ID="+this.IDPoesiaD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.commento=new ArrayList<CommentoDAO>();
                //this.getCommenti().s //da finire
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }
    
    //aggiorno poesia nel DB
    public boolean updatePoesia(){
        String query="UPDATE Poesie SET titolo='"+this.titolo+ 
                      "', testo='"+this.testo + 
                      "', pubblica="+this.pubblica+ 
                      ", dataPubblicazione='"+this.dataPubblicazione+ 
                      "', IDRaccolta="+this.raccolta.getIDRaccoltaD()+ 
                      " WHERE ID="+this.IDPoesiaD;
        try{
            int rows=DBConnectionManager.updateQuery(query);
            return rows>0;
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    
    //cancellazione poesia dal DB
    public boolean deletePoesia(){
        String query="DELETE FROM Poesie WHERE ID="+this.IDPoesiaD;
        try{
            int rows=DBConnectionManager.updateQuery(query);
            return rows>0;
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    //aggiungi cuore a una poesia
    public void addCuore(long IDUtente){
        String queryInsert="INSERT INTO Cuori (IDUtente, IDPoesia) VALUES ("+IDUtente+", "+this.IDPoesiaD+")";
        String queryUpdate="UPDATE Poesie SET NumCuori= NumCuori+1 WHERE ID="+this.IDPoesiaD;
        try{
            DBConnectionManager.updateQuery(queryInsert);
            DBConnectionManager.updateQuery(queryUpdate);
            this.cuore++;
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }

    //aggiungi commento a una poesia
    public boolean addCommento(CommentoDAO commento){
        // 1. Query sicura con parametri placeholder (?)
        String query = "INSERT INTO Commenti (IDPoesia, AutoreCommento, Testo, DataPubblicazione) VALUES (?, ?, ?, ?)";
        // Estrazione dei parametri
        long idAutoreCommento = commento.getAutoreCommento().getIDUtenteD();
        try{
            // 2. Esecuzione tramite DBConnectionManager.updateQueryWithParams
            int rows = DBConnectionManager.updateQueryWithParams(
                query,
                this.IDPoesiaD,
                idAutoreCommento,
                commento.getTesto(),
                commento.getDataPubblicazione() // LocalDateTime viene convertito in Timestamp nel manager
            );
            // 3. Verifica successo e aggiornamento dello stato locale (in-memory)
            if (rows > 0){
                // Assicurati che il CommentoDAO abbia l'ID generato dal DB
                // (Assumendo che createCommento in CommentoDAO sia l'unico a fare l'INSERT finale
                // e che stiamo delegando ad esso.)
                // Dato che qui chiamiamo CDAO.createCommento, se CommentoDAO ha un suo metodo 
                // di creazione, dovremmo chiamarlo, ma in questo flusso, l'INSERT avviene qui.
                // Se l'INSERT ha avuto successo, aggiungi alla lista locale.
                this.commento.add(commento); 
                return true;
            }
     
        }catch(ClassNotFoundException|SQLException e){
            System.err.println("❌ Errore SQL persistenza commento: " + e.getMessage());
            e.printStackTrace(); 
        }
        return false;
    }

    
    // Per mostrare gli ultimi tre commenti di una poesia
    public ArrayList<CommentoDAO> getUltimiCommenti() {
    ArrayList<CommentoDAO> ultimiTreCommenti = new ArrayList<>();
    
    String query = "SELECT * FROM Commenti WHERE IDPoesia = " + this.IDPoesiaD + 
                   " ORDER BY DataPubblicazione DESC LIMIT 3";
    
    try {
        ResultSet rs = DBConnectionManager.selectQuery(query);
        
        while (rs.next()) {
            CommentoDAO CDAO = new CommentoDAO();
            
            CDAO.setIDCommentoD(rs.getLong("ID"));
            CDAO.setTesto(rs.getString("Testo"));
            
           
            Timestamp timestamp = rs.getTimestamp("DataPubblicazione");
            if (timestamp != null) {
                CDAO.setDataPubblicazione(timestamp.toLocalDateTime());
            }
            
            ultimiTreCommenti.add(CDAO);
        }
        
        rs.close(); 
    } 
    catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
    
    return ultimiTreCommenti;
}


   
    
public static ArrayList<PoesiaDAO> getFeedAutoriDiversi(long IDAutore){
    ArrayList<PoesiaDAO> feed = new ArrayList<>();
    
    
    String query = "SELECT * FROM Poesie WHERE Pubblica = true AND IDUtente <> " + IDAutore + 
                   " ORDER BY DataPubblicazione DESC LIMIT 5";
    
    try{
        ResultSet rs = DBConnectionManager.selectQuery(query);
        
        
        
        while(rs.next()){
            PoesiaDAO PDAO = new PoesiaDAO();
            PDAO.setIDPoesiaD(rs.getLong("ID"));
            PDAO.setTitolo(rs.getString("Titolo"));
            PDAO.setTesto(rs.getString("Testo"));
            
            Timestamp ts = rs.getTimestamp("DataPubblicazione");
            if (ts != null) {
                PDAO.setDataPubblicazione(ts.toLocalDateTime());
}

            
            feed.add(PDAO);
        }
    } catch(ClassNotFoundException | SQLException e){
        e.printStackTrace();
    }
    return feed;
}
    
    //per vedere statistiche
    public static void getStatisticheAutore(long IDAutore){
        try {
            String queryCuoriTotali="SELECT SUM(cuore) AS totCuori FROM Poesie WHERE IDAutore="+IDAutore;
            String queryCommentiTotali="SELECT COUNT(Co.ID) AS totCommenti FROM Commenti Co JOIN Poesie Po ON Co.IDPoesia=Po.ID WHERE Po.IDAutore="+IDAutore;
            String queryTopPoesia="SELECT Titolo FROM Poesie WHERE IDAutore="+IDAutore+" ORDER BY cuore DESC LIMIT 1";
        
            ResultSet rs1=DBConnectionManager.selectQuery(queryCuoriTotali);
            ResultSet rs2=DBConnectionManager.selectQuery(queryCommentiTotali);
            ResultSet rs3=DBConnectionManager.selectQuery(queryTopPoesia);
        
            if(rs1.next()&&rs2.next()&&rs3.next()){
                System.out.println("Totale cuori: " + rs1.getInt("totCuori"));
                System.out.println("Totale commenti: " + rs2.getInt("totCommenti"));
                System.out.println("Poesia più apprezzata: " + rs3.getString("Titolo"));
        }
    } catch (ClassNotFoundException|SQLException e) {
        e.printStackTrace();
    }
    }
        
        
        
        public static int getTotalCuoriByAutore(long idUtente) {
            String query = "SELECT SUM(P.NumCuori) AS TotCuori FROM Poesie P WHERE P.IDUtente = " + idUtente;
            try {
                ResultSet rs = DBConnectionManager.selectQuery(query);
                if (rs.next()) return rs.getInt("TotCuori");      //qui ritorna il numero dei cuori
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return 0; //In caso di errore restituisce 0
        }
        
        
        public static int getTotalCommentiByAutore(long idUtente) {
            String query = "SELECT COUNT(Co.ID) AS TotCommenti FROM Commenti Co " +
                           "JOIN Poesie P ON Co.IDPoesia = P.ID WHERE P.IDUtente = " + idUtente;
            try {
                ResultSet rs = DBConnectionManager.selectQuery(query);
                if (rs.next()) return rs.getInt("TotCommenti");
                
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }
        
        
        //trovo la top poesia sfruttando il database con l'ordinamento
        
        public static PoesiaDAO getTopPoesiaByAutore(long idUtente) {
            String query = "SELECT ID, Titolo, NumCuori FROM Poesie " +
                           "WHERE IDUtente = " + idUtente +
                           " ORDER BY NumCuori DESC, DataPubblicazione DESC LIMIT 1";
            try {
                ResultSet rs = DBConnectionManager.selectQuery(query);
                if (rs.next()) {
                    long id = rs.getLong("ID");
                    // Nota: Se PoesiaDAO(ID) non carica il titolo, qui lo leggiamo esplicitamente
                    String titolo = rs.getString("Titolo"); 
                    int cuori = rs.getInt("NumCuori");
                    PoesiaDAO topPDAO = new PoesiaDAO(id); // Carica i dati rimanenti
                    topPDAO.setTitolo(titolo);
                    topPDAO.setCuore(cuori); 
                    return topPDAO;
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        
        
        //FATTO
}

    
    
    
    
    
    
    
    
    
    
    
    

