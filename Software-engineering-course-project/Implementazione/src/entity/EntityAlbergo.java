package entity;

import java.util.ArrayList;

import database.AlbergoDAO;
import database.CameraDAO;
import database.SingolaDAO;
import database.DoppiaDAO;
import database.TriplaDAO;

import exception.InvalidCameraTypeException;

public class EntityAlbergo {

	private int identificativo;
	private String nome;
	private String citta;
	private String indirizzo;
	private String CAP;
	private String numeroDiTelefono;
	private EntityCatenaAlberghiera catenaAlberghiera;
	private ArrayList<EntityCamera> listaCamere;
	
	/**
	 * Costruttore di default
	 * */
	public EntityAlbergo() {
		super();
		
		this.listaCamere = new ArrayList<EntityCamera>();
	}

	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param identificativo della classe albergo
	 * */
	public EntityAlbergo(int identificativo) {
		super();
		
		this.identificativo = identificativo;
		
		AlbergoDAO albergo = new AlbergoDAO(identificativo);
		
		this.nome = albergo.getNome();
		this.citta = albergo.getCitta();
		this.indirizzo = albergo.getIndirizzo();
		this.CAP = albergo.getCAP();
		this.numeroDiTelefono = albergo.getNumeroDiTelefono();
		
		caricaCatenaAlberghiera(albergo);
		caricaListaCamere(albergo);
	}
	
	/**
	 * Costruttore: inizializzazione tramite il nome
	 * 
	 * @apiNote Il nome nel database è unique
	 * 
	 * @param nome Nome dell'albergo
	 * */
	public EntityAlbergo(String nome) {
		super();
		
		this.nome = nome;
		
		AlbergoDAO albergo = new AlbergoDAO(nome);
		
		this.identificativo = albergo.getIdentificativo();
		this.citta = albergo.getCitta();
		this.indirizzo = albergo.getIndirizzo();
		this.CAP = albergo.getCAP();
		this.numeroDiTelefono = albergo.getNumeroDiTelefono();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param albergo Albergo già costruito
	 * */
	public EntityAlbergo(AlbergoDAO albergo) {
		this.identificativo = albergo.getIdentificativo();
		this.nome = albergo.getNome();
		this.citta = albergo.getCitta();
		this.indirizzo = albergo.getIndirizzo();
		this.CAP = albergo.getCAP();
		this.numeroDiTelefono = albergo.getNumeroDiTelefono();

		caricaCatenaAlberghiera(albergo);
		caricaListaCamere(albergo);
	}
	
	/** 
	 * Carica da database la catena alberghiera dell'albergo
	 * 
	 * @param albergo Oggetto DAO già costruito
	 * */
	public void caricaCatenaAlberghiera(AlbergoDAO albergo) {
		EntityCatenaAlberghiera catena = new EntityCatenaAlberghiera();
		
		catena.setCodice(albergo.getCatenaAlberghiera().getCodice());
		catena.setNome(albergo.getCatenaAlberghiera().getNome());
		catena.setEmailDirettore(albergo.getCatenaAlberghiera().getEmailDirettore());
		
		this.catenaAlberghiera = catena;
	}
	
	/** 
	 * Carica da database la lista delle camere dell'albergo
	 * 
	 * @param albergo Oggetto DAO già costruito
	 * */
	public void caricaListaCamere(AlbergoDAO albergo) {
		this.listaCamere = new ArrayList<EntityCamera>();
		
		for(CameraDAO cameraDAO : albergo.getListaCamere()) {
			EntityCamera camera = new EntityCamera(cameraDAO);
			
			this.listaCamere.add(camera);
		}
	}
	
	/** 
	 * Cerca la lista delle camere singole dell'albergo disponibili
	 * 
	 * @return Lista di camere singole disponibili nell'albergo
	 * */
	public ArrayList<SingolaDAO> verificaDisponibilitaSingole() {
		AlbergoDAO albergo = new AlbergoDAO(this.identificativo);
		
		return albergo.readListaSingoleDisponibili();
	}
	
	/** 
	 * Cerca la lista delle camere doppie dell'albergo disponibili
	 * 
	 * @return Lista di camere doppie disponibili nell'albergo
	 * */
	public ArrayList<DoppiaDAO> verificaDisponibilitaDoppie() {
		AlbergoDAO albergo = new AlbergoDAO(this.identificativo);
		
		return albergo.readListaDoppieDisponibili();
	}
	
	/** 
	 * Cerca la lista delle camere triple dell'albergo disponibili
	 * 
	 * @return Lista di camere triple disponibili nell'albergo
	 * */
	public ArrayList<TriplaDAO> verificaDisponibilitaTriple() {
		AlbergoDAO albergo = new AlbergoDAO(this.identificativo);
		
		return albergo.readListaTripleDisponibili();
	}
	
	/** 
	 * Carica da database la lista delle camere singole dell'albergo disponibili
	 * 
	 * @return Lista di camere singole disponibili nell'albergo
	 * */
	public ArrayList<EntitySingola> caricaSingoleDisponibili() {
		ArrayList<EntitySingola> singole = new ArrayList<EntitySingola>();
		
		AlbergoDAO albergo = new AlbergoDAO(this.identificativo);
		
		ArrayList<SingolaDAO> singoleDAO = albergo.readListaSingoleDisponibili();
		
		for(SingolaDAO singolaDAO : singoleDAO) {
			try {
				EntitySingola singola = new EntitySingola(singolaDAO.getIdentificativo());
				
				singole.add(singola);
			} catch (InvalidCameraTypeException e) {
				e.printStackTrace();
			}
		}
		
		return singole;
	}
	
	/** 
	 * Carica da database la lista delle camere doppie dell'albergo disponibili
	 * 
	 * @return Lista di camere doppie disponibili nell'albergo
	 * */
	public ArrayList<EntityDoppia> caricaDoppieDisponibili() {
		ArrayList<EntityDoppia> doppie = new ArrayList<EntityDoppia>();
		
		AlbergoDAO albergo = new AlbergoDAO(this.identificativo);
		
		ArrayList<DoppiaDAO> doppieDAO = albergo.readListaDoppieDisponibili();
		
		for(DoppiaDAO doppiaDAO : doppieDAO) {
			try {
				EntityDoppia doppia = new EntityDoppia(doppiaDAO.getIdentificativo());
				
				doppie.add(doppia);
			} catch (InvalidCameraTypeException e) {
				e.printStackTrace();
			}
		}
		
		return doppie;
	}
	
	/** 
	 * Carica da database la lista delle camere triple dell'albergo disponibili
	 * 
	 * @return Lista di camere triple disponibili nell'albergo
	 * */
	public ArrayList<EntityTripla> caricaTripleDisponibili() {
		ArrayList<EntityTripla> triple = new ArrayList<EntityTripla>();
		
		AlbergoDAO albergo = new AlbergoDAO(this.identificativo);
		
		ArrayList<DoppiaDAO> tripleDAO = albergo.readListaDoppieDisponibili();
		
		for(DoppiaDAO triplaDAO : tripleDAO) {
			try {
				EntityTripla tripla = new EntityTripla(triplaDAO.getIdentificativo());
				
				triple.add(tripla);
			} catch (InvalidCameraTypeException e) {
				e.printStackTrace();
			}
		}
		
		return triple;
	}

	public int getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(int identificativo) {
		this.identificativo = identificativo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCAP() {
		return CAP;
	}

	public void setCAP(String cAP) {
		CAP = cAP;
	}

	public String getNumeroDiTelefono() {
		return numeroDiTelefono;
	}

	public void setNumeroDiTelefono(String numeroDiTelefono) {
		this.numeroDiTelefono = numeroDiTelefono;
	}

	public EntityCatenaAlberghiera getCatenaAlberghiera() {
		return catenaAlberghiera;
	}

	public void setCatenaAlberghiera(EntityCatenaAlberghiera catenaAlberghiera) {
		this.catenaAlberghiera = catenaAlberghiera;
	}

	public ArrayList<EntityCamera> getListaCamere() {
		return listaCamere;
	}

	public void setListaCamere(ArrayList<EntityCamera> listaCamere) {
		this.listaCamere = listaCamere;
	}
	@Override
	public String toString() {
		return "\nNome: " + nome +
		       "\nCittà: " + citta +
		       "\nIndirizzo: " + indirizzo +
		       "\nCAP: " + CAP +
		       "\nNumero di telefono: " + numeroDiTelefono +
		       "\nCatena Alberghiera: " + catenaAlberghiera.getNome();
	}
	
}