package entity;

import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import database.CameraDAO;
import database.ClienteDAO;
import database.PrenotazioneDAO;

import exception.NotUniqueEmailException;

public class EntityCliente {
		
	private int identificativo;
	private String nome;
	private String cognome;
	private String email;
	private String telefono;
	private String indirizzo;
	private String numeroCartaCredito;
	private ArrayList<EntityPrenotazione> listaPrenotazioni;
	
	/**
	 * Costruttore di default
	 * */
	public EntityCliente() {
		super();
	
		this.listaPrenotazioni = new ArrayList<EntityPrenotazione>();
	}
	
	/**
	 * Costruttore: inizializzazione tramite chiave primaria
	 * 
	 * @param 
	 * */
	public EntityCliente(int identificativo) {
		super();
		
		this.identificativo = identificativo;
		
		ClienteDAO cliente = new ClienteDAO(identificativo);
		
		this.nome = cliente.getNome();
		this.cognome = cliente.getCognome();
		this.email = cliente.getEmail();
		this.telefono = cliente.getTelefono();
		this.indirizzo = cliente.getIndirizzo();
		this.numeroCartaCredito = cliente.getNumeroCartaCredito();
		
		caricaListaPrenotazioni(cliente);
	}
	
	/**
	 * Costruttore: inizializzazione tramite l'e-mail
	 * 
	 * @apiNote L'e-mail nel database è unique
	 * 
	 * @param email L'e-mail del cliente
	 * */
	public EntityCliente(String email) {
		super();
		
		this.email = email;
		
		ClienteDAO cliente = new ClienteDAO(email);
		
		this.identificativo = cliente.getIdentificativo();
		this.nome = cliente.getNome();
		this.cognome = cliente.getCognome();
		this.telefono = cliente.getTelefono();
		this.indirizzo = cliente.getIndirizzo();
		this.numeroCartaCredito = cliente.getNumeroCartaCredito();
		
		caricaListaPrenotazioni(cliente);	
	}
	
	/**
	 * Costruttore: inizializzazione tramite oggetto DAO già definito
	 * 
	 * @param cliente Cliente già costruito
	 * */
	public EntityCliente(ClienteDAO cliente) {
		this.identificativo=cliente.getIdentificativo();
		this.nome = cliente.getNome();
		this.cognome = cliente.getCognome();
		this.email = cliente.getEmail();
		this.telefono = cliente.getTelefono();
		this.indirizzo = cliente.getIndirizzo();
		this.numeroCartaCredito = cliente.getNumeroCartaCredito();

		caricaListaPrenotazioni(cliente);
	}
	
	/**
	 * Costruttore: inizializzazione tramite clienteDAO 
	 * per caricare la lista di prenotazioni effettuata da un cliente
	 * 
	 * @param 
	 * */
	public void caricaListaPrenotazioni(ClienteDAO cliente) {
		this.listaPrenotazioni = new ArrayList<EntityPrenotazione>();
		
		for(PrenotazioneDAO prenotazioneDAO : cliente.getListaPrenotazioni()) {
			EntityPrenotazione prenotazione = new EntityPrenotazione();
			
			prenotazione.setCodice(prenotazioneDAO.getCodice());
			prenotazione.setDataArrivo(prenotazioneDAO.getDataArrivo());
			prenotazione.setDataPartenza(prenotazioneDAO.getDataPartenza());
			prenotazione.setNumeroStanza(prenotazioneDAO.getNumeroStanza());
			
			prenotazione.caricaAlbergo(prenotazioneDAO);
			prenotazione.caricaCamera(prenotazioneDAO);
			
			prenotazione.setCliente(this);
			
			this.listaPrenotazioni.add(prenotazione);
		}
	}
		
	/**
	 * Permette al cliente di inserire nome, cognome, e-mail, numero di telefono, indirizzo e numero carta
	 * di credito.
	 * 
	 * @return Cliente valido
	*/
	public void inserimentoDatiCliente(String nome, String cognome, String email, String telefono,
								String indirizzo, String numeroCartaCredito) {
		this.setNome(nome);
		this.setCognome(cognome);
		this.setEmail(email);
		this.setTelefono(telefono);
		this.setIndirizzo(indirizzo);
		this.setNumeroCartaCredito(numeroCartaCredito);
	}

	/**
	 * Scenario 2
	 * 
	 * @throws NotUniqueEmailException Eccezione scatenata nel caso in cui si rileva che un cliente ha provato a 
	 * inserire un'e-mail corrispondente già a un altro cliente nel database.
	 * 
	 * @param nome Nome inserito dal cliente
	 * @param cognome Cognome inserito dal cliente
	 * @param email E-mail inserita dal cliente
	 * @param telefono Teefono inserito dal cliente
	 * @param indirizzo Indirizzo inserito dal cliente
	 * @param numeroCartaCredito Numero carta di credito inserita dal cliente
	 * @param dataArrivoStringa Data di arrivo inserita dal cliente come stringa
	 * @param dataPartenzaStringa Data di partenza inserita dal cliente come stringa
	 * @param tipologiaCamera Tipologia camera inserita dal cliente
	 * @param nomeAlbergo Nome dell'albergo scelto dal cliente
	 * 
	 * @return Codice della prenotazione effettuata (-1 se la prenotazione non è andata a buon fine)
	*/
	public int effettuaPrenotazione(String nome, String cognome, String email, String telefono,
			String indirizzo, String numeroCartaCredito, String dataArrivoStringa, String dataPartenzaStringa,
			String tipologiaCamera, String nomeAlbergo) throws NotUniqueEmailException {
		int codice = -1;
				
		ClienteDAO clienteDAO = new ClienteDAO(email);
		
		// Se la mail inserita non esiste nel sistema e anche i dati del cliente non risultano, creo un nuovo cliente
		if(!clienteDAO.checkUniqueEmail(email) && !clienteDAO.readClienteByAllData(nome,cognome,email,telefono,indirizzo,
				numeroCartaCredito)) {
			
			// Assegno un nuovo identificativo al cliente
			this.identificativo = clienteDAO.getIdentificativoNuovoCliente();
			
			// Inserimento dati del cliente validati
			this.inserimentoDatiCliente(nome,cognome,email,telefono,indirizzo,numeroCartaCredito);
		
			// Se i dati inseriti non sono presenti nel database, vanno salvati: creo un nuovo cliente
			if(clienteDAO.createCliente(nome,cognome,email,telefono,indirizzo,numeroCartaCredito)) {
				JOptionPane.showMessageDialog(null, "\nNuovo cliente registrato!", "REGISTRAZIONE EFFETTUATA", JOptionPane.INFORMATION_MESSAGE);
				
				JOptionPane.showMessageDialog(null, "\nDati validi, prenotazione in corso", "PRENOTAZIONE", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Cliente non registrato correttamente", "REGISTRAZIONE NON EFFETTUATA", JOptionPane.ERROR_MESSAGE);
			}
			
		// Se la mail esiste già e i dati del cliente coincidono, si identifica il cliente registrato
		}else if(clienteDAO.checkUniqueEmail(email) && clienteDAO.readClienteByAllData(nome,cognome,email,telefono,
				indirizzo,numeroCartaCredito)) {
			this.identificativo = clienteDAO.getIdentificativo();
			
			JOptionPane.showMessageDialog(null, "\nBentornato! Siamo lieti che tu sia tornato a prenotare con Italian Travel!", "CLIENTE GIÀ REGISTRATO", JOptionPane.INFORMATION_MESSAGE);

			JOptionPane.showMessageDialog(null, "\nDati validi, prenotazione in corso", "PRENOTAZIONE", JOptionPane.INFORMATION_MESSAGE);
		
		// Se la mail esiste già e i dati del cliente NON coincidono, non si può creare il cliente
		}else if(clienteDAO.checkUniqueEmail(email) && !clienteDAO.readClienteByAllData(nome,cognome,email,telefono,
				indirizzo,numeroCartaCredito)) {
			throw new NotUniqueEmailException("L'e-mail inserita appartiene già ad un altro cliente!");
		}
			
		// Creo la nuova prenotazione
		EntityPrenotazione prenotazione = new EntityPrenotazione();
		
		// Setto l'albergo in base al nome
		EntityAlbergo albergo = new EntityAlbergo(nomeAlbergo);
		
		// Conversione date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate dataArrivo = LocalDate.parse(dataArrivoStringa, formatter);
      	LocalDate dataPartenza = LocalDate.parse(dataPartenzaStringa, formatter);
		
		// Per la creazione della nuova prenotazione nel database
		PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();

		prenotazione.setCodice(prenotazioneDAO.getCodiceNuovaPrenotazione());
		prenotazione.setDataArrivo(dataArrivo);
		prenotazione.setDataPartenza(dataPartenza);
		prenotazione.setAlbergo(albergo);
		prenotazione.setCliente(this);
		
		// Logica di assegnazione delle camere: la prima della lista
		if(tipologiaCamera.equalsIgnoreCase("Singola")) {
			prenotazione.setCamera(albergo.caricaSingoleDisponibili().get(0));
		}else if(tipologiaCamera.equalsIgnoreCase("Doppia")) {
			prenotazione.setCamera(albergo.caricaDoppieDisponibili().get(0));
		}else if(tipologiaCamera.equalsIgnoreCase("Tripla")) {
			prenotazione.setCamera(albergo.caricaTripleDisponibili().get(0));
		}else {
			JOptionPane.showMessageDialog(null, "Camera " + tipologiaCamera + " inesistente nell'albergo " 
					+ prenotazione.getAlbergo().getNome() + "!", "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		prenotazione.setNumeroStanza(prenotazione.getCamera().getNumeroCamera());
		
		// Imposto la camera appena prenotata come "PRENOTATA"
		prenotazione.getCamera().setStato("PRENOTATA");
		
		// Aggiornamento della camera
		CameraDAO camera = new CameraDAO(prenotazione.getCamera().getIdentificativo());
		
		// Se l'aggiornamento della camera funziona correttamente, procedo all'inserimento della prenotazione
		if(camera.updateCamera(prenotazione.getCamera().getStato())){
			
			// Creo la prenotazione con tutti i dati necessari nel  database
			if(prenotazioneDAO.createPrenotazione(dataArrivo,dataPartenza,prenotazione.getNumeroStanza(),
					prenotazione.getAlbergo().getIdentificativo(),prenotazione.getCamera().getIdentificativo(),
					this.identificativo)) {
				
				JOptionPane.showMessageDialog(null, prenotazione.getCamera(), "INFO CAMERA", JOptionPane.INFORMATION_MESSAGE);
				
				// Aggiungo la prenotazione alla lista delle prenotazioni
				this.getListaPrenotazioni().add(prenotazione);
				
				JOptionPane.showMessageDialog(null, "\nPrenotazione effettuata correttamente", "PRENOTAZIONE EFFETTUATA", JOptionPane.INFORMATION_MESSAGE);
				
				codice = prenotazione.getCodice();
					
			}else {
				JOptionPane.showMessageDialog(null, "Prenotazione non effettuata correttamente. "
						+ "È necessario ripetere la procedura", "ERRORE", JOptionPane.ERROR_MESSAGE);
			}
			
		}else {
			// Questo è un caso raro di errore interno, non dovrebbe interessare al Cliente
			JOptionPane.showMessageDialog(null, "Prenotazione non effettuata correttamente. "
					+ "È necessario ripetere la procedura", "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return codice;
	}

	/**
	 * Scenario 3
	 * 
	 * @return True se il check-in è andato a buon fine, false altrimenti
	*/
	public boolean effettuaCheckIn() {
		boolean check = false;
		
		ClienteDAO clienteDAO = new ClienteDAO(this.email);
		
		PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
		
		if(clienteDAO.readClienteByEmail(this.email)) {
			EntityPrenotazione prenotazione = new EntityPrenotazione();
			
			prenotazione.setCodice(prenotazioneDAO.readCodicePrenotazioneCheckIn(this.email));
			
			if(this.getListaPrenotazioni().contains(prenotazione)) {
				JOptionPane.showMessageDialog(null, this.nome + " " + this.cognome + 
						" è presente nel sistema e ha effettuato una prenotazione", "DATI VERIFICATI", JOptionPane.INFORMATION_MESSAGE);
			
				int index = this.getListaPrenotazioni().indexOf(prenotazione);
				
				LocalDate today = LocalDate.now();
				
				if(this.getListaPrenotazioni().get(index).getDataArrivo().isEqual(today)) {
					// Set dei dati della prenotazione
					prenotazione.setDataArrivo(this.getListaPrenotazioni().get(index).getDataArrivo());
					prenotazione.setDataPartenza(this.getListaPrenotazioni().get(index).getDataPartenza());
					prenotazione.setNumeroStanza(this.getListaPrenotazioni().get(index).getCamera().getNumeroCamera());
					prenotazione.setAlbergo(this.getListaPrenotazioni().get(index).getAlbergo());
					prenotazione.setCamera(this.getListaPrenotazioni().get(index).getCamera());
					prenotazione.setCliente(this);
					
					JOptionPane.showMessageDialog(null, prenotazione, "RIEPILOGO DATI PRENOTAZIONE", JOptionPane.INFORMATION_MESSAGE);
					
					CameraDAO cameraDAO = new CameraDAO(prenotazione.getCamera().getIdentificativo());
					
					// Si aggiorna lo stato della camera ad occupata
					prenotazione.getCamera().setStato("OCCUPATA");
					
					// Aggiorno la camera se questa esiste
					if(cameraDAO.updateCamera(prenotazione.getCamera().getStato())) {
						JOptionPane.showMessageDialog(null, "Check-in effettuato con successo", "OCCUPAZIONE CAMERA", JOptionPane.INFORMATION_MESSAGE);
					
						check = true;
					} else {
						JOptionPane.showMessageDialog(null, "Check-in non effettuato", "ERRORE", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "La data di arrivo non corrisponde alla data odierna", "ERRORE", JOptionPane.ERROR_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(null, "\nIl cliente non può effettuare alcun check-in!", "ERRORE", JOptionPane.ERROR_MESSAGE);
			}
			
		} else {
			JOptionPane.showMessageDialog(null, "\nIl cliente non esiste!", "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return check;
	}

	/**
	 * Scenario 4
	 * 
	 * @return True se il check-out è andato a buon fine, false altrimenti
	*/
	public int effettuaCheckOut() {
		int codice = -1;
		
		ClienteDAO clienteDAO = new ClienteDAO(this.email);
		
		PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
		
		if(clienteDAO.readClienteByEmail(this.email)) {
			EntityPrenotazione prenotazione = new EntityPrenotazione();
			
			prenotazione.setCodice(prenotazioneDAO.readCodicePrenotazioneCheckOut(this.email));
			
			if(this.getListaPrenotazioni().contains(prenotazione)) {
				JOptionPane.showMessageDialog(null, this.nome + " " + this.cognome + 
						" è presente nel sistema e ha effettuato una prenotazione", "DATI VERIFICATI", JOptionPane.INFORMATION_MESSAGE);
			
				int index = this.getListaPrenotazioni().indexOf(prenotazione);
				
				LocalDate today = LocalDate.now();
				
				if(this.getListaPrenotazioni().get(index).getDataPartenza().isEqual(today)) {
					// Set dei dati della prenotazione
					prenotazione.setDataArrivo(this.getListaPrenotazioni().get(index).getDataArrivo());
					prenotazione.setDataPartenza(this.getListaPrenotazioni().get(index).getDataPartenza());
					prenotazione.setNumeroStanza(this.getListaPrenotazioni().get(index).getCamera().getNumeroCamera());
					prenotazione.setAlbergo(this.getListaPrenotazioni().get(index).getAlbergo());
					prenotazione.setCamera(this.getListaPrenotazioni().get(index).getCamera());
					prenotazione.setCliente(this);
					
					JOptionPane.showMessageDialog(null, prenotazione, "RIEPILOGO DATI PRENOTAZIONE", JOptionPane.INFORMATION_MESSAGE);
					
					CameraDAO cameraDAO = new CameraDAO(prenotazione.getCamera().getIdentificativo());
					
					// Si aggiorna lo stato della camera ad occupata
					prenotazione.getCamera().setStato("DISPONIBILE");
					
					// Aggiorno la camera se questa esiste
					if(cameraDAO.updateCamera(prenotazione.getCamera().getStato())) {
						JOptionPane.showMessageDialog(null, "Check-out effettuato con successo", "CAMERA LIBERA", JOptionPane.INFORMATION_MESSAGE);
						
						codice = this.getListaPrenotazioni().get(index).getCodice();
					} else {
						JOptionPane.showMessageDialog(null, "Check-out non effettuato", "ERRORE", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "La data di partenza non corrisponde alla data odierna", "ERRORE", JOptionPane.ERROR_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(null, "\nIl cliente non può effettuare alcun check-out!", "ERRORE", JOptionPane.ERROR_MESSAGE);
			}
			
		} else {
			JOptionPane.showMessageDialog(null, "\nIl cliente non esiste!", "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return codice;
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumeroCartaCredito() {
		return numeroCartaCredito;
	}

	public void setNumeroCartaCredito(String numeroCartaCredito) {
		this.numeroCartaCredito = numeroCartaCredito;
	}

	public ArrayList<EntityPrenotazione> getListaPrenotazioni() {
		return listaPrenotazioni;
	}

	public void setListaPrenotazioni(ArrayList<EntityPrenotazione> listaPrenotazioni) {
		this.listaPrenotazioni = listaPrenotazioni;
	}

	@Override
	public String toString() {
	    String result = "\nNome: " + nome +
	                    "\nCognome: " + cognome +
	                    "\nEmail: " + email +
	                    "\nTelefono: " + telefono +
	                    "\nIndirizzo: " + indirizzo +
	                    "\nNumero Carta Credito: " + numeroCartaCredito;
	    
	    if(!listaPrenotazioni.isEmpty()) {
	    	result += "\n\nLista di Prenotazioni:\n\n";

		    for (EntityPrenotazione prenotazione : listaPrenotazioni) {
		        result += "Data Arrivo: " + prenotazione.getDataArrivo() + "\n" +
		                  "Data Partenza: " + prenotazione.getDataPartenza() + "\n" +
		                  "Albergo: " + prenotazione.getAlbergo().getNome() + "\n" +
		                  "Numero stanza: " + prenotazione.getNumeroStanza() + "\n\n";
		    }
	    }
	                    
	    return result;
	}
		
}