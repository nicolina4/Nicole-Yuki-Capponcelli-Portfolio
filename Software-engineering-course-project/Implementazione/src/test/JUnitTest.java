package test;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import control.Controller;
import control.Controller.Contesto;

public class JUnitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testInputValidiEffettuaPrenotazione() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		boolean expectedNome=true;
		Controller singleton = Controller.getInstance();
		boolean checkNome = singleton.validazioneStringa("Mario",Contesto.NOME);
		assertEquals(expectedNome,checkNome);
		
		boolean expectedCognome=true;
		boolean checkCognome = singleton.validazioneStringa("Rossi",Contesto.COGNOME);
		assertEquals(expectedCognome,checkCognome);
		
		boolean expectedEmail=true;
		boolean checkEmail = singleton.validazioneStringa("mario_rossi@gmail.com",Contesto.EMAIL);
		assertEquals(expectedEmail,checkEmail);
			
		boolean expectedTelefono=true;
		boolean checkTelefono = singleton.validazioneStringa("081872701",Contesto.TELEFONO);
		assertEquals(expectedTelefono,checkTelefono);
		
		boolean expectedCartaCredito=true;
		boolean checkCartaCredito = singleton.validazioneStringa("8904561237895674",Contesto.CARTACREDITO);
		assertEquals(expectedCartaCredito,checkCartaCredito);
	}
	
	@Test
	public void testValidazioneNomeConSimboli() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("M4r10",Contesto.NOME);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneNumeroCaratteriNome() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("Marioooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo",Contesto.NOME);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneCognomeConSimboli() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("R0ss1",Contesto.COGNOME);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneNumeroCaratteriCognome() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("Rossiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",Contesto.COGNOME);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneDominioEmail() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("mario_rossi@gm41l.com",Contesto.EMAIL);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneCarattereChiocciolaEmail() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("mario_rossi_gmail.com",Contesto.EMAIL);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneNumeroCaratteriTelefono() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("081872701000000000000000",Contesto.TELEFONO);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneNumeroCaratteriSuperioriCartaCredito() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("890456129037895674",Contesto.CARTACREDITO);
		assertEquals(expected,check);
	}
	
	@Test
	public void testValidazioneNumeroCaratteriInferioriCartaCredito() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		//fail("Not yet implemented");
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("890895674",Contesto.CARTACREDITO);
		assertEquals(expected,check);
	}
	
	@Test
	public void testInputValidiGeneraReportElencoNotti() throws ArrayIndexOutOfBoundsException, UnknownHostException, IllegalArgumentException {
		boolean expected=true;
		Controller singleton = Controller.getInstance();
		boolean check = singleton.validazioneStringa("mariorossi@gmail.com",Contesto.EMAIL);
		assertEquals(expected,check);
	}

}