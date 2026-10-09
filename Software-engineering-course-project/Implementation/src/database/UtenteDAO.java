package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.time.LocalDateTime;

public class UtenteDAO{
    protected long IDUtenteD;
    protected String email;
    protected String password;
    protected LocalDateTime dataRegistrazione;
    
    //costrutttore di default
    public UtenteDAO(){
        super();
    }
    
    //costruttore per sottoclassi
    public UtenteDAO(long IDUtenteD,String email,String password,LocalDateTime dataRegistrazione){
        this.IDUtenteD=IDUtenteD;
        this.email=email;
        this.password=password;
        this.dataRegistrazione=dataRegistrazione;
    }
    
    //costruttore per inizializzare tramite chiave primaria
    
    public UtenteDAO(long IDUtenteD){
        super();
        this.IDUtenteD=IDUtenteD;
        
        readUtente();
        //in autore si mette readPoesiePubblicate
    }
    
    //costruttore per inizializzare tramite email
    public UtenteDAO(String email){
        super();
        this.email=email;
        readUtenteByEmail();
        //in autore si mette readPoesiePubblicate
        
    }
    
    //costruttore per inizializzare tramite oggetto DAO gia definito
    
    public UtenteDAO(UtenteDAO UDAO){
        this.IDUtenteD=UDAO.getIDUtenteD();
        this.email=UDAO.getEmail();
        this.password=UDAO.getPassword();
        this.dataRegistrazione=UDAO.getDataRegistrazione();
    }
    
    //metodi get
    public long getIDUtenteD(){
        return this.IDUtenteD;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public LocalDateTime getDataRegistrazione(){
        return this.dataRegistrazione;
    }
    
    //metodi set
    public void setIDUtenteD(long IDUtenteD){
        this.IDUtenteD=IDUtenteD;
    }
    
    public void setEmail(String email){
        this.email=email;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    
    public void setDataRegistrazione(LocalDateTime dataRegistrazione){
        this.dataRegistrazione=dataRegistrazione;
    }
    
    
    //creazione nuov ID per nuovo utente
  // Sostituisci il metodo getNuovoID con:
public long getNuovoID() {
    return -1; // Lascia che il database gestisca l'auto-increment
}

// Modifica createUtente:
public boolean createUtente(String email, String password) {
    int rows = 0;
    
    // Query aggiornata senza specificare ID
    String query = "INSERT INTO Utenti (Email, Password) VALUES (LOWER('" + email + "'), '" + password + "')";

    try {
        // Usa il metodo che restituisce la chiave generata
        Integer generatedID = DBConnectionManager.updateQueryReturnGeneratedKey(query);
        if (generatedID != null) {
            this.setIDUtenteD(generatedID.longValue());
            rows = 1;
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }

    return rows > 0;
}
    
    //aggiorna tabrlla utenti tramite ID
    public boolean updateUtente(){
        int rows=0;
        String query="UPDATE Utenti SET email = LOWER('"+this.email+"'), password = '"+this.password+"' WHERE ID = "+this.IDUtenteD;

        try{
            rows=DBConnectionManager.updateQuery(query);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        //return rows>0;
        if(rows>0){
            return true;
        }else{
            return false;
        }
    }

    
    //elimina utente
    public boolean deleteUtente(){
        int rows=0;
        String query="DELETE FROM Utenti WHERE ID = "+this.IDUtenteD;

        try{
            rows=DBConnectionManager.updateQuery(query);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        return rows>0;
    }

    
    //per leggere da DB tramite ID
    //fare con override in autore e amministratore
    public void readUtente(){
        String query="SELECT * FROM Utenti WHERE ID="+this.IDUtenteD;
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.setEmail(rs.getString("Email"));
                this.setPassword(rs.getString("Password"));
            }
            
        }catch(SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    
    

    
    //lettura di un utente tramite email, ritorna true se esiste senno false
    //fare con override in autore e amministratore
    public boolean readUtenteByEmail(String email){
        String query="SELECT * FROM Utenti WHERE email='"+this.email+"'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                String readEmail=rs.getString("email");
                return email.equalsIgnoreCase(readEmail);
            }
            
        }catch(SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //lettura di un utente tramite email
    //fare con override in autore e amministratore
    public void readUtenteByEmail(){
        String query="SELECT * FROM Utenti WHERE email='"+this.email+"'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.setIDUtenteD(rs.getLong("ID"));
                this.setPassword(rs.getString("Password"));
            }
            
        }catch(SQLException|ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    //controllo esistenza di un utente tramite email
    //per poi vedere i suoi dati
    //fare con override in autore e amministratore
    public boolean readUtenteByAllData(String email, String password){
        String query ="SELECT * FROM Utenti WHERE email= '"+this.email+"' AND password='"+this.password+"'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                String readEmail=rs.getString("email");
                String readPassword=rs.getString("password");
                return email.equalsIgnoreCase(readEmail)&&password.equalsIgnoreCase(readPassword);
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    //controllo se una mail e gia collegata a un utente
    public boolean checkEmailRegistrata(String email){
        String emailCheck=new String();
        String query ="SELECT email FROM Utenti WHERE email='"+this.email+"'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                emailCheck=rs.getString("email");
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
        return emailCheck.equals(email);
    }
    
    //controlla unicita utente
    public boolean checkUniqueEmail(String email){
        String emailCheck=new String();
        String query="SELECT email FROM Utenti WHERE email='"+this.email+"'";
        
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            
            if(rs.next()){
                emailCheck=rs.getString("email");
            }
        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
        return emailCheck.equals(email);
    }
    
    
    @Override
    //da fare anche in autore e amminisrtatore
    public String toString(){
        String result="\nEmail: "+this.email+
	              "\nPassword: "+this.password+
                      "\nData registrazione: "+this.dataRegistrazione;
        return result;
    }

    
    
    


}
