package entity;

import java.util.ArrayList;

import database.AlbergoDAO;
import database.CatenaAlberghieraDAO;

import exception.NoDirectorFoundException;
import exception.NoRoomException;

public class EntityCatenaAlberghiera {

	private int codice;
	private String nome;
	private String emailDirettore;
	private ArrayList<EntityAlbergo> listaAlberghi;
	
	/**
	 * Costruttore di default
	 * */
	public EntityCatenaAlberghiera() {
		super();
		
		this.listaAlberghi = new ArrayList<EntityAlbergo>();
	}
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param codice Codice della catena alberghiera
	 * */
	public EntityCatenaAlberghiera(int codice) {
		super();
		
		this.codice = codice;
		
		CatenaAlberghieraDAO catena = new CatenaAlberghieraDAO(codice);

		this.nome = catena.getNome();
		this.emailDirettore = catena.getEmailDirettore();
		
		caricaListaAlberghi(catena);
	}
	
	/**
	 * Costruttore: inizializzazione tramite l'e-mail del direttore
	 * 
	 * @apiNote L'e-mail del direttore nel database è unique
	 * 
	 * @param emailDirettore L'e-mail del direttore
	 * */
	public EntityCatenaAlberghiera(String emailDirettore) throws NoDirectorFoundException {
		super();
		
		CatenaAlberghieraDAO catena = new CatenaAlberghieraDAO();
		
		if(!catena.hasDirector(emailDirettore)) {
			throw new NoDirectorFoundException();
		}
		
		this.emailDirettore = emailDirettore;
		
		caricaCatenaByEmail();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param catena Catena alberghiera già costruita
	 * */
	public EntityCatenaAlberghiera(CatenaAlberghieraDAO catena) {
		this.codice = catena.getCodice();
		this.nome = catena.getNome();
		this.emailDirettore = catena.getEmailDirettore();
		
		caricaListaAlberghi(catena);
	}
	
	/**
	 * Carica da database la catena alberghiera da database tramite l'e-mail
	 * */
	public void caricaCatenaByEmail() {
		try {
			CatenaAlberghieraDAO catena = new CatenaAlberghieraDAO(this.emailDirettore);
			
			this.codice = catena.getCodice();
			this.nome = catena.getNome();
		} catch (NoDirectorFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Carica da database la lista di alberghi di una catena alberghiera
	 * 
	 * @param catena Oggetto DAO già costruito
	 * */
	public void caricaListaAlberghi(CatenaAlberghieraDAO catenaAlberghiera) {
		this.listaAlberghi = new ArrayList<EntityAlbergo>();
		
	    for(AlbergoDAO albergoDAO : catenaAlberghiera.getListaAlberghi()) {
			EntityAlbergo albergo = new EntityAlbergo(albergoDAO);
			
			this.listaAlberghi.add(albergo);
	    }
	}
	
	/**
	 * READ: Lettura da database della lista di alberghi di una singola catena alberghiera, con camere disponibili
	 * (della tipologia indicata) e nella città indicata
	 * 
	 * @throws NoHotelForCityException Eccezione scatenata nel momento in cui non esiste un albergo
	 * nella città selezionata dall'utente
	 * @throws NoRoomException Eccezione scatenata nel momento in cui non esistono camere singole/doppie/triple
	 * negli alberghi della città selezionata dall'utente
	 * 
	 * @param citta La città scelta dall'utente
	 * @param tipologiaCamera La tipologia di camera scelta dall'utente
	 * 
	 * @return Lista di alberghi disponibili
	 * */
	public ArrayList<EntityAlbergo> visualizzaListaAlberghiDisponibili(String citta, String tipologiaCamera) {
		
		ArrayList<EntityAlbergo> alberghiDisponibili = new ArrayList<EntityAlbergo>();
			
		for(EntityAlbergo albergo : this.getListaAlberghi()) {
			
			if(albergo.getCitta().equals(citta)) {
				
				// Per leggere le singole/doppie/triple di un albergo nella città scelta,
				// chiamo "albergo" che avrà la città interessata (in quanto è catturata dall'if)
				
				if(tipologiaCamera.equalsIgnoreCase("Singola")) {
					
					// Se l'albergo ha almeno una singola disponibile,
					// lo aggiungo alla lista degli alberghi disponibili
					if( !albergo.verificaDisponibilitaSingole().isEmpty() ) {
						alberghiDisponibili.add(albergo);
					}
					
				}
				
				if(tipologiaCamera.equalsIgnoreCase("Doppia")) {
					
					// Se l'albergo ha almeno una doppia disponibile,
					// lo aggiungo alla lista degli alberghi disponibili
					if( !albergo.verificaDisponibilitaDoppie().isEmpty() ) {
						alberghiDisponibili.add(albergo);
					}
					
				}
				
				if(tipologiaCamera.equalsIgnoreCase("Tripla")) {
					
					// Se l'albergo ha almeno una tripla disponibile,
					// lo aggiungo alla lista degli alberghi disponibili
					if( !albergo.verificaDisponibilitaTriple().isEmpty() ) {
						alberghiDisponibili.add(albergo);
					}
				}
			}
		}
		
		return alberghiDisponibili;
	}
	
	/**
	 * Genera il report con l'elenco delle notti
	 * 
	 * @return Stringa contenente il report
	 * */
	public String generaReportElencoNotti() {
		CatenaAlberghieraDAO catena = new CatenaAlberghieraDAO(this.codice);
		
		return catena.reportToString();
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

	public ArrayList<EntityAlbergo> getListaAlberghi() {
		return listaAlberghi;
	}

	public void setListaAlberghi(ArrayList<EntityAlbergo> listaAlberghi) {
		this.listaAlberghi = listaAlberghi;
	}

	@Override
	public String toString() {
	   
	    String result = "\nCodice: " + codice +
	    				"\nNome: " + nome +
	    				"\nEmail Direttore: " + emailDirettore;
	    				
	    if(!listaAlberghi.isEmpty()) {
	    	result += "\n\nLista di alberghi:\n\n";
	    	
	    	for (EntityAlbergo albergo : listaAlberghi) {
		        result += "Nome: " + albergo.getNome() +
		        		  "\nCitta: " + albergo.getCitta() +
		        		  "\nIndirizzo: " + albergo.getIndirizzo() +
		        		  "\n\n";
		    }
	    }

	  return result; 
	}

}
