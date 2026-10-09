package control;

import entity.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

import exception.DataNotValidException;
import exception.NoDirectorFoundException;
import exception.NoRoomException;
import exception.NotFirstDayException;
import exception.NotUniqueEmailException;

/**
 * Classe Singleton: vi è un'unica istanza della classe accessibile globalmente tramite il metodo getInstance().
 * */
public class Controller {
	
	/**
	 * Istanza static (allocata indipendentemente dall'oggetto e accessibile globalmente)
	 * */
	private static Controller instance = null;
	
	/**
	 * Classe enumerativa per la gestione della validazione delle stringhe.
	 * */
	public enum Contesto {
		NOME,
		COGNOME,
		CITTA,
		EMAIL,
		TELEFONO,
		INDIRIZZO,
		CARTACREDITO
	}
	
	/**
	 * "numeroCatene" è il numero di catene alberghiere del sistema.
	 * Se dovessero essere aggiunte altre catene, lo sviluppatore dovrà tener conto di modificare questo valore.
	 * */
	public final static int numeroCatene = 10;
	
	/**
	 * Costruttore di default: protected in quanto si può accedere al Controller solamente tramite getIstance()
	 * */
	private Controller() {
		super();
	}
	
	/**
	 * Singleton: funzione per ottenere l'istanza del Controller
	 * */
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		
		return instance;
	}
	
	/**
	 * Scenario 1
	 * 
	 * @apiNote La funzione contiene un ciclo "for" che va da 1 a "numeroCatene", dove "numeroCatene" è il numero di
	 * catene alberghiere del sistema.
	 * Se dovessero essere aggiunte altre catene, lo sviluppatore dovrà tener conto di modificare questo valore
	 * 
	 * @param citta Città inserita dall'utente
	 * @param tipologiaCamera Tipologia camera inserita dall'utente
	 * @param dataArrivoStringa Data di arrivo inserita dall'utente come stringa
	 * @param dataPartenzaStringa Data di partenza inserita dall'utente come stringa
	 * @param textArea Area di testo dove compariranno gli alberghi disponibili
	 * @param btn Bottone che comparirà solo nel caso in cui vi siano alberghi disponibili
	 * 
	 * @throws NoRoomException Eccezione che viene scatenata nel caso in cui non viene trovata una camera d'albergo 
	 * della tipologia richiesta dall'utente (Singola, Doppia, Tripla) negli alberghi della città richiesta dall'utente
	 * @throws IllegalArgumentException Chiamato se viene passato qualche valore inappropriato
	 * @throws ArrayIndexOutOfBoundsException L'e-mail viene divisa in 2 parti, tramite un array di stringhe
	 * (con 2 elementi); l'elemento separatore è la @.
	 * Se questa non viene trovata, lo split non avviene e ciò equivale a un array di un solo
	 * elemento. L'elemento parts[1] sarà inaccessibile e significa che il testo inserito
	 * non può corrispondere a una mail
	 * @throws UnknownHostException Gestito da javax.mail, verifica che il dominio dell'e-mail sia valido
	 * @throws NoRoomException Eccezione che viene scatenata nel caso in cui non viene trovata una camera
	 * d'albergo della tipologia richiesta dall'utente (Singola, Doppia, Tripla) negli alberghi della città 
	 * richiesta dall'utente
	 * 
	 * @return Lista con i nomi degli alberghi disponibili
	 * */
	public ArrayList<String> verificaDisponibilitaCamera(String citta, String tipologiaCamera, String dataArrivoStringa,
			String dataPartenzaStringa, JTextArea textArea, JButton btn) {

			// Per salvare i nomi degli alberghi disponibili
			ArrayList<String> nomiAlberghi = new ArrayList<String>();

			try {
				validazioneStringa(citta,Contesto.CITTA);
			} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | UnknownHostException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
			}

			try {
				if(validazioneData(dataArrivoStringa,dataPartenzaStringa)) {
					try {
						ArrayList<EntityAlbergo> alberghiDisponibiliPerCatena = new ArrayList<EntityAlbergo>();

						for(int i = 1; i <= numeroCatene; i++) {
							EntityCatenaAlberghiera catena = new EntityCatenaAlberghiera(i);

							ArrayList<EntityAlbergo> alberghiDisponibili = catena.visualizzaListaAlberghiDisponibili(citta,tipologiaCamera);

							alberghiDisponibiliPerCatena.addAll(alberghiDisponibili);
						}

						if(!alberghiDisponibiliPerCatena.isEmpty()) {
							System.out.println("ALBERGHI DISPONIBILI:\n");

							for(EntityAlbergo albergo : alberghiDisponibiliPerCatena) {
								System.out.println(albergo);
								textArea.append("ALBERGHI DISPONIBILI:\n" + albergo.toString());
							}
							
							btn.setVisible(true);
		                  	btn.setEnabled(true);

							for(EntityAlbergo albergo : alberghiDisponibiliPerCatena) {
								nomiAlberghi.add(albergo.getNome());
							}

						}else {
							
							btn.setVisible(false);
		                  	btn.setEnabled(false);
		                  	
		                  	String error = new String();
		                  	
		                  	if(citta.charAt(0) == 'a' || citta.charAt(0) == 'A') {
		                  		error = "Non esistono alberghi ad " + citta + " con una camera " +
										tipologiaCamera + " disponibile nel periodo selezionato. Siamo spiacenti";
		                  	}else {
		                  		error = "Non esistono alberghi a " + citta + " con una camera " +
										tipologiaCamera + " disponibile nel periodo selezionato. Siamo spiacenti";
		                  	}
							
							throw new NoRoomException(error);
						}

					}catch(NoRoomException e){
						
						btn.setVisible(false);
	                  	btn.setEnabled(false);
						// A questo punto non può procedere a effettua prenotazione
						JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					
					btn.setVisible(false);
	              	btn.setEnabled(false);
				}
			} catch (DataNotValidException e) {
				
				btn.setVisible(false);
	          	btn.setEnabled(false);
				// Qui ritorna alla schermata di inserimento
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
			}

			return nomiAlberghi;
		}

	/**
	 * Scenario 2
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
	 * @return True se la prenotazione va a buon fine, false altrimenti
	 * */
	public boolean effettuaPrenotazione(String nome, String cognome, String email, String telefono,
						String indirizzo, String numeroCartaCredito, String dataArrivoStringa,
						String dataPartenzaStringa, String tipologiaCamera, String nomeAlbergo) {
		boolean check = false;
		
		try {
			if(validazioneStringa(nome,Contesto.NOME) && validazioneStringa(cognome,Contesto.COGNOME) &&
					validazioneStringa(email,Contesto.EMAIL) && validazioneStringa(telefono,Contesto.TELEFONO) && 
					validazioneStringa(indirizzo,Contesto.INDIRIZZO) &&
					validazioneStringa(numeroCartaCredito,Contesto.CARTACREDITO)) {
				
				// Rendo tutte le iniziali maiuscole
				nome = capitalizeString(nome);
				cognome = capitalizeString(cognome);
				indirizzo = capitalizeString(indirizzo);
				
				EntityCliente cliente = new EntityCliente();
				
				try {
					int codice = cliente.effettuaPrenotazione(nome,cognome,email,telefono,indirizzo,numeroCartaCredito,
							dataArrivoStringa, dataPartenzaStringa, tipologiaCamera, nomeAlbergo);
					
					if(codice > 0) {
						// Se la prenotazione è andata a buon fine, il sistema invia al Cliente un report
						// del riepilogo prenotazione
						EntityPrenotazione prenotazione = new EntityPrenotazione(codice);
						
						String prezzoComplessivo = prenotazione.generaPrezzoComplessivo();
						
						JOptionPane.showMessageDialog(null, "Riepilogo prenotazione:\n" + prenotazione, "REGISTRAZIONE EFFETTUATA", JOptionPane.INFORMATION_MESSAGE);
						
						String oggetto = "Riepilogo prenotazione - Alloggio nell'albergo " + prenotazione.getAlbergo().getNome();
							
						String testo = prenotazione.creaRiepilogoPrenotazione(prenotazione.getAlbergo(),
														prenotazione.getCamera(),tipologiaCamera,
														prezzoComplessivo);
						
						inviaEmail(email, oggetto, testo);
						
						check = true;
					}
				} catch (NotUniqueEmailException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "REGISTRAZIONE NON EFFETTUATA", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}catch(IllegalArgumentException | ArrayIndexOutOfBoundsException | UnknownHostException e) {
			// Qui poi si torna alla schermata di inserimento perché c'è stato qualche problema
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return check;
	}
	
	/**
	 * Scenario 3
	 * 
	 * @param nome Nome comunicato al personale della reception da parte del cliente
	 * @param cognome Cognome comunicato al personale della reception da parte del cliente
	 * @param email E-mail comunicata al personale della reception da parte del cliente
	 * 
	 * @return True se il check-in è andato a buon fine, false altrimenti
	 * */
	public boolean effettuaCheckIn(String nome, String cognome, String email) {
		boolean check = false;
		
		try {
			if(validazioneStringa(nome,Contesto.NOME) && validazioneStringa(cognome,Contesto.COGNOME) &&
					validazioneStringa(email,Contesto.EMAIL)) {
				EntityCliente cliente = new EntityCliente(email);
				
				if(cliente.effettuaCheckIn()) {
					check = true;
				}
			}
			
		}catch(IllegalArgumentException | ArrayIndexOutOfBoundsException | UnknownHostException e) {
			// Qui poi si torna alla schermata di inserimento perché c'è stato qualche problema
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return check;
	}
	
	/**
	 * Scenario 4
	 * 
	 * @param nome Nome comunicato al personale della reception da parte del cliente
	 * @param cognome Cognome comunicato al personale della reception da parte del cliente
	 * @param email E-mail comunicata al personale della reception da parte del cliente
	 * 
	 * @throws IllegalArgumentException Chiamato se viene passato qualche valore inappropriato
	 * @throws ArrayIndexOutOfBoundsException L'e-mail viene divisa in 2 parti, tramite un array di stringhe
	 * (con 2 elementi); l'elemento separatore è la @.
	 * Se questa non viene trovata, lo split non avviene e ciò equivale a un array di un solo
	 * elemento. L'elemento parts[1] sarà inaccessibile e significa che il testo inserito
	 * non può corrispondere a una mail
	 * @throws UnknownHostException Gestito da javax.mail, verifica che il dominio dell'e-mail sia valido
	 * 
	 * @return True se il check-out è andato a buon fine, false altrimenti
	 * */
	public int effettuaCheckOut(String nome, String cognome, String email) {
		int codice = -1;
		
		try {
			if(validazioneStringa(nome,Contesto.NOME) && validazioneStringa(cognome,Contesto.COGNOME) &&
					validazioneStringa(email,Contesto.EMAIL)) {
				EntityCliente cliente = new EntityCliente(email);
				
				codice = cliente.effettuaCheckOut();
			}
			
		}catch(IllegalArgumentException | ArrayIndexOutOfBoundsException | UnknownHostException e) {
			// Qui poi si torna alla schermata di inserimento perché c'è stato qualche problema
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return codice;
	}
	
	/**
	 * Scenario 5
	 * 
	 * @param emailDirettore L'e-mail del direttore
	 * 
	 * @throws NotFirstDayException Se non è il primo giorno del mese non è possibile generare il report
	 * @throws NoDirectorFoundException Eccezione che viene scatenata nel caso in cui viene inserita una e-mail 
	 * che non corrisponde a un Direttore nel database.
	 * @throws IllegalArgumentException Chiamato se viene passato qualche valore inappropriato
	 * @throws ArrayIndexOutOfBoundsException L'e-mail viene divisa in 2 parti, tramite un array di stringhe
	 * (con 2 elementi); l'elemento separatore è la @.
	 * Se questa non viene trovata, lo split non avviene e ciò equivale a un array di un solo
	 * elemento. L'elemento parts[1] sarà inaccessibile e significa che il testo inserito
	 * non può corrispondere a una mail
	 * @throws UnknownHostException Gestito da javax.mail, verifica che il dominio dell'e-mail sia valido
	 * 
	 * @return True se è il primo giorno del mese ed è stata comunicata una mail corrispondente a un direttore,
	 * false altirmenti
	 * */
	public boolean generaReportElencoNotti(String emailDirettore) {
		boolean check = false;
		
		try {
			// Lo scenario inizia solo se è il primo giorno del mese (pre-condizione)
			if(isFirstDayOfMonth()) {
				try {
					if(validazioneStringa(emailDirettore,Contesto.EMAIL)) {
						try {
							EntityCatenaAlberghiera catena = new EntityCatenaAlberghiera(emailDirettore);
							
							if(!catena.getEmailDirettore().isEmpty()) {
								String oggetto = "Report elenco notti - Mese di " + reportMonthToString();
								
								inviaEmail(catena.getEmailDirettore(), oggetto, catena.generaReportElencoNotti());
							
								check = true;
							}

						}catch(NoDirectorFoundException e) {
							JOptionPane.showMessageDialog(null, "Il direttore è inesistente", "ERRORE", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					}
					
				}catch(ArrayIndexOutOfBoundsException | UnknownHostException | IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
				}
			
			// Throw per identificare in che casistica scatenare l'eccezione NotFirstDayException
			} else {
				throw new NotFirstDayException("Non è il primo giorno del mese: impossibile generare il report");
			}
			
		// Se non è il primo giorno del mese, si scatena l'eccezione NotFirstDayException
		}catch(NotFirstDayException e) {
			check = true;
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
		}
		
		return check;
	}
	
	/**
	 * Genera la fattura relativa a una prenotazione
	 * 
	 * @param codice Codice della prenotazione
	 * 
	 * @return Fattura formattata come stringa
	 * */
	public String generaFattura(int codice) {
		EntityPrenotazione prenotazione = new EntityPrenotazione(codice);
		
		return prenotazione.generaFattura();
	}
	
	/**
	 * Verifica in base alla data attuale se oggi è il primo giorno del mese
	 * 
	 * @return true se è il primo giorno del mese, false altrimenti
	*/
	private boolean isFirstDayOfMonth() {
		// Data attuale (rilevata dal sistema)
		LocalDate today = LocalDate.now();
		
		// Data attuale con il giorno modificato a 1 (il primo del mese)
		LocalDate firstOfMonth = today.withDayOfMonth(1);
		
		if(today.equals(firstOfMonth) ) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Trasforma il mese per il report (mese attuale - 1) in una stringa, così da stamparla nel report
	 * 
	 * @throws DateTimeException Gestisce l'errore nel recupero della data odierna con LocalDate
	 * 
	 * @return Mese per il report in formato testuale
	 * */
	public String reportMonthToString() {
		String month = new String();
		
		LocalDate today = LocalDate.now();
		
		// Report del mese attuale, ma se è dicembre (12) stampo Novembre, se è gennaio (1) stampo Dicembre...
		switch( today.getMonthValue() ) {
		
		case 1:
			// Mese odierno: Gennaio, nel report si stampa Dicembre
			month = "Dicembre";
			break;
			
		case 2:
			// Mese odierno: Febbraio, nel report si stampa Gennaio
			month = "Gennaio";
			break;
		
		case 3:
			// Mese odierno: Marzo, nel report si stampa Febbraio
			month = "Febbraio";
			break;
			
		case 4:
			// Mese odierno: Aprile, nel report si stampa Marzo
			month = "Marzo";
			break;
			
		case 5:
			// Mese odierno: Maggio, nel report si stampa Aprile
			month = "Aprile";
			break;
			
		case 6:
			// Mese odierno: Giugno, nel report si stampa Maggio
			month = "Maggio";
			break;
			
		case 7:
			// Mese odierno: Luglio, nel report si stampa Giugno
			month = "Giugno";
			break;
			
		case 8:
			// Mese odierno: Agosto, nel report si stampa Luglio
			month = "Luglio";
			break;
			
		case 9:
			// Mese odierno: Settembre, nel report si stampa Agosto
			month = "Agosto";
			break;
			
		case 10:
			// Mese odierno: Ottobre, nel report si stampa Settembre
			month = "Settembre";
			break;
			
		case 11:
			// Mese odierno: Novembre, nel report si stampa Ottobre
			month = "Ottobre";
			break;
			
		case 12:
			// Mese odierno: Dicembre, nel report si stampa Novembre
			month = "Novembre";
			break;
			
		default:
			
			try {
				JOptionPane.showMessageDialog(null,"\nNon è stato possibile generare il mese","ERRORE", JOptionPane.ERROR_MESSAGE );
			}catch(DateTimeException e) {
				System.err.println(e.getMessage());
			}			
		
		}
		
		return month;
	}
	
	/**
	 * Verifica la validità della stringa ricevuta in input in base al contesto.
	 * 
	 * <br><br><b>Nome e Cognome</br></b>
	 * <blockquote>Una stringa nome/cognome è valida se è composta da lettere maiuscole o minuscole, da spazi; anche gli
	 * apostrofi, gli accenti e le lettere speciali äëïöü sono concessi.
	 * (la persona potrebbe avere più nomi o più cognomi/uno spazio tra più termini del cognome).
	 * Esempio: "Anna Maria De Leo".</blockquote>
	 * 
	 * <br><b>Città</br></b>
	 * <blockquote>Una stringa città è valida se è composta da lettere maiuscole o minuscole, da spazi; anche gli
	 * apostrofi e le lettere speciali äëïöü sono concessi; gli accenti NON sono concessi perché le
	 * città italiane non hanno accenti.</blockquote>
	 * 
	 * <br><b>E-mail</br></b>
	 * <blockquote>Un'e-mail è valida se contiene, in ordine: un username, una @ e un dominio valido.</blockquote>
	 * 
	 * <br><b>Telefono</br></b>
	 * <blockquote>Un numero di telefono è valido se non contiene caratteri e trattini (può contenere il + per il 
	 * prefisso, la dimensione della stringa viene valutata in base alla presenza o assenza del + del 
	 * prefisso)</blockquote>
	 * 
	 * <br><b>Indirizzo</br></b>
	 * <blockquote>Un indirizzo è valido se è composto da lettere maiuscole o minuscole, da numeri o da spazi; anche gli
	 * apostrofi, gli accenti e le lettere speciali äëïöü sono concesse.</blockquote>
	 * 
	 * <br><b>Numero carta di credito</br></b>
	 * <blockquote>Un numero della carta di credito è valido se non contiene caratteri speciali o lettere e se 
	 * sono 16 numeri</blockquote>
	 * 
	 * @throws IllegalArgumentException Chiamato se viene passato qualche valore inappropriato
	 * @throws ArrayIndexOutOfBoundsException L'e-mail viene divisa in 2 parti, tramite un array di stringhe
	 * (con 2 elementi); l'elemento separatore è la @.
	 * Se questa non viene trovata, lo split non avviene e ciò equivale a un array di un solo
	 * elemento. L'elemento parts[1] sarà inaccessibile e significa che il testo inserito
	 * non può corrispondere a una mail
	 * @throws UnknownHostException Gestito da javax.mail, verifica che il dominio dell'e-mail sia valido
	 * 
	 * @param input La stringa da verificare
	 * @param contesto Il contesto di validazione (NOME, COGNOME, CITTA)
	 * 
	 * @return true se la stringa è corretta, false altrimenti
	 */
	public boolean validazioneStringa(String input, Contesto contesto) throws IllegalArgumentException,
								ArrayIndexOutOfBoundsException, UnknownHostException{
		boolean check = false;

		// Per verificare se vengono inseriti solo spazi
		String trimmedInput = input.trim();
		
		switch(contesto) {
		
		case NOME:
		
			// ^: inizio stringa
			// a-z: lettere minuscole
			// A-Z: lettere maiuscole
			// àèìòù: per definire le lettere accentate
			// äëïöü: lettere speciali
			// \\s: per gli spazi
			// +: i caratteri indicati possono comparire UNA o PIÙ volte
			// $: fine stringa

			if(trimmedInput.equals("")){
				throw new IllegalArgumentException("Il nome non può essere un campo vuoto!");
			}
			
			if( input.matches("^[a-zA-Zàèìòùäëïöü\\s]+$") ) {
				if(input.length() < 1) {
					throw new IllegalArgumentException("Non hai inserito un nome! Inserisci almeno un carattere.");
				}else if(input.length() > 50) {
					throw new IllegalArgumentException("Il nome è troppo lungo! Inserisci un massimo di 50 caratteri.");
				}else {
					check = true;
				}
			}else {
				throw new  IllegalArgumentException("Il valore inserito non è un nome. "
						+ "Riprova inserendo solo caratteri");
			}
			
			break;
	
		case COGNOME:
		
			// ^: inizio stringa
			// a-z: lettere minuscole
			// A-Z: lettere maiuscole
			// àèìòù: per definire le lettere accentate
			// äëïöü: lettere speciali
			// \\s: per gli spazi
			// +: i caratteri indicati possono comparire UNA o PIÙ volte
			// $: fine stringa
			
			if(trimmedInput.equals("")){
				throw new  IllegalArgumentException("Il cognome non può essere un campo vuoto!");
			}
			
			if( input.matches("^[a-zA-Zàèìòùäëïöü\\s]+$") ) {
				if(input.length() < 1) {
					throw new IllegalArgumentException("Non hai inserito un cognome! Inserisci almeno un carattere.");
				}else if(input.length() > 50) {
					throw new IllegalArgumentException("Il cognome è troppo lungo! Inserisci un massimo di 50 caratteri.");
				}else {
					check = true;
				}
			} else {
				throw new  IllegalArgumentException("Il valore inserito non è un cognome. "
                        + "Riprova inserendo solo caratteri");
			}
			
			break;
			
		case CITTA:
			
			// ^: inizio stringa
			// a-z: lettere minuscole
			// A-Z: lettere maiuscole
			// äëïöü: lettere speciali
			// \\s: per gli spazi
			// +: i caratteri indicati possono comparire UNA o PIÙ volte
			// $: fine stringa
				
			if(trimmedInput.equals("")){
				throw new  IllegalArgumentException("La città non può essere un campo vuoto!");
			}
			
			if( input.matches("^[a-zA-Zäëïöü\\s]+$") ) {
				if(input.length() < 1) {
					throw new IllegalArgumentException("Non hai inserito una città! Inserisci almeno un carattere.");
				}else if(input.length() > 50) {
					throw new IllegalArgumentException("La città è troppo lunga! Inserisci un massimo di 50 caratteri.");
				}else {
					check = true;
				}
			} else {
				throw new  IllegalArgumentException("Il valore inserito non è una città. "
                        + "Riprova inserendo solo caratteri");
			}
				
			break;
			
		case EMAIL:
			
			// ^: inizio stringa
			// a-z: lettere minuscole
			// A-Z: lettere maiuscole
			// 0-9: numeri
			// .: carattere "." (non è un intervallo ma un carattere singolo)
			// _: underscore
			// @: carattere "@" (non è un intervallo ma un carattere singolo)
			// +: i caratteri indicati possono comparire UNA o PIÙ volte
			// $: fine stringa

			/* Se l'email non contiene una @, un . o contiene delle lettere maiuscole,
			 * non inizierà il controllo del dominio. Sicuramente il campo non potrà essere totalmente vuoto 
			*/
			
			// Le lettere maiuscole saranno convertite in minuscole all'atto del caricamento nel database
			if(input.matches("^[a-zA-Z0-9._@]+$")) {
				// Divide la stringa "email" in due elementi: il nome e il dominio
				String[] parts = input.split("@"); 
				
				// Salviamo il dominio in una nuova stringa
				try {
					String domain = parts[1];
					
					// Controllo la validità del dominio
					try {
						InetAddress.getByName(domain);
						
						check = true;
					} catch (UnknownHostException e) {
						// Generiamo un'eccezione se la mail non è valida (dominio inesistente), verificato da java.net
						throw new UnknownHostException("L'indirizzo e-mail non è valido: il dominio inserito è inesistente");
					}
					
				}catch(ArrayIndexOutOfBoundsException e) {
					/* Se non c'è la @ l'array parts non viene correttamente generato
					 * Quindi accedere a parts[1] è un'eccezione ArrayIndexOutOfBoundsException */
					throw new ArrayIndexOutOfBoundsException("Il testo inserito non corrisponde a una mail: "
							+ "è necessario inserire una @ e un dominio");
				}
			}else {
				// if !email.matches("^[a-z0-9.@]+$") viene generata quest'eccezione
				throw new IllegalArgumentException("Il testo inserito non corrisponde a una mail: non sono ammessi "
						+ " caratteri speciali o vuoti");
			}
			
			break;
			
		case TELEFONO:
			
			// ^: inizio stringa
			// 0-9: numeri
			// \\s: spazi
			// *: i caratteri indicati possono comparire ZERO o PIÙ volte
			// $: fine stringa
			
			if(trimmedInput.equals("")){
				throw new  IllegalArgumentException("Il telefono non può essere un campo vuoto!");
			}
			
			/* Un eventuale + può trovarsi solo a inizio stringa.
			 * Se si inserisce il prefisso, sono consentiti 3 caratteri in più */
			if( input.charAt(0) == '+' && input.matches("^[0-9+\\s]*$") ) {
				
				if(input.length() < 11) {
					throw new IllegalArgumentException("Numero di telefono troppo breve!");
				}else if(input.length() > 18){
					throw new IllegalArgumentException("Numero di telefono troppo lungo!");
				}else {
					check = true;
				}
				
			}else if( input.charAt(0) != '+' && input.matches("^[0-9\\s]*$") ) {
				
				if(input.length() < 8) {
					throw new IllegalArgumentException("Numero di telefono troppo breve!");
				}else if(input.length() > 15){
					throw new IllegalArgumentException("Numero di telefono troppo lungo!");
				}else {
					check = true;
				}
			
			// if !telefono.matches("^[0-9\\s+]*$") o se inserisco un numero di caratteri sbagliato viene scatenata l'eccezione
			}else if( !input.matches("^[0-9\\s]*$") ){
				throw new IllegalArgumentException("Il valore inserito non è un numero di telefono. "
						+ " Riprova inserendo solo numeri.\nSe vuoi inserire un prefisso, ricordati di inserire "
						+ " il '+' a inizio numero di telefono e non in altre posizioni.");
			}
			
			break;
			
		case INDIRIZZO:
			
			// ^: inizio stringa
			// a-z: lettere minuscole
			// A-Z: lettere maiuscole
			// 0-9: numeri
			// àèìòù: per definire le lettere accentate
			// äëïöü: lettere speciali
			// \\s; spazi
			// +: i caratteri indicati possono comparire UNA o PIÙ volte
			// $: fine stringa
			
			if(trimmedInput.equals("")){
					throw new  IllegalArgumentException("L'indirizzo non può essere un campo vuoto!");
			}
			
			// Controllo se l'indirizzo contiene solo numeri senza lettere
		    if (input.matches("^[0-9\\s]+$")) {
		        throw new IllegalArgumentException("L'indirizzo deve contenere almeno una lettera. Riprova inserendo numeri e lettere.");
		    }
				
			if( input.matches("^[a-zA-Z0-9àèìòùäëïöü\\s]+$")  ) {
				
				if( input.length() < 1) {
					throw new IllegalArgumentException("Indirizzo troppo breve! Inserisci almeno un carattere.");
				}else if(input.length() > 150) {
					throw new IllegalArgumentException("Indirizzo troppo lungo!");
				}else {
					check = true;
				}
			
			// if !indirizzo.matches("^[a-zA-Z0-9\\s]+$") viene scatenata l'eccezione
			}else {
				throw new IllegalArgumentException("Il valore inserito non è un indirizzo valido. "
						+ "Riprova inserendo solo numeri, lettere e spazi");
			}
			
			break;
			
		case CARTACREDITO:
			
			// ^: inizio stringa
			// 0-9: numeri
			// +: i caratteri indicati possono comparire UNA o PIÙ volte
			// $: fine stringa
			
			// Controllo se l'indirizzo contiene solo numeri senza lettere
		    if (trimmedInput.equals("")) {
		        throw new IllegalArgumentException("La carta di credito non può essere vuota. Riprova.");
		    }
			
			if( input.matches("^[0-9]+$") ) {
				
				if( input.length() != 16) {
					throw new IllegalArgumentException("Numero carta di credito non valido! Sono ammesse solo 16 cifre");
				}else {
					check = true;
				}
			// if !numeroCartaDiCredito.matches("^[0-9\\s]{16}") viene scatenata l'eccezione
			}else {
				throw new IllegalArgumentException("Il valore inserito non è un numero di carta di credito valido."
						+ " Riprova inserendo solo 16 cifre, non inserire lettere e/o caratteri speciali.");
			}
			
			break;
			
		default:
			
			// Il blocco try-catch cattura l'eccezione, ma nel caso default è sempre un errore
			// Quindi in ogni caso verrà chiamata l'eccezione IllegalArgumentException
			throw new IllegalArgumentException("La flag indicante il contesto della validazione della stringa è inesistente");

		}
		
		return check;

	}
	
	/**
	 * Formatta le date nel formato dd/mm/yyyy e controlla se le date di arrivo e di partenza, inserite al fine di
	 * effettuare una prenotazione, sono valide.
	 * 
	 * <br><br>I casi di non validità sono:
	 * 
	 * <dl> - la data di arrivo è successiva o uguale alla data di partenza;
	 * <dl> - la data di partenza è precedente o uguale alla data di arrivo;
	 * <dl> - la data di arrivo è precedente o uguale alla data odierna;
	 * <dl> - la data di partenza è precedente o uguale alla data odierna.
	 * 
	 * @param dataArrivoStringa Data di arrivo inserita come stringa
	 * @param dataPartenzaStringa Data di partenza inserita come stringa
	 * 
	 * @throws DataNotValidException Eccezione che viene scatenata nel caso in cui le date di arrivo e di partenza
	 * non sono valide.
	 * 
	 * @return True se le date sono corrette, false altrimenti
	 * */
	public boolean validazioneData(String dataArrivoStringa, String dataPartenzaStringa) throws DataNotValidException {
		boolean check = false;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
     	LocalDate dataArrivo = LocalDate.parse(dataArrivoStringa, formatter);
      	LocalDate dataPartenza = LocalDate.parse(dataPartenzaStringa, formatter);

      	if (dataArrivo.isAfter(dataPartenza)) {
      		throw new DataNotValidException("La data di arrivo è successiva alla data di partenza!");
      		
      	}else if(dataArrivo.isEqual(dataPartenza)) {
      		throw new DataNotValidException("La data di arrivo è uguale alla data di partenza!");
      		
      	}else if(dataPartenza.isBefore(dataArrivo)) {
      		throw new DataNotValidException("La data di partenza è precedente alla data di arrivo!");
      		
      	}else if(dataPartenza.isEqual(dataArrivo)) {
      		throw new DataNotValidException("La data di partenza è uguale alla data di arrivo!");
      		
      	}else if(dataArrivo.isBefore(LocalDate.now())) {
      		throw new DataNotValidException("La data di arrivo è precedente alla data odierna!");
      		
      	}else if(dataArrivo.isEqual(LocalDate.now())) {
      		throw new DataNotValidException("La data di arrivo è uguale alla data odierna!");
      		
      	}else if(dataPartenza.isBefore(LocalDate.now())) {
      		throw new DataNotValidException("La data di partenza è precedente alla data odierna!");
      		
      	}else if(dataPartenza.isEqual(LocalDate.now())) {
      		throw new DataNotValidException("La data di partenza è uguale o precedente alla data odierna!");
      		
      	}else {
      		check = true;
      	}
      	
      	return check;
	}
	
	/**
	 * Trasforma la stringa in input in una stringa con le iniziali di ogni parola maiuscole.
	 * 
	 * @param input Stringa di cui effettuare la conversione
	 * 
	 * @apiNote Il risultato finale è restituito senza spazi vuoti residui grazie al metodo trim()
	 * 
	 * @throws IllegalArgumentException Chiamato se viene passato qualche valore inappropriato
	 * 
	 * @return Stringa di input con iniziali maiuscole
	 * */
	public String capitalizeString(String input) {
		
		// Vettore di stringhe: separiamo le parole di cui effettuare la conversione grazie agli spazi
		String[] words = input.split(" ");
		
		// Il tipo StringBuilder fornisce il metodo append per ricreare la stringa
	    StringBuilder output = new StringBuilder();
	    
	    try {
	    	// Scorro con un range-based for le stringhe di "strings"
			for (String word : words) {
				
				// Il primo carattere della stringa (quello in posizione 0, input.charAt(0) )
				// si può rendere maiuscolo grazie alla classe wrapper del tipo char, Character, che include 
				// un metodo per convertire un char in maiuscolo
				Character firstChar = Character.toUpperCase(word.charAt(0));
				
				// Il resto della parola viene salvato in una sottostringa (stavolta tutta minuscola)
				String restOfWord = word.substring(1).toLowerCase();
				
				// Creo la stringa di output concatenando i risultati ottenuti
				output.append(firstChar).append(restOfWord).append(" ");
				
				// Il processo si ripete finché non termina la stringa di input
			}
	    	
		// L'IllegalArgumentException serve a catturare il caso in cui la lunghezza di "string" sia < 0
	    } catch(IllegalArgumentException e) {
	    	System.err.println("Non è stata inserita nessuna stringa di cui effettuare la conversione");
	    }
	
	    return output.toString().trim();
	}
	
	/**
	 * Invia un'email con oggetto "oggetto" e testo "testo" al destinatario "email".
	 * 
	 * @apiNote Il server e-mail rimane un servizio esterno in quanto non possiamo modificarne i vincoli.
	 * La funzione funge solo da interfacciamento al server stesso.
	 * 
	 * @throws MessagingException Errore nell'invio dell'e-mail o nella creazione degli oggetti per l'invio
	 * @throws NullPointerException Errore nella creazione degli oggetti per l'invio (talvolta, se c'è un problema
	 * di connessione al server Outlook, javax prova a creare qualche oggetto il cui riferimento è null)
	 * 
	 * @param email L'e-mail del destinatario
	 * @param oggetto L'oggetto dell'e-mail
	 * @param testo Il testo dell'e-mail
	*/
	public void inviaEmail(String email, String oggetto, String testo) {
		
		JOptionPane.showMessageDialog(null, "\nInvio email in corso...", "INVIO EMAIL", JOptionPane.INFORMATION_MESSAGE);
		
		// Connessione al server Outlook
		
	    // Parametri per la connessione al server SMTP
	    String host = "smtp-mail.outlook.com";
	    int port = 587;
	    String username = "gestione.prenotazioni@outlook.it";
	    String password = "dbaAlbergo_23";
	
	    // Proprietà per la configurazione della sessione di posta
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	
	    // Creazione di una sessione di posta con autenticazione
	    Session session = Session.getInstance(props, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });
	
	    try {
	        // Creazione di un oggetto MimeMessage
	        MimeMessage message = new MimeMessage(session);
	
	        // Posta del mittente
	        message.setFrom(new InternetAddress("gestione.prenotazioni@outlook.it"));
	
	        // Posta del destinatario
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
	
	        // Oggetto della mail 
	        message.setSubject(oggetto);
	
	        // Contenuto della mail
	        message.setText(testo);
	
	        // Invio della mail
	        Transport.send(message);
	        
	        JOptionPane.showMessageDialog(null, "\nE-mail inviata a: " + email, "INVIO EMAIL", JOptionPane.INFORMATION_MESSAGE);
	        
	    } catch (MessagingException | NullPointerException e) {
	    	JOptionPane.showMessageDialog(null, "Impossibile inviare email", "Qualcosa è andato storto", JOptionPane.ERROR_MESSAGE);
	    	
	    }
		
	}
	
}