package database;

import DTO.AutoreAttivitaDTO;
import DTO.ReportIntervalloDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class AmministratoreDAO extends UtenteDAO{

    //costruttore da superclasse
    public AmministratoreDAO(long ID, String email, String password,LocalDateTime dataRegistrazione){
        super(ID, email, password,dataRegistrazione);
    }
    
    //costruttore di default
    public AmministratoreDAO(){
        super();
    }
    
    //costruttore per inizializzare tramite oggetto DAO gia definito
    public AmministratoreDAO(AmministratoreDAO ADAO){
        this.IDUtenteD=ADAO.getIDUtenteD();
        this.email=ADAO.getEmail();
        this.password=ADAO.getPassword();
        this.dataRegistrazione=ADAO.getDataRegistrazione();
    }
    
    //ccostruttore per AmministratoreBoundary
    public AmministratoreDAO(long ID){
        this.IDUtenteD=ID;
    }
    
    //creazione nuovo amministratore nel DB
    @Override
public boolean createUtente(String email, String password) {
    int rows = 0;
    
    // Query senza ID specificato
    String query = "INSERT INTO Utenti (Email, Password, Ruolo) VALUES (LOWER('" + email + "'), '" + password + "', 'amministratore')";
    
    try {
        Integer generatedID = DBConnectionManager.updateQueryReturnGeneratedKey(query);
        if (generatedID != null) {
            this.IDUtenteD = generatedID.longValue();
            this.email = email;
            this.password = password;
            rows = 1;
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
    
    return rows > 0;
}

    //per aggiornare dati dell'amministratore nel DB
    @Override
    public boolean updateUtente(){
        String query="UPDATE Utenti SET Email='"+this.email+"', Password='"+this.password+ 
                       "' WHERE ID="+this.IDUtenteD;
        try{
            int rows=DBConnectionManager.updateQuery(query);
            return rows>0;
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // per eliminare un amministratore dal DB
    @Override
    public boolean deleteUtente(){
        try{
            int rows=DBConnectionManager.updateQuery("DELETE FROM Utenti WHERE ID="+this.IDUtenteD);
            return rows>0;
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //per leggere i dati dell'amministratore
    @Override
    public void readUtente(){
        String query="SELECT * FROM Utenti WHERE ID="+this.IDUtenteD+" AND Ruolo='amministratore'";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            if(rs.next()){
                this.email=rs.getString("Email");
                this.password=rs.getString("Password");
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    //per ottenere tutti gli utenti registrati (autori e non)
    public ArrayList<UtenteDAO> readAllUtenti(){
        ArrayList<UtenteDAO> listaUtenti=new ArrayList<>();
        String query="SELECT * FROM Utenti";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            while(rs.next()){
                UtenteDAO UDAO=new UtenteDAO();
                UDAO.setIDUtenteD(rs.getLong("ID"));
                UDAO.setEmail(rs.getString("Email"));
                UDAO.setPassword(rs.getString("Password"));
                listaUtenti.add(UDAO);
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return listaUtenti;
    }

    /*
    //per eliminare un utente qualsiasi (non l’amministratore)
    public boolean eliminaUtenteByID(long idUtente){
        try{
            int rows=DBConnectionManager.updateQuery("DELETE FROM Utenti WHERE ID="+idUtente);
            return rows>0;
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    */
    //per genrare report, 3 query diverse cosi
    //amministratore puo scegliere su cosa generare report
    
    public void generaReport(String tipoReport, Timestamp dataInizio, Timestamp dataFine){
        switch(tipoReport.toLowerCase()){
            case "Poesie in un certo intervallo":
                generaReportPoesieInIntervallo(dataInizio, dataFine);
                break;
            case "Autori piu attivi":
                generaReportAutoriPiuAttivi();
                break;
            case "Tag piu usati":
                generaReportTagPiuUsati();
                break;
            case "Poesie con piu interazioni":
                generaReportPoesiePiuInterazioni();
                break;
            default:
                System.out.println("Tipo di report non riconosciuto.");
        }
    }

    //1: poesie pubblicate in un certo intervallo
    public ReportIntervalloDTO generaReportPoesieInIntervallo(Timestamp inizio, Timestamp fine){
        String query = "SELECT COUNT(*) AS totPoesie FROM Poesie WHERE DataPubblicazione BETWEEN ? AND ?";
        try {
            // Usa PreparedStatement per gestire Timestamp in sicurezza
            ResultSet rs = DBConnectionManager.selectQueryWithParams(query, inizio, fine);
            if (rs.next()) {
                return new ReportIntervalloDTO(rs.getInt("totPoesie"));
            }
        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return new ReportIntervalloDTO(0);
    }

    //2: autori piu attivi
    public ArrayList<AutoreAttivitaDTO> generaReportAutoriPiuAttivi(){
        ArrayList<AutoreAttivitaDTO> lista = new ArrayList<>();
        String query = "SELECT a.Nome, a.Cognome, COUNT(p.ID) AS NumPoesie FROM Autori a " +
                       "JOIN Poesie p ON a.IDUtente = p.IDUtente GROUP BY a.IDUtente " +
                       "ORDER BY NumPoesie DESC LIMIT 5";
        try {
            ResultSet rs = DBConnectionManager.selectQuery(query);
            while(rs.next()){
                String nomeCompleto = rs.getString("Nome") + " " + rs.getString("Cognome");
                lista.add(new AutoreAttivitaDTO(nomeCompleto, rs.getInt("NumPoesie")));
            }
        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    //3: tag piu utilizzati
    public ArrayList<String> generaReportTagPiuUsati(){
        ArrayList<String> listaTag = new ArrayList<>();
        String query = "SELECT t.NomeTag, COUNT(pt.IDPoesia) AS NumUtilizzi FROM Tag t " +
                       "JOIN PoesiaTag pt ON t.IDTag = pt.IDTag GROUP BY t.NomeTag " +
                       "ORDER BY NumUtilizzi DESC LIMIT 10";
        try {
            ResultSet rs = DBConnectionManager.selectQuery(query);
            while(rs.next()){
                listaTag.add(rs.getString("NomeTag") + " (" + rs.getInt("NumUtilizzi") + " utilizzi)");
            }
        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return listaTag;
    }

    //4: poesie con piu onterazioni
    public ArrayList<String> generaReportPoesiePiuInterazioni(){
        ArrayList<String> listaPoesie = new ArrayList<>();
        // ✅ QUERY CORRETTA:
        // Utilizziamo P.NumCuori (valore pre-calcolato) invece di contare le righe di Cuori.
        String query = "SELECT p.Titolo, p.NumCuori, " +
                       " COUNT(DISTINCT c.ID) AS NumCommenti, " +
                       " (p.NumCuori + COUNT(DISTINCT c.ID)) AS TotInterazioni " +
                       " FROM Poesie p " +
                       " LEFT JOIN Commenti c ON p.ID = c.IDPoesia " +
                       " GROUP BY p.ID, p.Titolo, p.NumCuori " + 
                       " ORDER BY TotInterazioni DESC LIMIT 5";
        try {
            ResultSet rs = DBConnectionManager.selectQuery(query);
            while(rs.next()){
                int numCuoriCorretti = rs.getInt("NumCuori"); // Leggiamo il valore corretto
                int numCommenti = rs.getInt("NumCommenti");
                int totInterazioni = rs.getInt("TotInterazioni"); // Il calcolo TotInterazioni è ora corretto
                // Formattazione della stringa di output
                String risultato = rs.getString("Titolo") + " → " + totInterazioni + " interazioni (" +
                                   numCuoriCorretti + " cuori, " + numCommenti + " commenti)";
                listaPoesie.add(risultato);
            }
        } catch(ClassNotFoundException | SQLException e) {
            System.err.println("❌ Errore DAO Report Poesie Interagite: " + e.getMessage());
            e.printStackTrace();
        }
        return listaPoesie;
    }
    
    //per visualizzare tutti gli autori
    public ArrayList<AutoreDAO> readAllAutori(){
        ArrayList<AutoreDAO> listaAutori=new ArrayList<>();
        String query="SELECT * FROM Utenti JOIN Autori ON Utenti.ID = Autori.IDUtente";
        try{
            ResultSet rs=DBConnectionManager.selectQuery(query);
            while(rs.next()){
                AutoreDAO ADAO= new AutoreDAO();
                ADAO.setIDUtenteD(rs.getLong("ID"));
                ADAO.setEmail(rs.getString("Email"));
                ADAO.setPassword(rs.getString("Password"));
                ADAO.setNome(rs.getString("Nome"));
                ADAO.setCognome(rs.getString("Cognome"));
                listaAutori.add(ADAO);
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return listaAutori;
    }
    
    //FATTO

}
