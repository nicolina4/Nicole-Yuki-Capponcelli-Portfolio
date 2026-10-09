package database;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

import exception.NoDirectorFoundException;

public class CatenaAlberghieraDAO {

	private int codice;
	private String nome;
	private String emailDirettore;
	private ArrayList<AlbergoDAO> listaAlberghi;
	
	/**
	 * Costruttore di default
	 * */
	public CatenaAlberghieraDAO() {
		super();
		
		this.listaAlberghi = new ArrayList<AlbergoDAO>();
	}

	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param codice Codice della catena alberghiera
	 * */
	public CatenaAlberghieraDAO(int codice) {
		super();
		
		this.codice = codice;
		
		readCatenaAlberghiera();
		readListaAlberghi();
	}
	
	/**
	 * Costruttore: inizializzazione tramite l'e-mail del direttore
	 * 
	 * @apiNote L'e-mail del direttore nel database è unique
	 * 
	 * @param emailDirettore L'e-mail del direttore
	 * */
	public CatenaAlberghieraDAO(String emailDirettore) throws NoDirectorFoundException {
		super();
		
		if(!hasDirector(emailDirettore)) {
			throw new NoDirectorFoundException();
		}
		
		this.emailDirettore = emailDirettore;
		
		readCatenaAlberghieraByEmail();
		
		this.listaAlberghi = new ArrayList<AlbergoDAO>();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param catena Catena alberghiera già costruita
	 * */
	public CatenaAlberghieraDAO(CatenaAlberghieraDAO catena) {
		this.codice = catena.getCodice();
		this.nome = catena.getNome();
		this.emailDirettore = catena.getEmailDirettore();
		this.listaAlberghi = catena.getListaAlberghi();
	}
	
	/**
	 * READ: Lettura da database della catena alberghiera tramite il codice
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCatenaAlberghiera() {
		String query = "SELECT * FROM CateneAlberghiere WHERE codice = " + this.codice;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setNome(rs.getString("nome"));
				this.setEmailDirettore(rs.getString("emailDirettore"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della catena alberghiera tramite l'e-mail del direttore
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readCatenaAlberghieraByEmail() {
		String query = "SELECT * FROM CateneAlberghiere WHERE emailDirettore = '" + this.emailDirettore + "'";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			if(rs.next()) {
				this.setCodice(rs.getInt("codice"));
				this.setNome(rs.getString("nome"));
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * READ: Lettura da database della lista di alberghi relativi a una catena alberghiera
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * */
	public void readListaAlberghi() {
		this.listaAlberghi = new ArrayList<AlbergoDAO>();
		
		String query = "SELECT * FROM Alberghi WHERE codiceCatenaAlberghiera = " + this.codice;
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
			
			while(rs.next()){
				// Creazione dell'istanza di AlbergoDAO
				AlbergoDAO albergo = new AlbergoDAO();
				
				albergo.setCatenaAlberghiera(this);
				
				albergo.setIdentificativo(rs.getInt("identificativo"));
				albergo.setNome(rs.getString("nome"));
				albergo.setCitta(rs.getString("citta"));
				albergo.setIndirizzo(rs.getString("indirizzo"));
				albergo.setCAP(rs.getString("CAP"));
				albergo.setNumeroDiTelefono(rs.getString("numeroDiTelefono"));
				
				albergo.readListaCamere();
				
				this.listaAlberghi.add(albergo);
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Funzione per verificare che la catena alberghiera abbia il direttore inserito
	 * 
	 * @param email L'e-mail del direttore da controllare
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return True se l'e-mail è nel sistema, false altrimenti
	 * */
	public boolean hasDirector(String email) {
		String query = "SELECT * FROM CateneAlberghiere WHERE emailDirettore = '" + email + "'";
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
	        
	        if(rs.next()) {
	        	return true;
	        }
	        
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	/**
	 * Stampa la lista delle camere prenotate per ogni mese
	 * 
	 * @throws ClassNotFoundException Gestione di un'eventuale abort della query
	 * @throws SQLException Gestione di un'eventuale abort della query
	 * 
	 * @return Report elenco notti generato tramite i risultati della query
	 * */
	public String reportToString() {
		// DATEDIFF è un operatore per calcolare la differenza tra la data partenza e la data arrivo
		// Restituisce un intero che rappresenta la differenza
		String query = "SELECT Ca.identificativo AS numeroStanza, DATEDIFF(Pr.dataPartenza, Pr.dataArrivo) AS numeroNotti "
				+ "	FROM ( CAMERE Ca JOIN PRENOTAZIONI Pr ON Ca.identificativo = Pr.numeroStanza )"
				+ "	JOIN ALBERGHI Al ON Ca.identificativoAlbergo = Al.identificativo"
				+ "	WHERE MONTH(Pr.dataArrivo) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)"
				+ "	AND YEAR(Pr.dataArrivo) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH)"
				+ "	AND Al.codiceCatenaAlberghiera = " + this.codice;
		
		ArrayList<Integer> risultati = new ArrayList<Integer>();
		
		try {
			ResultSet rs = DBConnectionManager.selectQuery(query);
				
			while(rs.next()) {
				Integer numeroStanza = rs.getInt("numeroStanza");
				Integer numeroNotti = rs.getInt("numeroNotti");
					
				risultati.add(numeroStanza);
				risultati.add(numeroNotti);
			}
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String report = "Report per la catena alberghiera: " + this.nome + "\n";
		
		if(!risultati.isEmpty()) {
			for(int i = 0; i < risultati.size() - 1; i++) {
				report += "\n- NUMERO STANZA: " + risultati.get(i) + "\n- Numero notti: " + risultati.get(i+1) + "\n";
				
				i++;
			}
		}else {
			report += "\nSiamo spiacenti, ma non vi sono prenotazioni relative alla catena alberghiera "
					+ this.nome + ": il report non può essere generato";
		}
		
		return report;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmailDirettore() {
		return emailDirettore;
	}

	public void setEmailDirettore(String emailDirettore) {
		this.emailDirettore = emailDirettore;
	}

	public ArrayList<AlbergoDAO> getListaAlberghi() {
		return listaAlberghi;
	}

	public void setListaAlberghi(ArrayList<AlbergoDAO> listaAlberghi) {
		this.listaAlberghi = listaAlberghi;
	}

	@Override
	public String toString() {
	   
	    String result = "\nCodice: " + codice +
	    				"\nNome: " + nome +
	    				"\nEmail Direttore: " + emailDirettore;
	    				
	    if(!listaAlberghi.isEmpty()) {
	    	result += "\n\nLista di alberghi:\n\n";
	    	
	    	for (AlbergoDAO albergo : listaAlberghi) {
		        result += "Nome: " + albergo.getNome() +
		        		  "\nCitta: " + albergo.getCitta() +
		        		  "\nIndirizzo: " + albergo.getIndirizzo() +
		        		  "\n\n";
		    }
	    }

	  return result; 
	}
	
}