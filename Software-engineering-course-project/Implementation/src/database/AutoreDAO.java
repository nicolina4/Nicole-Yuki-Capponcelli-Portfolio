package database;


import java.util.ArrayList;
import java.util.Arrays;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import java.sql.Timestamp;



public class AutoreDAO extends UtenteDAO{
    private String nome;
    private String cognome;
    private String bio;
    private String immagineProfilo; //per path del file immagine o URL
    private ArrayList<PoesiaDAO> PoesiePubblicate;
    private ArrayList<RaccoltaDAO> RaccolteAutore;
    
    //costruttotre di default
    public AutoreDAO(){
        
        super();
        this.PoesiePubblicate=new ArrayList<PoesiaDAO>();
        this.RaccolteAutore=new ArrayList<RaccoltaDAO>();
    }
    
    //costruttore da superclasse
    public AutoreDAO(long ID,String email, String password,LocalDateTime dataRegistrazione, String nome, String cognome, 
            String bio, String immagineProfilo, ArrayList<PoesiaDAO> PoesiePubblicate, ArrayList<RaccoltaDAO> RaccolteAutore){
        super(ID,email,password,dataRegistrazione);
        this.nome=nome;
        this.cognome=cognome;
        this.bio=bio;
        this.immagineProfilo=immagineProfilo;
        this.PoesiePubblicate=new ArrayList<PoesiaDAO>();
        this.RaccolteAutore=new ArrayList<RaccoltaDAO>();
    }
    
    
    
    
    //costruttore per inizializzare tramite chiave primaria
    
    public AutoreDAO(long IDUtenteD){
        super();
        
        this.IDUtenteD=IDUtenteD;
        
        this.PoesiePubblicate=new ArrayList<PoesiaDAO>();
        this.RaccolteAutore=new ArrayList<RaccoltaDAO>();
        
        
        
        readUtente();
        readListaPoesiePubblicate();
        readListaRaccolteAutore();
    }
    
    //costruttore per inizializzare tramite email
    public AutoreDAO(String email){
        super();
        this.PoesiePubblicate=new ArrayList<PoesiaDAO>();
        this.RaccolteAutore=new ArrayList<RaccoltaDAO>();
        this.email=email;
        readUtenteByEmail();
        readListaPoesiePubblicate();
        
    }
    
    //costruttore per inizializzare tramite oggetto DAO gia definito
    
    public AutoreDAO(AutoreDAO ADAO){
        this.PoesiePubblicate=new ArrayList<PoesiaDAO>();
        this.RaccolteAutore=new ArrayList<RaccoltaDAO>();

        this.IDUtenteD=ADAO.getIDUtenteD();
        this.email=ADAO.getEmail();
        this.password=ADAO.getPassword();
        this.dataRegistrazione=ADAO.getDataRegistrazione();
        this.nome=ADAO.getNome();
        this.cognome=ADAO.getCognome();
        this.bio=ADAO.getBio();
        this.immagineProfilo=ADAO.getImmagineProfilo();
        this.PoesiePubblicate=ADAO.getListaPoesiePubblicate();
        this.RaccolteAutore=ADAO.getListaRaccolteAutore();
    }
    
    //metodi get
    public String getNome(){
        return this.nome;
    }
    
    public String getCognome(){
        return this.cognome;
    }
    
    public ArrayList<PoesiaDAO> getListaPoesiePubblicate(){
        return this.PoesiePubblicate;
    }
    
    public ArrayList<RaccoltaDAO> getListaRaccolteAutore(){
        return this.RaccolteAutore;
    }
    
    public String getBio(){
        return this.bio;
    }
    
    public String getImmagineProfilo(){
        return this.immagineProfilo;
    }
    
    //metodi set
    public void setNome(String nome){
        this.nome=nome;
    }
    
    public void setCognome(String cognome){
        this.cognome=cognome;
    }
    
    public void setPoesiePubblicate(ArrayList<PoesiaDAO> PoesiePubblicate){
        this.PoesiePubblicate=PoesiePubblicate;
    }
    
    public void setRaccolteAutore(ArrayList<RaccoltaDAO> RaccolteAutore){
        this.RaccolteAutore=RaccolteAutore;
    }
    
    public void setBio(String bio){
        this.bio=bio;
    }
    
    public void setImmagineProfilo(String immagineProfilo){
        this.immagineProfilo=immagineProfilo;
    }
    
    //crea autore
@Override
public boolean createUtente(String email, String password) {
    boolean created = super.createUtente(email, password);
    
    if (!created) {
        return false;
    }

    // Ora this.IDUtenteD contiene l'ID generato dal database
    int rows = 0;
    String query = "INSERT INTO Autori (IDUtente, Nome, Cognome, Bio, ImmagineProfilo) VALUES (" +
                   this.IDUtenteD + ", '" + nome + "', '" + cognome + "', '" + bio + "', '" + immagineProfilo + "')";
    
    try {
        rows = DBConnectionManager.updateQuery(query);
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
    
    return rows > 0;
}

    
    //aggiorna autore richisando update della superclasse
    @Override
    public boolean updateUtente(){
        String query="UPDATE Utenti SET Email = '"+this.email+"', Password = '"+this.password+
                   "' WHERE ID = "+this.IDUtenteD;

        String queryAutore="UPDATE Autori SET Nome = '"+this.nome+"', Cognome = '"+this.cognome+
                         "', Bio = '"+this.bio+"', ImmagineProfilo = '"+this.immagineProfilo+
                         "' WHERE IDUtente = "+this.IDUtenteD;
        try{
            int rowsUtente=DBConnectionManager.updateQuery(query);
            int rowsAutore=DBConnectionManager.updateQuery(queryAutore);
            return rowsUtente>0 && rowsAutore>0;
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    
    //elimina autore
    @Override
    public boolean deleteUtente(){
        try{
            int rowsAutore=DBConnectionManager.updateQuery(
                "DELETE FROM Autori WHERE IDUtente = "+this.IDUtenteD);
            int rowsUtente=DBConnectionManager.updateQuery(
                "DELETE FROM Utenti WHERE ID = "+this.IDUtenteD);
            return rowsAutore>0 && rowsUtente>0;
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    

    
    //per leggere da DB tramite ID
    @Override 
    public void readUtente(){
        super.readUtente(); //si legge da Utenti
        readAutore(); //si legge da Autori
}

    //lettura dal DB (modifica readAutore senza cambiare gli altri campi)
    public void readAutore(){
        String query="SELECT * FROM Autori WHERE IDUtente = "+this.IDUtenteD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.nome=rs.getString("Nome");
                this.cognome=rs.getString("Cognome");
                this.bio=rs.getString("Bio");                        // nuovo campo
                this.immagineProfilo=rs.getString("ImmagineProfilo"); // nuovo campo
            }
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    //lettura di un utente tramite email, ritorna true se esiste senno false
    //fare con override in autore e amministratore
    @Override
    public boolean readUtenteByEmail(String email){
        String query="SELECT * FROM Utenti WHERE email='"+this.email+"'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                String readEmail=rs.getString("Email");
                String readNome=rs.getString("Nome");
                String readCognome=rs.getString("Cognome");
                
                
                
                
                
                return email.equalsIgnoreCase(readEmail)&&nome.equalsIgnoreCase(readNome)&&
                        cognome.equalsIgnoreCase(readCognome);
            }
            
        }catch(SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //lettura di un utente tramite email
 //lettura di un utente tramite email
//lettura di un utente tramite email
@Override
public void readUtenteByEmail(){
    // ✅ QUERY CORRETTA - specifica la condizione di JOIN
    String query = "SELECT U.*, A.Nome, A.Cognome, A.Bio, A.ImmagineProfilo " +
                   "FROM Utenti U JOIN Autori A ON U.ID = A.IDUtente " +
                   "WHERE U.email = '" + this.email + "'";
    
    try{
        ResultSet rs = DBConnectionManager.selectQuery(query);
        if(rs.next()){
            // ✅ CORREGGI I NOMI DELLE COLONNE - ora sono disponibili sia da Utenti che Autori
            this.setIDUtenteD(rs.getLong("ID")); // Da Utenti
            this.setPassword(rs.getString("Password")); // Da Utenti
            this.setNome(rs.getString("Nome")); // Da Autori
            this.setCognome(rs.getString("Cognome")); // Da Autori
            this.setBio(rs.getString("Bio")); // Da Autori
            this.setImmagineProfilo(rs.getString("ImmagineProfilo")); // Da Autori
            
            System.out.println("✅ Utente trovato: " + this.getNome() + " " + this.getCognome());
        } else {
            System.out.println("❌ Nessun utente trovato con email: " + this.email);
        }      
    } catch(SQLException | ClassNotFoundException e){
        e.printStackTrace();
    }
    
    caricaPoesieDaDB();
    caricaRaccolteDaDB();
}


public void caricaPoesieDaDB(){
    String query= "SELECT * FROM Poesie WHERE IDUtente="+this.IDUtenteD;
        
        try{
            ResultSet rs2=DBConnectionManager.selectQuery(query);
            while(rs2.next()){
                long IDPoesia=rs2.getLong("ID");
                String TitoloPoesia=rs2.getString("Titolo");
                String TestoPoesia=rs2.getString("Testo");
                
                int numCuori=rs2.getInt("NumCuori");
                
                ArrayList ListaTag=new ArrayList<String>();
                String query3="SELECT t.NomeTag FROM Tag t"+" JOIN PoesiaTag pt ON t.IDTag=pt.IDTag "+"WHERE pt.IDPoesia="+IDPoesia;
                
                try{
                    ResultSet rs3=DBConnectionManager.selectQuery(query3);
                    
                    while(rs3.next()){
                        String tag=rs3.getString("NomeTag");
                        ListaTag.add(tag);
                    }
                }catch(SQLException|ClassNotFoundException e){
                    e.printStackTrace();
                }
                
                boolean Pubblica=rs2.getBoolean("Pubblica");
                
                Timestamp timestamp = rs2.getTimestamp("DataPubblicazione");
                LocalDateTime dataPubblicazionePoesia= timestamp.toLocalDateTime();

                
                String query4="SELECT * FROM Commenti WHERE IDPoesia="+IDPoesia;
                ArrayList ListaCommenti=new ArrayList<CommentoDAO>();
                try{
                    ResultSet rs4=DBConnectionManager.selectQuery(query4);
                    
                    while(rs4.next()){
                        long IDCommento=rs4.getLong("ID");
                        String Testo=rs4.getString("Testo");
                        Timestamp timestamp2 = rs4.getTimestamp("DataPubblicazione");
                        LocalDateTime dataPubblicazioneCommento = timestamp2.toLocalDateTime();
                        
                        long IDAutoreCommento=rs4.getLong("AutoreCommento");

                        
                
                        CommentoDAO C=new CommentoDAO(IDCommento, Testo, dataPubblicazioneCommento,IDAutoreCommento);
                        
                        ListaCommenti.add(C);
                    }
                }catch(SQLException|ClassNotFoundException e){
                    e.printStackTrace();
                } 
                
                
                PoesiaDAO P=new PoesiaDAO(IDPoesia,TitoloPoesia,TestoPoesia,Pubblica,dataPubblicazionePoesia, ListaTag, ListaCommenti);
                P.setCuore(numCuori);
                this.PoesiePubblicate.add(P);
            }
        }catch(SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }
}

public void caricaRaccolteDaDB(){
    String query="SELECT * FROM Raccolte WHERE IDUtente="+this.IDUtenteD;
        try{
                    ResultSet rs5=DBConnectionManager.selectQuery(query);
                    
                    while(rs5.next()){
                        long IDRaccolta=rs5.getLong("ID");
                        String titolo=rs5.getString("Titolo");
                        String descrizione=rs5.getString("Descrizione");

                        RaccoltaDAO R=new RaccoltaDAO(IDRaccolta, titolo, descrizione);            
               
                        
                        
                        this.RaccolteAutore.add(R);
                    }
                }catch(SQLException|ClassNotFoundException e){
                    e.printStackTrace();
                } 
}

    //controllo esistenza di un utente tramite email
    //per poi vedere i suoi dati
    @Override
    public boolean readUtenteByAllData(String email, String password){
        String query ="SELECT * FROM Utenti U JOIN Autori A ON U.ID=A.IDUtente WHERE email= '"+this.email+"' AND password='"+this.password+"'"; //+"' AND nome='"+this.nome+"' AND cognome='"+this.cognome+"'";
        
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                String readEmail=rs.getString("email");
                String readPassword=rs.getString("password");
                String readNome=rs.getString("nome");
                String readCognome=rs.getString("cognome");
                String readBio=rs.getString("Bio");
                String readImmagineProfilo=rs.getString("immagineprofilo");
                
                this.setNome(readNome);
                this.setCognome(readCognome);
                this.setBio(readBio);
                return email.equalsIgnoreCase(readEmail)&&password.equalsIgnoreCase(readPassword);
                       //&&nome.equalsIgnoreCase(readNome)&&cognome.equalsIgnoreCase(readCognome)&&
                       // bio.equalsIgnoreCase(readBio)&&immagineProfilo.equalsIgnoreCase(readImmagineProfilo);
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    //lettura della lista di poesie pubblicate da un autore nel DB
   public void readListaPoesiePubblicate() {
    this.PoesiePubblicate = new ArrayList<PoesiaDAO>();
    String query = "SELECT * FROM Poesie WHERE IDUtente = " + this.IDUtenteD;
    
    
    
    try {
        ResultSet rs = DBConnectionManager.selectQuery(query);
        int count = 0;
        
        while (rs.next()) {
            PoesiaDAO PDAO = new PoesiaDAO();
            
            PDAO.setIDPoesiaD(rs.getLong("ID"));
            PDAO.setTitolo(rs.getString("Titolo"));
            PDAO.setTesto(rs.getString("Testo"));
            PDAO.setPubblica(rs.getBoolean("Pubblica"));
            
            Timestamp ts = rs.getTimestamp("DataPubblicazione");
            if (ts != null) {
                PDAO.setDataPubblicazione(ts.toLocalDateTime());
            }
            
            ArrayList<String> tags=new ArrayList<>();
            String tagQuery="SELECT t.NomeTag FROM Tag t"+" JOIN PoesiaTag pt ON t.IDTag=pt.IDTag "+
                    "WHERE pt.IDPoesia="+PDAO.getIDPoesiaD();
            
            ResultSet rs1=DBConnectionManager.selectQuery(tagQuery);
            while(rs1.next()){
                tags.add(rs1.getString("NomeTag"));
            }
            
            PDAO.setTag(tags);
            
            
            
            
            // AGGIUNGI: Carica il numero di cuori
            PDAO.setCuore(rs.getInt("NumCuori"));
            
            this.PoesiePubblicate.add(PDAO);
            count++;
            
            
        }
        
        
        
    } catch (ClassNotFoundException | SQLException e) {
        System.err.println("❌ Errore caricamento poesie: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    //lettura della lista di raccolte di un autore nel DB
    public void readListaRaccolteAutore() {
    this.RaccolteAutore = new ArrayList<RaccoltaDAO>();
    String query = "SELECT * FROM Raccolte WHERE IDUtente = " + this.IDUtenteD;
    
   
    
    try {
        ResultSet rs = DBConnectionManager.selectQuery(query);
        int count = 0;
        
        while (rs.next()) {
            RaccoltaDAO RDAO = new RaccoltaDAO();
            RDAO.setIDRaccoltaD(rs.getLong("ID"));
            RDAO.setTitolo(rs.getString("Titolo"));
            RDAO.setDescrizione(rs.getString("Descrizione"));
            this.RaccolteAutore.add(RDAO);
            count++;
            
            
        }
        
        
        
    } catch (ClassNotFoundException | SQLException e) {
        System.err.println("❌ Errore caricamento raccolte: " + e.getMessage());
        e.printStackTrace();
    }
}
    
  
// ✅ AGGIUNGI QUESTO METODO PER SALVARE I TAG
public void salvaTagPoesia(long idPoesia, ArrayList<String> tags) {
    if (tags == null || tags.isEmpty()) return;
    
    try {
        for (String tag : tags) {
            // 1. Verifica se il tag esiste già, altrimenti crealo
            String checkTagQuery = "SELECT IDTag FROM Tag WHERE NomeTag = '" + tag + "'";
            ResultSet rs = DBConnectionManager.selectQuery(checkTagQuery);
            
            long idTag;
            if (rs.next()) {
                // Tag esistente
                idTag = rs.getLong("IDTag");
            } else {
                // Crea nuovo tag
                String insertTagQuery = "INSERT INTO Tag (NomeTag) VALUES ('" + tag + "')";
                Integer generatedId = DBConnectionManager.updateQueryReturnGeneratedKey(insertTagQuery);
                idTag = generatedId != null ? generatedId.longValue() : -1;
            }
            
            // 2. Collega il tag alla poesia
            if (idTag > 0) {
                String insertPoesiaTagQuery = "INSERT INTO PoesiaTag (IDPoesia, IDTag) VALUES (" + 
                                            idPoesia + ", " + idTag + ")";
                DBConnectionManager.updateQuery(insertPoesiaTagQuery);
            }
        }
    } catch (ClassNotFoundException | SQLException e) {
        System.err.println("Errore nel salvataggio dei tag: " + e.getMessage());
        e.printStackTrace();
    }
}


    //toString
    @Override
    public String toString(){
        String result="\nNome: "+nome+
	              "\nCognome: "+cognome+
	              "\nBio: "+bio+
	              "\nImmagine profilo: "+immagineProfilo;
	if(!PoesiePubblicate.isEmpty()){
            result+="\\nLista delle poesie pubblicate:\\n";
            for(PoesiaDAO PDAO:PoesiePubblicate){
                result+="\nTitolo poesia: "+PDAO.getTitolo()+
	                "\nTesto: "+PDAO.getTesto()+
	                "\nTag: "+PDAO.getTag()+
	                "\nPubblica: "+PDAO.getPubblica()+
                        "\nData pubblicazione: "+PDAO.getDataPubblicazione();
            }
        }else{
            result+="L'autore non ha pubblicato poesie";
        }
        if(!RaccolteAutore.isEmpty()){
            result+="\nTLista raccolte dell'autore:\\n";
            for(RaccoltaDAO RDAO:RaccolteAutore){
                result+="\nTitolo raccolta: "+RDAO.getTitolo()+
	                "\nDescrizione: "+RDAO.getDescrizione();
            }
        }else{
            result+="L'autore non creato raccolte";
        }
        return super.toString()+result;
    }
    
    
    
    
    //FATTO
}

