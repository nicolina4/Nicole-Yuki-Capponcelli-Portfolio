package entity;

import java.text.DecimalFormat;
import java.time.LocalDate;

import database.PrenotazioneDAO;

public class EntityPrenotazione {

	private int codice;
	private LocalDate dataArrivo;
	private LocalDate dataPartenza;
	private int numeroStanza;
	private EntityAlbergo albergo;
	private EntityCamera camera;
	private EntityCliente cliente;
	
	/**
	 * Costruttore di default
	 * */
	public EntityPrenotazione() {
		super();
		
		this.albergo = new EntityAlbergo();
		this.camera = new EntityCamera();
		this.cliente = new EntityCliente();
	}
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param codice Codice della prenotazione
	 * */
	public EntityPrenotazione(int codice) {
		super();
		
		this.codice = codice;
		
		PrenotazioneDAO prenotazione = new PrenotazioneDAO(codice);
		
		this.dataArrivo = prenotazione.getDataArrivo();
		this.dataPartenza = prenotazione.getDataPartenza();
		this.numeroStanza = prenotazione.getNumeroStanza();

		caricaAlbergo(prenotazione);
		caricaCamera(prenotazione);
		caricaCliente(prenotazione);
		
		this.numeroStanza = this.getCamera().getNumeroCamera();
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param prenotazione Prenotazione già costruita
	 * */
	public EntityPrenotazione(PrenotazioneDAO prenotazione) {
		this.codice = prenotazione.getCodice();
		this.dataArrivo = prenotazione.getDataArrivo();
		this.dataPartenza = prenotazione.getDataPartenza();
		this.numeroStanza = prenotazione.getNumeroStanza();

		caricaAlbergo(prenotazione);
		caricaCamera(prenotazione);
		caricaCliente(prenotazione);
	}
	
	/**
	 * Carica da database l'albergo relativo alla prenotazione
	 * 
	 * @param prenotazione Oggetto DAO già costruito
	 * */
	public void caricaAlbergo(PrenotazioneDAO prenotazione) {
		EntityAlbergo albergo = new EntityAlbergo();
		
		albergo.setIdentificativo(prenotazione.getAlbergo().getIdentificativo());
		albergo.setNome(prenotazione.getAlbergo().getNome());
		albergo.setCitta(prenotazione.getAlbergo().getCitta());
		albergo.setIndirizzo(prenotazione.getAlbergo().getIndirizzo());
		albergo.setCAP(prenotazione.getAlbergo().getCAP());
		albergo.setNumeroDiTelefono(prenotazione.getAlbergo().getNumeroDiTelefono());
		
		this.albergo = albergo;
	}
	
	/**
	 * Carica da database la camera relativa alla prenotazione
	 * 
	 * @param prenotazione Oggetto DAO già costruito
	 * */
	public void caricaCamera(PrenotazioneDAO prenotazione) {
		EntityCamera camera = new EntityCamera(prenotazione.getCamera());
		
		this.camera = camera;
	}
	
	/**
	 * Carica da database il cliente relativo alla prenotazione
	 * 
	 * @param prenotazione Oggetto DAO già costruito
	 * */
	public void caricaCliente(PrenotazioneDAO prenotazione) {
		EntityCliente cliente = new EntityCliente(prenotazione.getCliente());
		
		this.cliente = cliente;
	}

	/**
	 * Genera il riepilogo di una prenotazione.
	 * 
	 * @param albergo Albergo relativo alla prenotazione
	 * @param camera Camera relativa alla prenotazione
	 * @param tipologiaCamera Tipologia della camera relativa alla prenotazione
	 * @param prezzoPerNotte Il prezzo per notte relativo alla camera prenotata, formattato come stringa
	 * 
	 * @return Stringa contenente il riepilogo
	 * */
	public String creaRiepilogoPrenotazione(EntityAlbergo albergo, EntityCamera camera, String tipologiaCamera,
						String prezzoPerNotte) {
		
		String riepilogo = "Grazie per aver prenotato con noi!\n\n"
				+ "Dettagli prenotazione:\n\nCitta: " + albergo.getCitta() + "\nData Arrivo: " + this.dataArrivo
				+ "\nData Partenza: " + this.dataPartenza + "\nNumero stanza: " + this.numeroStanza
				+ "\nTipologia Camera: "; 
		
		if(tipologiaCamera.equalsIgnoreCase("Singola")) {
			riepilogo += "Singola";
		}
		
		if(tipologiaCamera.equalsIgnoreCase("Doppia")) {
			riepilogo += "Doppia";
		}

		if(tipologiaCamera.equalsIgnoreCase("Tripla")) {
			riepilogo += "Tripla";
		}
		
		riepilogo += "\nPrezzo per notte: € " + prezzoPerNotte + "\n\nAlbergo: " + albergo.getNome() + "\nIndirizzo: " + albergo.getIndirizzo() + "\nCAP: " 
					+ albergo.getCAP() + "\nNumero di Telefono: " + albergo.getNumeroDiTelefono();		
				
		return riepilogo;
	}
	
	/**
	 * Genera il prezzo complessivo
	 * 
	 * @return Prezzo complessivo relativo a una prenotazione, formattato come stringa
	 * */
	public String generaPrezzoComplessivo() {
		PrenotazioneDAO prenotazione = new PrenotazioneDAO(this.codice);
		
		DecimalFormat decimalFormat = new DecimalFormat("#.##"); 
		String prezzoFormattato = decimalFormat.format(prenotazione.createPrezzoComplessivo());
		
		return prezzoFormattato;
	}
	
	/**
	 * Genera la fattura relativa a una prenotazione (al momento del check-out).
	 * 
	 * @return Stringa contenente la fattura
	 * */
	public String generaFattura() {
		PrenotazioneDAO prenotazione = new PrenotazioneDAO(this.codice);
		
		String fattura = prenotazione.createFattura();
		
		return fattura;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public LocalDate getDataArrivo() {
		return dataArrivo;
	}

	public void setDataArrivo(LocalDate dataArrivo) {
		this.dataArrivo = dataArrivo;
	}

	public LocalDate getDataPartenza() {
		return dataPartenza;
	}

	public void setDataPartenza(LocalDate dataPartenza) {
		this.dataPartenza = dataPartenza;
	}

	public int getNumeroStanza() {
		return numeroStanza;
	}

	public void setNumeroStanza(int numeroStanza) {
		this.numeroStanza = numeroStanza;
	}
	
	public EntityAlbergo getAlbergo() {
		return albergo;
	}

	public void setAlbergo(EntityAlbergo albergo) {
		this.albergo = albergo;
	}

	public EntityCamera getCamera() {
	    return camera;
	}

	public void setCamera(EntityCamera camera) {
        this.camera = camera;
    }

	public EntityCliente getCliente() {
		return cliente;
	}

	public void setCliente(EntityCliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
	    return "\nCliente: " + cliente.getNome() + " " + cliente.getCognome() +
	           "\nData Arrivo: " + dataArrivo +
	           "\nData Partenza: " + dataPartenza +
	           "\nAlbergo: " + albergo.getNome() + 
	           "\nNumero stanza: " + numeroStanza;
	}
	
	/** 
	 * Override del metodo equals. Due prenotazioni sono uguali se coincidono i codici delle prenotazioni
	 * */
	@Override
	public boolean equals(Object obj) {
		
	    // Verifica se l'oggetto è identico a se stesso
		// (cioè se tutti i campi di un cliente coincidono con l'altro cliente)
	    if (this == obj) {
	        return true;
	    }

	    // Verifica se l'oggetto è null o di un tipo diverso
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }

	    //Per tutti gli altri casi, si effettua il cast dell'oggetto
	    EntityPrenotazione prenotazione = (EntityPrenotazione) obj;

	    return this.codice == prenotazione.getCodice();
	}
}